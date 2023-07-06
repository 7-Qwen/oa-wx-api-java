package com.wen.oawxapi.common.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wen.oawxapi.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: 7wen
 * @Date: 2023-05-19 20:44
 * @description: token令牌工具类
 */
@Component
public class JwtUtils {
    @Value("${oa.jwt.secret}")
    private String secret;

    @Value("${oa.jwt.expire}")
    private int expire;


    /**
     * 通过userId构建token
     */
    public String createToken(Long uid) {
        if (uid == null) {
            throw new CustomException("用户id不能为空");
        }
        //计算过期时间
        DateTime offset = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, expire);
        //调用算法类获取秘钥
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //构建jwt并返回token
        return JWT.create().withClaim("userId", uid).withExpiresAt(offset).sign(algorithm);
    }


    /**
     * 通过token获取userId
     */
    public Long getUserId(String token) {
        if (StrUtil.isEmpty(token)) {
            throw new CustomException("token不能为空");
        }
        Claim userId;
        try {
            DecodedJWT decode = JWT.decode(token);
            userId = decode.getClaim("userId");
        } catch (Exception e) {
            throw new CustomException("令牌无效");
        }
        return userId.asLong();
    }


    /**
     * 校验token
     * 不会返回值的原因是 如果校验不通过会报异常
     */
    public void verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier build = JWT.require(algorithm).build();
        build.verify(token);
    }
}
