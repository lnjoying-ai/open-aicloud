package com.lnjoying.justice.aos.helm.command;

import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/30 15:07
 */

public abstract class AbstractChartCommand<V> extends AbstractCommand<V>
{

    private String chartDirectory;

    /**
     * if set, the installation process deletes the installation on failure.
     * The --wait flag will be set automatically if --atomic is used
     */
    private boolean atomic;

    /**
     * simulate an install
     */
    private boolean dryRun;

    /**
     * prevent hooks from running during install
     */
    private boolean noHooks;

    /**
     * time to wait for any individual Kubernetes operation (like Jobs for hooks) (default 5m0s)
     */
    private String timeout;

    /**
     * use development versions, too. Equivalent to version '>0.0.0-0'. If --version is set, this is ignored
     */
    private boolean devel;

    public AbstractChartCommand()
    {
        super();
    }

    public AbstractChartCommand(Logger logger)
    {
        super(logger);
    }


    public boolean isAtomic()
    {
        return atomic;
    }

    public void setAtomic(boolean atomic)
    {
        this.atomic = atomic;
    }

    public boolean isDryRun()
    {
        return dryRun;
    }

    public void setDryRun(boolean dryRun)
    {
        this.dryRun = dryRun;
    }

    public boolean isNoHooks()
    {
        return noHooks;
    }

    public void setNoHooks(boolean noHooks)
    {
        this.noHooks = noHooks;
    }

    public Optional<String> getTimeout()
    {
        return Optional.ofNullable(timeout);
    }

    public void setTimeout(String timeout)
    {
        this.timeout = timeout;
    }

    public boolean isDevel()
    {
        return devel;
    }

    public void setDevel(boolean devel)
    {
        this.devel = devel;
    }

    public String getChartDirectory()
    {
        return chartDirectory;
    }

    public void setChartDirectory(String chartDirectory)
    {
        Objects.requireNonNull(chartDirectory, "chartDirectory must not be null");
        this.chartDirectory = chartDirectory;
    }

    @Override
    protected void moreFlags(List<String> flags)
    {
        super.moreFlags(flags);

        if (isAtomic())
        {
            flags.add("--atomic");
        }

        if (isDevel())
        {
            flags.add("--devel");
        }

        if (isDryRun())
        {
            flags.add("--dry-run");
        }

        if (isNoHooks())
        {
            flags.add("--no-hooks");
        }

        getTimeout().ifPresent(timeout ->
        {
            flags.add("--timeout");
            flags.add(timeout);
        });
    }
}

