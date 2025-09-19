package com.lnjoying.justice.aos.config;

import lombok.Data;
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

    private int maxPoolSize = corePoolSize * 2;

    private int keepAliveSeconds = 60;

    private int queueCapacity = 1024;
}
