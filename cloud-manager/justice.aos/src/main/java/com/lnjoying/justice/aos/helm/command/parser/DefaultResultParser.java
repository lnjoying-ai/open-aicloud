package com.lnjoying.justice.aos.helm.command.parser;


import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.result.DefaultCommandResult;

import java.util.function.Consumer;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/17 11:24
 */

public class DefaultResultParser extends AbstractResultParser<DefaultCommandResult>
        implements Consumer<String>
{

    @Override
    public DefaultCommandResult parse()
    {
        DefaultCommandResult result = new DefaultCommandResult();
        result.setStatusCode(statusCode);
        result.setStatusMessage(String.join(LINE_SEPARATOR, statusMessages));
        return result;
    }

    @Override
    public void accept(String s)
    {
        if (s.startsWith("Error:"))
        {
            statusCode = CommandStatusCode.FAILURE;
        }
        else
        {
            statusCode = CommandStatusCode.SUCCESS;
        }
        statusMessages.add(s);
    }
}
