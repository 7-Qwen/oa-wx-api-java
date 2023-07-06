package com.wen.oawxapi.common.aop;

import cn.hutool.core.util.StrUtil;
import com.wen.oawxapi.common.utils.R;
import com.wen.oawxapi.common.utils.ThreadLocalToken;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 7wen
 * @Date: 2023-05-22 15:19
 * @description: 通过切面返回新的令牌(如果刷新的话)
 */
@Aspect
@Component
public class ShiroAop {
    @Autowired
    private ThreadLocalToken threadLocalToken;

    @Pointcut("execution(public * com.wen.oawxapi.controller.*.*(..))")
    public void aspect() {

    }

    @Around("aspect()")
    public Object flushedToken(ProceedingJoinPoint point) throws Throwable {
        R r = (R) point.proceed();
        //校验当前线程是否有新的token
        String token = threadLocalToken.getToken();
        if (StrUtil.isNotBlank(token)) {
            r.put("token", token);
            //清空线程中的token
            threadLocalToken.clear();
        }
        return r;
    }

}
