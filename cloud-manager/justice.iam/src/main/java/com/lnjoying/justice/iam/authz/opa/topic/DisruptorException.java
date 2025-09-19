package com.lnjoying.justice.iam.authz.opa.topic;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:48
 */

public class DisruptorException extends RuntimeException
{
    public DisruptorException() {
        super();
    }

    public DisruptorException(String message) {
        super(message);
    }

    public DisruptorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisruptorException(Throwable cause) {
        super(cause);
    }

    protected DisruptorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
