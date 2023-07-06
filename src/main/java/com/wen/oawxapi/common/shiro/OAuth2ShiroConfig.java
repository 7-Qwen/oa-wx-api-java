package com.wen.oawxapi.common.shiro;

import com.wen.oawxapi.common.config.ymlArgementConfig.oa.OaConfig;
import com.wen.oawxapi.common.filter.OAuth2Filter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: 7wen
 * @Date: 2023-05-22 14:52
 * @description: shiro整合配置类
 */
@Configuration
public class OAuth2ShiroConfig {

    @Autowired
    private OaConfig oaConfig;
    /**
     * 设置安全管理器(核心),将shiro中令牌的管理方法realm设置到其中
     */
    @Bean
    public SecurityManager securityConfig(OAuth2Realm oAuth2Realm) {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(oAuth2Realm);
        defaultSecurityManager.setRememberMeManager(null);
        return defaultSecurityManager;
    }


    /**
     * 过滤器设置
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, OAuth2Filter oAuth2Filter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //设置filter
        Map<String, Filter> filterMap = new HashMap<>(1);
        filterMap.put("oauthFilter", oAuth2Filter);
        shiroFilterFactoryBean.setFilters(filterMap);

        //设置哪些请求不通过shiro处理
        Map<String, String> notBeFilter = new LinkedHashMap<>();
        //获取参数配置
        notBeFilter.put("/webjars/**", "anon");
        notBeFilter.put("/druid/**", "anon");
        notBeFilter.put("/app/**", "anon");
        notBeFilter.put("/sys/login", "anon");
        notBeFilter.put("/swagger/**", "anon");
        notBeFilter.put("/v2/api-docs", "anon");
        notBeFilter.put("/swagger-ui.html", "anon");
        notBeFilter.put("/swagger-resources/**", "anon");
        notBeFilter.put("/captcha.jpg", "anon");
        notBeFilter.put("/user/register", "anon");
        notBeFilter.put("/user/login", "anon");
        notBeFilter.put("/test/**", "anon");
        notBeFilter.put("/user/test", "anon");
        //以上请求外所有请求都要经过filter进行处理
        notBeFilter.put("/**", "oauthFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(notBeFilter);
        return shiroFilterFactoryBean;
    }


    /**
     * shiro生命周期
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * shiro切面方法
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityConfig) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityConfig);
        return advisor;
    }
}
