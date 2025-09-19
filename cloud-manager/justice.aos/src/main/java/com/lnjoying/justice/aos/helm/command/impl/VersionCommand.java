package com.lnjoying.justice.aos.helm.command.impl;

import com.lnjoying.justice.aos.helm.command.AbstractDefaultCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.result.DefaultCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/4 10:36
 */

public class VersionCommand extends AbstractDefaultCommand<DefaultCommandResult>
{
    public static final String COMMAND_VERSION = "version";

    /**
     * print the version number
     */
    private boolean shortVersion;

    /**
     * template for version string format
     */
    private String template;

    @Override
    protected void moreFlags(List<String> flags)
    {
        super.moreFlags(flags);
        getTemplate().ifPresent(template -> {
            flags.add("--template");
            flags.add("Version: {{.Version}}");
        });
    }

    @Override
    protected DefaultCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();

        //Consumer<String> loggingConsumer = s -> this.logger.info(s);
        ResultParser parsingConsumer = new ResultParser();
        //Consumer<String> compositeConsumer = parsingConsumer.andThen(parsingConsumer);

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_VERSION);

        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        DefaultCommandResult result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }

    private static final class ResultParser extends AbstractResultParser<DefaultCommandResult>
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
            if (Objects.nonNull(s))
            {
                if (s.startsWith("Version")) {
                    statusCode = CommandStatusCode.SUCCESS;
                }
                else
                {
                    statusCode = CommandStatusCode.FAILURE;
                }
                statusMessages.add(s);
            }
        }
    }

    public Optional<String> getTemplate()
    {
        return Optional.ofNullable(template);
    }

    public void setTemplate(String template)
    {
        Objects.requireNonNull(template, "template must not be null");
        this.template = template;
    }

    public boolean isShortVersion()
    {
        return shortVersion;
    }

    public void setShortVersion(boolean shortVersion)
    {
        this.shortVersion = shortVersion;
    }
}
