package com.micro.core.exception;

public class BaseException extends RuntimeException
{
    /** Serial version UID */
    private static final long serialVersionUID = 1L;

    public BaseException() {
        super();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
