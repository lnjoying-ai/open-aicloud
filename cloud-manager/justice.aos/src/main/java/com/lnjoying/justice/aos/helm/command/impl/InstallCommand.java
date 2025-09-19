package com.lnjoying.justice.aos.helm.command.impl;

import com.lnjoying.justice.aos.helm.command.AbstractChartCommand;
import com.lnjoying.justice.aos.helm.command.AbstractChartPathCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.result.InstallCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Description: This command installs a chart archive.
 * helm install [NAME] [CHART] [flags]
 * @Author: Merak
 * @Date: 2022/7/30 16:25
 */

public class InstallCommand extends AbstractChartPathCommand<InstallCommandResult>
{
    public static final String COMMAND_INSTALL = "install";
    private String releaseName;

    private String releaseNameTemplate;

    private String values;

    public InstallCommand()
    {
        super();
    }

    public InstallCommand(Logger logger)
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

    public Optional<String> getReleaseNameTemplate()
    {
        return Optional.ofNullable(releaseNameTemplate);
    }

    public void setReleaseNameTemplate(String releaseNameTemplate)
    {
        this.releaseNameTemplate = releaseNameTemplate;
    }

    @Override
    public InstallCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();

        //Consumer<String> loggingConsumer = s -> this.logger.info(s);
        ResultParser parsingConsumer = new ResultParser();
        //Consumer<String> compositeConsumer = parsingConsumer.andThen(parsingConsumer);

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_INSTALL);
        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        InstallCommandResult result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }

    @Override
    protected void moreFlags(List<String> flags)
    {
        getReleaseName().ifPresent(releaseName -> {
            flags.add(releaseName);
        });
        flags.add(getChartDirectory());

        super.moreFlags(flags);

        getReleaseNameTemplate().ifPresent(releaseName -> {
           flags.add("--name-template");
           flags.add(releaseName);
        });

        getValues().ifPresent(values -> {
            flags.add("--values");
            flags.add(values);
        });
    }

    private static final class ResultParser extends AbstractResultParser<InstallCommandResult>
            implements Consumer<String>
    {
        @Override
        public InstallCommandResult parse()
        {
            InstallCommandResult result = new InstallCommandResult();
            result.setStatusCode(statusCode);
            result.setStatusMessage(String.join(LINE_SEPARATOR, statusMessages));
            return result;
        }

        @Override
        public void accept(String s)
        {
            if (Objects.nonNull(s))
            {
                if (s.startsWith("STATUS: deployed")) {
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

    public Optional<String> getValues()
    {
        return Optional.ofNullable(values);
    }

    public void setValues(String values)
    {
        this.values = values;
    }
}
