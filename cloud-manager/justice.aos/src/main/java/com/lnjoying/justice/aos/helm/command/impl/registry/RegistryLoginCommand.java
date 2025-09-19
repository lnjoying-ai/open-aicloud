package com.lnjoying.justice.aos.helm.command.impl.registry;

import com.lnjoying.justice.aos.helm.command.AbstractRegistryCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.impl.repo.RepoAddCommand;
import com.lnjoying.justice.aos.helm.command.result.DefaultCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/6/7 15:10
 */

public class RegistryLoginCommand extends AbstractRegistryCommand<DefaultCommandResult>
{
    public static final String COMMAND_LOGIN = "login";

    private String host;

    @Override
    protected void moreFlags(List<String> flags)
    {
        getHost().ifPresent(host -> {
            flags.add(extractHost(host));
        });

        super.moreFlags(flags);
    }


    @Override
    protected DefaultCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();
        RegistryLoginCommand.ResultParser parsingConsumer = new RegistryLoginCommand.ResultParser();

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_REGISTRY);
        commands.add(COMMAND_LOGIN);
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
                if (s.contains("Succeeded") || s.contains("Login Succeeded")) {
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


    public Optional<String> getHost()
    {
        return Optional.ofNullable(host);
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public static String extractHost(String url) {
        String regex = "([\\d\\.]+(:\\d+)?)/";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return url;
        }
    }

}
