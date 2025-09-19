package com.lnjoying.justice.iam.config.opa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/2 10:56
 */

@Configuration
@ConfigurationProperties(DisruptorProperties.PREFIX)
@Data
public class DisruptorProperties
{
    public static final String PREFIX = "opa.disruptor";

    private int ringBufferSize = 1024;

    private int ringThreadNumbers = 4;
}
