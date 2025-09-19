package com.lnjoying.justice.aos.helm.command.impl.plugin;

import com.lnjoying.justice.aos.helm.command.AbstractPluginCommand;
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
 * @Description: Helm plugin to push chart package to ChartMuseum
 *  helm cm-push mychart-0.1.0.tgz chartmuseum
 * @Author: Merak
 * @Date: 2022/8/23 14:08
 */

public class CmPushCommand extends AbstractPluginCommand<DefaultCommandResult>
{
    public static final String COMMAND_CM_PUSH = "cm-push";

    private String packagePath;

    private String repoName;

    /**
     * Override chart version pre-push
     */
    private String version;

    /**
     * Connect to server with an insecure way by skipping certificate verification [$HELM_REPO_INSECURE]
     */
    private boolean insecure;

    /**
     * Override app version pre-push
     */
    private String appVersion;

    @Override
    protected void moreFlags(List<String> flags)
    {
        getPackagePath().ifPresent(packageName -> {
            flags.add(packageName);
        });

        getRepoName().ifPresent(repoName -> {
            flags.add(repoName);
        });

        getVersion().ifPresent(version -> {
            flags.add("--version");
            flags.add(version);
        });

        getAppVersion().ifPresent(appVersion -> {
            flags.add("--app-version");
            flags.add(appVersion);
        });

        if (isInsecure())
        {
            flags.add("--insecure");
        }
        super.moreFlags(flags);
    }

    @Override
    protected DefaultCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();
        ResultParser parsingConsumer = new ResultParser();

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_CM_PUSH);
        addAndCheckFlags(commands);
        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        DefaultCommandResult result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }

    public Optional<String> getPackagePath()
    {
        return Optional.ofNullable(packagePath);
    }

    public void setPackagePath(String packagePath)
    {
        this.packagePath = packagePath;
    }

    public Optional<String> getVersion()
    {
        return Optional.ofNullable(version);
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public boolean isInsecure()
    {
        return insecure;
    }

    public void setInsecure(boolean insecure)
    {
        this.insecure = insecure;
    }

    public Optional<String> getAppVersion()
    {
        return Optional.ofNullable(appVersion);
    }

    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }

    public Optional<String> getRepoName()
    {
        return Optional.ofNullable(repoName);
    }

    public void setRepoName(String repoName)
    {
        this.repoName = repoName;
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
                if (s.contains("Done")) {
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
