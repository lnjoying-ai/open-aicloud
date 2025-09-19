package com.lnjoying.justice.aos.helm.command.impl;

import com.lnjoying.justice.aos.helm.command.AbstractChartCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.result.UpgradeCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/3 17:15
 */

public class UpgradeCommand extends AbstractChartCommand<UpgradeCommandResult>
{
    public static final String COMMAND_UPGRADE = "upgrade";

    /**
     * force resource updates through a replacement strategy
     */
    private boolean force;

    /**
     * if a release by this name doesn't already exist, run an installation
     */
    private boolean install;

    /**
     * allow deletion of new resources created in this upgrade when upgrade fails
     */
    private boolean cleanupOnFail;

    private String releaseName;

    /**
     * when upgrading, reset the values to the ones built into the chart
     */
    private boolean resetValues;

    /**
     * when upgrading, reuse the last release's values and merge in any overrides from the command line via --set and -f.
     * If '--reset-values' is specified, this is ignored
     */
    private boolean reuseValues;

    private Map<String, String> values = new LinkedHashMap<>();

    @Override
    protected void moreFlags(List<String> flags)
    {
        flags.add(getReleaseName());
        flags.add(getChartDirectory());

        super.moreFlags(flags);
        if (isForce())
        {
            flags.add("--force");
        }

        if (isInstall())
        {
            flags.add("--install");
        }

        if (isCleanupOnFail())
        {
            flags.add("--cleanup-on-fail");
        }

        if (isResetValues()) {
            flags.add("--reset-values");
        }
        if (isReuseValues()) {
            flags.add("--reuse-values");
        }
        if (!values.isEmpty()) {
            flags.add("--set");
            flags.add(String.join(",", values.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.toList())));
        }
    }

    @Override
    protected UpgradeCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();

        ResultParser parsingConsumer = new ResultParser();

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_UPGRADE);
        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        UpgradeCommandResult result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }


    public boolean isForce()
    {
        return force;
    }

    public void setForce(boolean force)
    {
        this.force = force;
    }

    public boolean isInstall()
    {
        return install;
    }

    public void setInstall(boolean install)
    {
        this.install = install;
    }

    public boolean isCleanupOnFail()
    {
        return cleanupOnFail;
    }

    public void setCleanupOnFail(boolean cleanupOnFail)
    {
        this.cleanupOnFail = cleanupOnFail;
    }

    public String getReleaseName()
    {
        return releaseName;
    }

    public void setReleaseName(String releaseName)
    {
        this.releaseName = releaseName;
    }

    public boolean isResetValues()
    {
        return resetValues;
    }

    public void setResetValues(boolean resetValues)
    {
        this.resetValues = resetValues;
    }

    public boolean isReuseValues()
    {
        return reuseValues;
    }

    public void setReuseValues(boolean reuseValues)
    {
        this.reuseValues = reuseValues;
    }

    private static final class ResultParser extends AbstractResultParser<UpgradeCommandResult>
    implements Consumer<String>
    {
        @Override
        public UpgradeCommandResult parse()
        {
            UpgradeCommandResult result = new UpgradeCommandResult();
            result.setStatusCode(statusCode);
            result.setStatusMessage(String.join(LINE_SEPARATOR, statusMessages));
            return result;
        }

        @Override
        public void accept(String s)
        {
            if (Objects.nonNull(s))
            {
                if (s.startsWith("Release") && s.contains("has been upgraded")) {
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
