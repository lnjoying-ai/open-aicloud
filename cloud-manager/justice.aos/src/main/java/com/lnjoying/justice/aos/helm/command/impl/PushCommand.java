package com.lnjoying.justice.aos.helm.command.impl;

import com.lnjoying.justice.aos.helm.command.AbstractChartPathCommand;
import com.lnjoying.justice.aos.helm.command.AbstractPushCommand;
import com.lnjoying.justice.aos.helm.command.parser.DefaultResultParser;
import com.lnjoying.justice.aos.helm.command.result.DefaultCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lnjoying.justice.aos.helm.util.HelmTools.convertToOciUrl;

/**
 * @Description: Retrieve a package from a package repository, and download it locally.
 * helm push [chart] [remote] [flags]
 * eg: helm push   network-operator-23.4.0.tgz  oci://192.168.0.1/helm --insecure-skip-tls-verify
 * @Author: Merak
 * @Date: 2024/6/7 10:24
 */

public class PushCommand extends AbstractPushCommand<DefaultCommandResult>
{

    public static final String COMMAND_PUSH = "push";

    private String packagePath;
    /**
     * like ali/mysql
     */
    private  String repoChartName;

    private String remote;


    @Override
    protected void moreFlags(List<String> flags)
    {

        getPackagePath().ifPresent(packageName -> {
            flags.add(packageName);
        });

//        getRepoChartName().ifPresent(repoChartName -> {
//            flags.add(repoChartName);
//        });

        getRemote().ifPresent(remote -> {
            flags.add(convertToOciUrl(remote));
        });


        super.moreFlags(flags);

    }

    @Override
    protected DefaultCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();
        DefaultResultParser parsingConsumer = new DefaultResultParser();

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_PUSH);
        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        DefaultCommandResult result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }



    public Optional<String> getRepoChartName()
    {
        return Optional.ofNullable(repoChartName);
    }

    public void setRepoChartName(String repoChartName)
    {
        this.repoChartName = repoChartName;
    }


    public Optional<String> getRemote()
    {
        return Optional.ofNullable(remote);
    }

    public void setRemote(String remote)
    {
        this.remote = remote;
    }

    public Optional<String> getPackagePath()
    {
        return Optional.ofNullable(packagePath);
    }

    public void setPackagePath(String packagePath)
    {
        this.packagePath = packagePath;
    }

}
