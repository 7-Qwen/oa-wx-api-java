package com.wen.oawxapi.common.filter;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.wen.oawxapi.common.shiro.OAuth2Token;
import com.wen.oawxapi.common.utils.JwtUtils;
import com.wen.oawxapi.common.utils.ThreadLocalToken;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author: 7wen
 * @Date: 2023-05-20 11:57
 * @description: shiro过滤web请求 验证token有效性
 */
@Component
@Scope("prototype")
public class OAuth2Filter extends AuthenticatingFilter {
    @Autowired
    private ThreadLocalToken threadLocalToken;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${oa.jwt.cache-expire}")
    private int cacheExpire;


    /**
     * 从request中获取token 并将token转换为shiro能处理的Token包装类
     */

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse){
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = getRequestToken(httpServletRequest);
        if (StrUtil.isBlank(token)) {
            return null;
        }
        return new OAuth2Token(token);
    }


    /**
     * 是否允许通过shiro请求的处理方法
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //ajax在发送json格式请求的时候会先发送options请求试探性请求,该请求可以直接通过 不让shiro进行处理
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        //其他请求都要通过shiro处理
        return false;
    }


    /**
     * 所有经过shiro处理的请求的解决方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //转换类型
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        //设置返回类型
        httpServletResponse.setHeader("Content-Type", "text/html;charset=UTF-8");
        //允许跨域请求
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        threadLocalToken.clear();

        //获取token
        String token = getRequestToken(httpServletRequest);
        //如果Token为空
        if (StrUtil.isBlank(token)) {
            //设置返回码
            httpServletResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
            //设置返回消息
            httpServletResponse.getWriter().print("无效的令牌");
            return false;
        }

        //校验token
        try {
            jwtUtils.verifyToken(token);
        } catch (TokenExpiredException e) {
            //如果出现了过期异常
            //1.检查cache是否过期
            if (redisTemplate.hasKey(token)) {
                //如果Redis中存在token,说明长token没有过期,则生成新的token返回
                Long userId = jwtUtils.getUserId(token);
                token = jwtUtils.createToken(userId);
                //更新redis
                redisTemplate.delete(token);
                redisTemplate.opsForValue().set(token, userId + "", cacheExpire, TimeUnit.DAYS);
                //将新的token绑定到线程中
                threadLocalToken.setToken(token);
            } else {
                //如果Redis中不存在,则说明短长token都已过期,需要重新登录
                //设置返回码
                httpServletResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
                //设置返回消息
                httpServletResponse.getWriter().print("令牌已过期");
                return false;
            }
        } catch (JWTDecodeException e) {
            //如果出现了校验异常
            //设置返回码
            httpServletResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
            //设置返回消息
            httpServletResponse.getWriter().print("非法令牌");
            return false;
        }

        //如果令牌校验成功,则将token存入当前线程中,以获取当前登录用户信息;并执行登录流程
        threadLocalToken.setToken(token);
        return executeLogin(servletRequest, servletResponse);
    }


    /**
     * 如果校验失败则返回的动作
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //转换类型
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //设置返回类型
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        //允许跨域请求
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        try {
            httpServletResponse.getWriter().print(e.getMessage());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return false;
    }

    /**
     * 从request中获取token
     */
    private String getRequestToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = httpServletRequest.getParameter("token");
        }
        return token;
    }
}
