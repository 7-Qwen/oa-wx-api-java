package com.wen.oawxapi.common.config.ymlArgementConfig.oa;

import com.wen.oawxapi.common.config.ymlArgementConfig.face.FaceSonOfOaConfig;
import com.wen.oawxapi.common.config.ymlArgementConfig.xcx.WxSonOfOaConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 7wen
 * @Date: 2023-05-22 16:10
 * @description:
 */
@Data
@ConfigurationProperties(prefix = "oa")
public class OaConfig {
    private JWTSonOfOaConfig jwt;
    private WxSonOfOaConfig wx;
    private FaceSonOfOaConfig face;
    private EmailSonOfOaConfig email;
    private String imageFolder;
}
