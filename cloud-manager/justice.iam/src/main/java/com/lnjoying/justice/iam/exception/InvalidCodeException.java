package com.lnjoying.justice.iam.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown when the verification code is wrong
 *
 * @author merak
 **/

public class InvalidCodeException extends AuthenticationException
{
    public InvalidCodeException(String msg)
    {
        super(msg);
    }

    public InvalidCodeException(String msg, Throwable t)
    {
        super(msg, t);
    }
}
