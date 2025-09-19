package com.lnjoying.justice.aos.helm.inner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/29 20:37
 */

@SuppressWarnings({"PMD.ThreadPoolCreationRule", "checkstyle:overloadmethodsdeclarationorder",
        "checkstyle:missingjavadocmethod"})
public final class ExecutorFactory
{
    public static ScheduledExecutorService newSingleScheduledExecutorService(final ThreadFactory threadFactory) {
        return Executors.newScheduledThreadPool(1, threadFactory);
    }
}
