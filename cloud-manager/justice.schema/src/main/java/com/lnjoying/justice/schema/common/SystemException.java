package com.lnjoying.justice.schema.common;

import com.micro.core.exception.BaseException;

public class SystemException extends BaseException
{
    /** Serial version UID */
    private static final long serialVersionUID = -89334987778999223L;

    /** Error code */
    private ErrorCode code;

    /** Error level */
    private ErrorLevel level;

    private String detailMsg;

    /** HTTP response code (optional) */
    private int httpResponseCode = -1;

    public SystemException(ErrorCode code, ErrorLevel level)
    {
        this(code, null, level);
    }

    public SystemException(ErrorCode code, ErrorLevel level, String detailMsg)
    {
        this(code, null, level);
        this.detailMsg = detailMsg;
    }

    public SystemException(ErrorCode code, ErrorLevel level, int httpResponseCode)
    {
        this(code, null, level);
        setHttpResponseCode(httpResponseCode);
    }

    public SystemException(ErrorCode code, Throwable cause, ErrorLevel level)
    {
        super(code.getMessage(), cause);
        this.code = code;
        this.level = level;
    }

    public String getDetailMsg()
    {
        return detailMsg;
    }
    /**
     * Get the error code.
     *
     * @return
     */
    public ErrorCode getCode()
    {
        return code;
    }

    /**
     * Set the error code.
     *
     * @param code
     */
    public void setCode(ErrorCode code)
    {
        this.code = code;
    }

    /**
     * Get the error level.
     *
     * @return
     */
    public ErrorLevel getLevel()
    {
        return level;
    }

    /**
     * Set the error level.
     *
     * @param level
     */
    public void setLevel(ErrorLevel level)
    {
        this.level = level;
    }

    /**
     * Get HTTP response code. (Used in REST services)
     *
     * @return
     */
    public int getHttpResponseCode()
    {
        return httpResponseCode;
    }

    /**
     * Set HTTP response code. (Used in REST services)
     *
     * @param httpResponseCode
     */
    public void setHttpResponseCode(int httpResponseCode)
    {
        this.httpResponseCode = httpResponseCode;
    }

    /**
     * Check whether an HTTP response code was set.
     *
     * @return
     */
    public boolean hasHttpResponseCode()
    {
        return getHttpResponseCode() != -1;
    }
}
