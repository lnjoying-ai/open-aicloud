package com.lnjoying.justice.iam.authz.opa.pep;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:48
 */

public class PepException extends RuntimeException
{
    public PepException() {
        super();
    }

    public PepException(String message) {
        super(message);
    }

    public PepException(String message, Throwable cause) {
        super(message, cause);
    }

    public PepException(Throwable cause) {
        super(cause);
    }

    protected PepException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
