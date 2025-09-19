package com.lnjoying.justice.aos.helm.command.impl;

import com.lnjoying.justice.aos.helm.command.AbstractChartPathCommand;
import com.lnjoying.justice.aos.helm.command.parser.DefaultResultParser;
import com.lnjoying.justice.aos.helm.command.result.DefaultCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lnjoying.justice.aos.helm.util.HelmTools.convertToOciUrl;

/**
 * @Description: Retrieve a package from a package repository, and download it locally.
 * helm pull [chart URL | repo/chartname] [...] [flags]
 * helm pull oci://xx/helm/network-operator --version 23.4.0
 * @Author: Merak
 * @Date: 2022/8/17 10:24
 */

public class PullCommand extends AbstractChartPathCommand<DefaultCommandResult>
{

    public static final String COMMAND_PULL = "pull";

    /**
     * like ali/mysql
     */
    private  String repoChartName;

    private String chartUrl;

    /**
     * use development versions, too. Equivalent to version '>0.0.0-0'. If --version is set, this is ignored.
     */
    private boolean devel;

    /**
     * if set to true, will untar the chart after downloading it
     */
    private boolean unTar;

    /**
     * fetch the provenance file, but don't perform verification
     */
    private boolean prov;

    /**
     * if untar is specified, this flag specifies the name of the directory into which the chart is expanded
     */
    private String unTarDir;

    /**
     * location to write the chart. If this and untardir are specified, untardir is appended to this
     */
    private String destination;

    @Override
    protected void moreFlags(List<String> flags)
    {
        if (!getChartUrl().isPresent())
        {
            getRepoChartName().ifPresent(repoChartName -> {
                flags.add(repoChartName);
            });
        }

        getChartUrl().ifPresent(chartUrl -> {
            flags.add(convertToOciUrl(chartUrl) );
        });

        if (isDevel())
        {
            flags.add("--devel");
        }

        if (isUnTar())
        {
            flags.add("--untar");
        }

        if (isProv())
        {
            flags.add("--prov");
        }

        getUnTarDir().ifPresent(unTarDir -> {
            flags.add("--untardir");
            flags.add(unTarDir);
        });

        getDestination().ifPresent(destination -> {
            flags.add("--destination");
            flags.add(destination);
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
        commands.add(COMMAND_PULL);
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

    @Override
    public boolean isDevel()
    {
        return devel;
    }

    @Override
    public void setDevel(boolean devel)
    {
        this.devel = devel;
    }

    public boolean isUnTar()
    {
        return unTar;
    }

    public void setUnTar(boolean unTar)
    {
        this.unTar = unTar;
    }

    public boolean isProv()
    {
        return prov;
    }

    public void setProv(boolean prov)
    {
        this.prov = prov;
    }

    public Optional<String> getUnTarDir()
    {
        return Optional.ofNullable(unTarDir);
    }

    public void setUnTarDir(String unTarDir)
    {
        this.unTarDir = unTarDir;
    }

    public Optional<String> getDestination()
    {
        return Optional.ofNullable(destination);
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }


    public Optional<String> getChartUrl()
    {
        return Optional.ofNullable(chartUrl);
    }

    public void setChartUrl(String chartUrl)
    {
        this.chartUrl = chartUrl;
    }
}
