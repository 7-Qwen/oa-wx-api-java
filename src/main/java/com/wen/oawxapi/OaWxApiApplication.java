package com.wen.oawxapi;

import cn.hutool.core.util.StrUtil;
import com.wen.oawxapi.common.config.ymlArgementConfig.oa.OaConfig;
import com.wen.oawxapi.common.environment.SystemConstant;
import com.wen.oawxapi.entity.SysConfig;
import com.wen.oawxapi.service.base.BaseSysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@SpringBootApplication
@ServletComponentScan
@ConfigurationPropertiesScan
@EnableAsync
@Slf4j
public class OaWxApiApplication {
    @Resource
    private BaseSysConfigService baseSysConfigService;
    @Resource
    private SystemConstant systemConstant;
    @Resource
    private OaConfig oaConfig;

    public static void main(String[] args) {
        SpringApplication.run(OaWxApiApplication.class, args);
    }

    @PostConstruct
    public void init() {
        List<SysConfig> allConfigValue = baseSysConfigService.getAllConfigValue();
        allConfigValue.forEach(sysConfig -> {
            //获取key转换大小写
            String paramKey = sysConfig.getParamKey();
            paramKey = StrUtil.toCamelCase(paramKey);
            //使用反射注入容器中的实例对象
            try {
                Field declaredField = systemConstant.getClass().getDeclaredField(paramKey);
                declaredField.set(systemConstant, sysConfig.getParamValue());
            } catch (Exception e) {
                log.error("出现异常", e);
            }
        });
        //新建文件夹
        new File(oaConfig.getImageFolder()).mkdirs();
    }
}
