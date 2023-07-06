package com.wen.oawxapi.common.utils;

import cn.hutool.core.util.StrUtil;
import com.wen.oawxapi.common.exception.CustomException;
import com.wen.oawxapi.mapper.TbUserMapper;
import com.wen.oawxapi.entity.TbUser;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: 7wen
 * @Date: 2023-05-20 11:05
 * @description: 存储token的线程工具类
 */
@Component
@Data
public class ThreadLocalToken {
    //初始化token值
    private String token;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private TbUserMapper tbUserMapper;

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 获取当前登录用户
     */
    public TbUser getCurrentUser() {
        if (StrUtil.isBlank(this.token)) {
            throw new CustomException("无法鉴别当前用户的token");
        }
        //校验token
        jwtUtils.verifyToken(this.token);
        return tbUserMapper.selectById(jwtUtils.getUserId(this.token));
    }

    /**
     * 获取token
     */
    public String getToken() {
        return threadLocal.get();
    }

    /**
     * 设置token
     */
    public void setToken(String token) {
        threadLocal.set(token);
        this.token = token;
    }

    /**
     * 清空token
     */
    public void clear() {
        threadLocal.remove();
    }
}
