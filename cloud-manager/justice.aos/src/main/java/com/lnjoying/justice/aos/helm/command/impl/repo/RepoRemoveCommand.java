package com.lnjoying.justice.aos.helm.command.impl.repo;

import com.lnjoying.justice.aos.helm.command.AbstractRepoCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.result.DefaultCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @Description:
 * <pre>
 *   helm repo remove [REPO1 [REPO2 ...]] [flags]
 * </pre>
 * @Author: Merak
 * @Date: 2022/8/5 20:11
 */

public class RepoRemoveCommand extends AbstractRepoCommand<DefaultCommandResult>
{
    public static final String COMMAND_REMOVE = "remove";

    private List<String> repoNames;

    public RepoRemoveCommand()
    {
        super();
    }

    public RepoRemoveCommand(Logger logger)
    {
        super(logger);
    }

    @Override
    protected void moreFlags(List<String> flags)
    {
        getRepoNames().forEach(repoName -> {
            flags.add(repoName);
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
        commands.add(COMMAND_REMOVE);
        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        DefaultCommandResult result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }

    public List<String> getRepoNames()
    {
        return Objects.isNull(repoNames) ? Collections.emptyList() : repoNames;
    }

    public void setRepoNames(List<String> repoNames)
    {
        Objects.requireNonNull(repoNames, "repo names at least one");
        this.repoNames = repoNames;
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
                if (s.contains(" has been removed")) {
                    statusCode = CommandStatusCode.SUCCESS;
                }
                else if (s.startsWith("Error:"))
                {
                    if (s.contains("no repo"))
                    {
                        statusCode = CommandStatusCode.SUCCESS;
                    }
                    else
                    {
                        statusCode = CommandStatusCode.FAILURE;
                    }
                }
                statusMessages.add(s);
            }
        }
    }
}
