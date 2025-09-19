package com.lnjoying.justice.aos.helm.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/3 19:06
 */

public abstract class AbstractResultParser<V extends AbstractCommandResult>
{
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    protected CommandStatusCode statusCode;

    protected List<String> statusMessages = new ArrayList<>();


    public  abstract V parse();

    public CommandStatusCode getStatusCode()
    {
        return statusCode;
    }

    public List<String> getStatusMessages()
    {
        return statusMessages;
    }

    public void setStatusCode(CommandStatusCode statusCode)
    {
        this.statusCode = statusCode;
    }

    public void setStatusMessages(List<String> statusMessages)
    {
        this.statusMessages = statusMessages;
    }
}
