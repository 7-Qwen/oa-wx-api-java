package com.wen.oawxapi.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author: 7wen
 * @Date: 2023-05-19 21:07
 * @description: 服务端将生成的Token返回给客户端 客户端携Token访问服务器的时候 通过shiro认证Token是否有效
 * <p>
 * Token不能直接传递给shiro 所以本类是将Token转换为shiro可以进行处理的类
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
