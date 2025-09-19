package com.lnjoying.justice.schema.entity;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/5/6 16:49
 */

public class RpcResult<T> implements Serializable
{
    private int code;

    private String message;

    private T data;

    public RpcResult()
    {
    }

    public RpcResult(int code, String message, T data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RpcResult(int code, T data)
    {
        this.code = code;
        this.data = data;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public boolean ok()
    {
        return this.code == 0;
    }

    public boolean systemError()
    {
        return this.code == 9999;
    }

    @Override
    public String toString()
    {
        return "RpcResult{" + "code=" + code + ", message='" + message + '\'' + ", data=" + data + '}';
    }

}
