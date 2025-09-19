package com.lnjoying.justice.iam.authz.opa.client.rest.util;

import com.lnjoying.justice.iam.authz.opa.client.OpaClientException;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:49
 */

public class InvalidEndpointException extends OpaClientException
{
    public InvalidEndpointException() {
        super();
    }

    public InvalidEndpointException(String message) {
        super(message);
    }

    public InvalidEndpointException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEndpointException(Throwable cause) {
        super(cause);
    }

    protected InvalidEndpointException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
