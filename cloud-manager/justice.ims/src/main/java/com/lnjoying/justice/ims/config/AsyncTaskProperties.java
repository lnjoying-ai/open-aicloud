package com.lnjoying.justice.ims.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * async task properties
 *
 * @author merak
 **/

@Data
@Component
public class AsyncTaskProperties
{
    private int corePoolSize = Runtime.getRuntime().availableProcessors() < 4 ? Runtime.getRuntime().availableProcessors() : 4;

    private int maxPoolSize = corePoolSize * 4;

    private int keepAliveSeconds = 60;

    private int queueCapacity = 1024;
}
