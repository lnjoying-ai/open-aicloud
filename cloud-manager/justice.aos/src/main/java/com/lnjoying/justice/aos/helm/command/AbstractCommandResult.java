package com.lnjoying.justice.aos.helm.command;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/30 16:29
 */

public abstract class AbstractCommandResult
{
    private CommandStatusCode statusCode;

    private String statusMessage;

    public CommandStatusCode getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(CommandStatusCode statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString()
    {
        return "statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' ;
    }
}
