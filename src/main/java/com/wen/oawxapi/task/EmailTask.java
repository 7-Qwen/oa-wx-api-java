package com.wen.oawxapi.task;

import com.wen.oawxapi.common.config.ymlArgementConfig.oa.OaConfig;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: 7wen
 * @Date: 2023-07-05 20:59
 * @description:
 */
@Component
@Scope("prototype")
public class EmailTask {
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private OaConfig oaConfig;


    /**
     * 发送邮箱
     */
    @Async
    public void sendAsync(SimpleMailMessage simpleMailMessage) {
        simpleMailMessage.setFrom(oaConfig.getEmail().getSystem());
        javaMailSender.send(simpleMailMessage);
    }
}
