package com.lnjoying.justice.aos.helm.command.impl.repo;

import com.lnjoying.justice.aos.helm.command.AbstractRepoCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.result.DefaultCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Description:
 * <pre>helm repo add stable  http://mirror.azure.cn/kubernetes/charts</pre>
 * @Author: Merak
 * @Date: 2022/8/5 17:07
 */

public class RepoAddCommand extends AbstractRepoCommand<DefaultCommandResult>
{
    public static final String COMMAND_ADD = "add";

    private String repoName;

    private String repoUrl;

    public RepoAddCommand()
    {
        super();
    }

    public RepoAddCommand(Logger logger)
    {
        super(logger);
    }

    @Override
    protected void moreFlags(List<String> flags)
    {
        getRepoName().ifPresent(repoName -> {
            flags.add(repoName);
        });

        getRepoUrl().ifPresent(repoUrl -> {
            flags.add(repoUrl);
        });

        super.moreFlags(flags);
    }

    @Override
    protected DefaultCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();
        ResultParser parsingConsumer = new ResultParser();

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_REPO);
        commands.add(COMMAND_ADD);
        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        DefaultCommandResult result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }

    public Optional<String> getRepoName()
    {
        return Optional.ofNullable(repoName);
    }

    public void setRepoName(String repoName)
    {
        this.repoName = repoName;
    }

    public Optional<String> getRepoUrl()
    {
        return Optional.ofNullable(repoUrl);
    }

    public void setRepoUrl(String repoUrl)
    {
        this.repoUrl = repoUrl;
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
                if (s.contains("already exists") || s.contains("has been added")) {
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
