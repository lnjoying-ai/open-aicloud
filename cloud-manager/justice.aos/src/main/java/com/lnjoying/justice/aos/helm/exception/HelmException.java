package com.lnjoying.justice.aos.helm.exception;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/29 19:08
 */

public class HelmException extends RuntimeException
{

    private static final long serialVersionUID = -693819100813036402L;

    protected HelmException()
    {
        super();
    }

    protected HelmException(final String message)
    {
        super(message);
    }

    protected HelmException(final Throwable cause)
    {
        super(cause);
    }

    protected HelmException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
