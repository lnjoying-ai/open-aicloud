package com.lnjoying.justice.aos.helm.command.impl;

import com.lnjoying.justice.aos.helm.command.AbstractChartCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.result.UninstallCommandResult;
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
 * @Date: 2022/8/3 20:45
 */

public class UninstallCommand extends AbstractChartCommand<UninstallCommandResult>
{
    public static final String COMMAND_UNINSTALL = "uninstall";

    private String releaseName;

    /**
     * remove all associated resources and mark the release as deleted,
     * but retain the release history")
     */
    private boolean keepHistory;

    public UninstallCommand()
    {
        super();
    }

    public UninstallCommand(Logger logger)
    {
        super(logger);
    }

    @Override
    protected void moreFlags(List<String> flags)
    {
        getReleaseName().ifPresent(releaseName -> {
            flags.add(releaseName);
        });
        super.moreFlags(flags);
    }

    @Override
    protected UninstallCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();

        ResultParser parsingConsumer = new ResultParser();

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_UNINSTALL);
        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        UninstallCommandResult result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }

    public Optional<String> getReleaseName()
    {
        return Optional.ofNullable(releaseName);
    }

    public void setReleaseName(String releaseName)
    {
        this.releaseName = releaseName;
    }

    public boolean isKeepHistory()
    {
        return keepHistory;
    }

    public void setKeepHistory(boolean keepHistory)
    {
        this.keepHistory = keepHistory;
    }

    private static final class ResultParser extends AbstractResultParser<UninstallCommandResult>
            implements Consumer<String>
    {

        @Override
        public UninstallCommandResult parse()
        {
            UninstallCommandResult result = new UninstallCommandResult();
            result.setStatusCode(statusCode);
            result.setStatusMessage(String.join(LINE_SEPARATOR, statusMessages));
            return result;
        }

        @Override
        public void accept(String s)
        {
            if (Objects.nonNull(s))
            {
                if (s.startsWith("release") && s.contains("uninstalled")) {
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
