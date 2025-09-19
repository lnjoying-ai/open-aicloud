package com.lnjoying.justice.aos.helm.command.impl;

import com.lnjoying.justice.aos.helm.command.AbstractChartCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.result.RollbackCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/3 20:20
 */

public class RollbackCommand extends AbstractChartCommand<RollbackCommandResult>
{
    public static final String COMMAND_ROLLBACK = "rollback";
    private String releaseName;

    public RollbackCommand()
    {
        super();
    }

    public RollbackCommand(Logger logger)
    {
        super(logger);
    }

    public Optional<String> getReleaseName()
    {
        return Optional.ofNullable(this.releaseName);
    }

    public void setReleaseName(String releaseName)
    {
        this.releaseName = releaseName;
    }

    @Override
    protected RollbackCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();

        ResultParser parsingConsumer = new ResultParser();

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_ROLLBACK);
        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        RollbackCommandResult result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }

    @Override
    protected void moreFlags(List<String> flags)
    {
        getReleaseName().ifPresent(releaseName -> {
            flags.add(releaseName);
        });
        super.moreFlags(flags);
    }

    private static final class ResultParser extends AbstractResultParser<RollbackCommandResult>
            implements Consumer<String>
    {

        @Override
        public RollbackCommandResult parse()
        {
            RollbackCommandResult result = new RollbackCommandResult();
            result.setStatusCode(statusCode);
            result.setStatusMessage(String.join(LINE_SEPARATOR, statusMessages));
            return result;
        }

        @Override
        public void accept(String s)
        {
            if (Objects.nonNull(s))
            {
                if (s.startsWith("Rollback")) {
                    statusCode = CommandStatusCode.SUCCESS;
                }
                else if (s.startsWith("Error:"))
                {
                    statusCode = CommandStatusCode.FAILURE;
                }
                statusMessages.add(s);
            }
        }
    }
}
