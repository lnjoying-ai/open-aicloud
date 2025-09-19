package com.lnjoying.justice.aos.helm.command.result;


import com.lnjoying.justice.aos.helm.command.AbstractCommandResult;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/9 17:09
 */

public class ListCommandResult<T> extends AbstractCommandResult
{
    private T data;

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}
