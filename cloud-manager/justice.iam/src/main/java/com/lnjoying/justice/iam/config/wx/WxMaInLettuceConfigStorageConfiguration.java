package com.lnjoying.justice.iam.config.wx;

import cn.binarywang.wx.miniapp.config.WxMaConfig;
import com.binarywang.spring.starter.wxjava.miniapp.config.storage.AbstractWxMaConfigStorageConfiguration;
import com.binarywang.spring.starter.wxjava.miniapp.properties.WxMaProperties;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.redis.WxRedisOps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/20 17:19
 */

@Configuration
@ConditionalOnProperty(prefix = WxMaProperties.PREFIX + ".config-storage", name = "type",
        matchIfMissing = true, havingValue = "Jedis")
@RequiredArgsConstructor
public class WxMaInLettuceConfigStorageConfiguration extends AbstractWxMaConfigStorageConfiguration
{
    private final WxMaProperties properties;

    @Bean
    @ConditionalOnMissingBean(WxMaConfig.class)
    public WxMaConfig wxMaConfig() {
        WxMaLettuceConfigImpl config = getWxMaInRedisTemplateConfigStorage();
        return this.config(config, properties);
    }

    private WxMaLettuceConfigImpl getWxMaInRedisTemplateConfigStorage() {
        WxRedisOps redisOps = new LettuceWxRedisOps();
        return new WxMaLettuceConfigImpl(redisOps, properties.getConfigStorage().getKeyPrefix());
    }
}
