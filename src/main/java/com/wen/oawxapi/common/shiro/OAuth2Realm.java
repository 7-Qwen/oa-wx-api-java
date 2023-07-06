package com.wen.oawxapi.common.shiro;

import com.wen.oawxapi.common.config.ymlArgementConfig.oa.OaConfig;
import com.wen.oawxapi.common.exception.CustomException;
import com.wen.oawxapi.common.utils.JwtUtils;
import com.wen.oawxapi.entity.TbUser;
import com.wen.oawxapi.service.UserService;
import com.wen.oawxapi.service.base.BaseTbUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author: 7wen
 * @Date: 2023-05-19 21:47
 * @description: shiro通过token完成对用户的权限/认证;定义认证与授权的实现方法
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private BaseTbUserService baseTbUserService;
    @Resource
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //查询用户相关的组织权限信息
        TbUser user = (TbUser) principalCollection.getPrimaryPrincipal();
        Long userId = user.getId();
        Set<String> userPermission = userService.getUserPermission(userId);
        //向info添加授权信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(userPermission);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从token令牌中获取userId 查询该用户是否被冻结
        String token = (String) authenticationToken.getPrincipal();
        Long userId = jwtUtils.getUserId(token);
        TbUser user = baseTbUserService.lambdaQuery()
                .eq(TbUser::getId, userId)
                .eq(TbUser::getStatus, 1)
                .last("limit 1")
                .one();
        if (user == null) {
            throw new CustomException("用户已被锁定");
        }
        //往info中添加用户信息以及token字符串
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, token, getName());
        return info;
    }
}
