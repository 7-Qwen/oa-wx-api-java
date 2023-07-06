package com.wen.oawxapi.common.config.ymlArgementConfig.oa;

import lombok.Data;

import java.util.List;

@Data
public class JWTSonOfOaConfig {
    private String secret;
    private int expire;
    private int cacheExpire;
}