package com.lnjoying.justice.aos.helm.command.impl;


import com.lnjoying.justice.aos.helm.command.AbstractChartCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.result.ListCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/9 17:08
 */

public class ListCommand extends AbstractChartCommand<ListCommandResult<String>>
{
    public static final String COMMAND_LIST = "list";

    /**
     * show all releases without any filter applied"
     */
    private boolean all;

    /**
     * list releases across all namespaces
     */
    private boolean allNamespace;

    /**
     * show deployed releases. If no other is specified, this will be automatically enabled
     */
    private boolean deployed;

    /**
     * show failed releases
     */
    private boolean failed;

    /**
     * show pending releases
     */
    private boolean pending;

    /**
     * show superseded releases
     */
    private boolean superseded;

    /**
     * show uninstalled releases (if 'helm uninstall --keep-history' was used)
     */
    private boolean uninstalled;

    /**
     * show releases that are currently being uninstalled
     */
    private boolean uninstalling;

    /**
     * sort by release date
     */
    private boolean date;

    /**
     * output short (quiet) listing format
     */
    private boolean shortOutput;

    /**
     * a regular expression (Perl compatible). Any releases that match the expression will be included in the results
     */
    private String filter;

    private String output;

    @Override
    protected void moreFlags(List<String> flags)
    {

        super.moreFlags(flags);

        if (isAll())
        {
            flags.add("--all");
        }

        if (isAllNamespace())
        {
            flags.add("--all-namespaces");
        }

        if (isDeployed())
        {
            flags.add("--deployed");
        }

        if (isFailed())
        {
            flags.add("--failed");
        }

        if (isPending())
        {
            flags.add("--pending");
        }

        if (isSuperseded())
        {
            flags.add("--superseded");
        }

        if (isUninstalled())
        {
            flags.add("--uninstalled");
        }

        if (isUninstalling())
        {
            flags.add("--uninstalling");
        }

        if (isDate())
        {
            flags.add("--date");
        }

        if (isShortOutput())
        {
            flags.add("--short");
        }

        getFilter().ifPresent(filter -> {
            flags.add("--filter");
            flags.add(filter);
        });

        getOutput().ifPresent(output -> {
            flags.add("--output");
            flags.add(output);
        });
    }


    @Override
    public ListCommandResult<String> internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();

        //Consumer<String> loggingConsumer = s -> this.logger.info(s);
        ResultParser parsingConsumer = new ResultParser();
        //Consumer<String> compositeConsumer = parsingConsumer.andThen(parsingConsumer);

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_HELM);
        commands.add(COMMAND_LIST);
        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        ListCommandResult<String> result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }

    public boolean isAll()
    {
        return all;
    }

    public void setAll(boolean all)
    {
        this.all = all;
    }

    public boolean isAllNamespace()
    {
        return allNamespace;
    }

    public void setAllNamespace(boolean allNamespace)
    {
        this.allNamespace = allNamespace;
    }

    public boolean isDeployed()
    {
        return deployed;
    }

    public void setDeployed(boolean deployed)
    {
        this.deployed = deployed;
    }

    public boolean isFailed()
    {
        return failed;
    }

    public void setFailed(boolean failed)
    {
        this.failed = failed;
    }

    public boolean isPending()
    {
        return pending;
    }

    public void setPending(boolean pending)
    {
        this.pending = pending;
    }

    public boolean isSuperseded()
    {
        return superseded;
    }

    public void setSuperseded(boolean superseded)
    {
        this.superseded = superseded;
    }

    public boolean isUninstalled()
    {
        return uninstalled;
    }

    public void setUninstalled(boolean uninstalled)
    {
        this.uninstalled = uninstalled;
    }

    public boolean isUninstalling()
    {
        return uninstalling;
    }

    public void setUninstalling(boolean uninstalling)
    {
        this.uninstalling = uninstalling;
    }

    public boolean isDate()
    {
        return date;
    }

    public void setDate(boolean date)
    {
        this.date = date;
    }

    public boolean isShortOutput()
    {
        return shortOutput;
    }

    public void setShortOutput(boolean shortOutput)
    {
        this.shortOutput = shortOutput;
    }

    public Optional<String> getFilter()
    {
        return Optional.ofNullable(filter);
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    public Optional<String> getOutput()
    {
        return Optional.ofNullable(output);
    }

    public void setOutput(String output)
    {
        this.output = output;
    }

    private static final class ResultParser extends AbstractResultParser<ListCommandResult<String>>
            implements Consumer<String>
    {

        private List<String> data = new ArrayList<>();

        @Override
        public ListCommandResult<String> parse()
        {
            ListCommandResult<String> result = new ListCommandResult<String>();
            result.setStatusCode(statusCode);
            result.setStatusMessage(String.join(LINE_SEPARATOR, statusMessages));
            result.setData(String.join("", data));
            return result;
        }

        @Override
        public void accept(String s)
        {
            if (Objects.nonNull(s))
            {
                if (s.startsWith("Error:")) {
                    statusCode = CommandStatusCode.FAILURE;
                }
                else
                {
                    statusCode = CommandStatusCode.SUCCESS;
                }
                statusMessages.add(s);
                data.add(s);
            }
        }
    }

    public  enum OutputType
    {
        TABLE("table"),

        YAML("yaml"),

        JSON("json");
        private final String type;

        OutputType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }
    }

    public static class ListResult
    {
        public ListResult(){}
        private String name;

        private String namespace;

        private String revision;

        private String updated;

        private String status;

        private String chart;

        private String appVersion;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getNamespace()
        {
            return namespace;
        }

        public void setNamespace(String namespace)
        {
            this.namespace = namespace;
        }

        public String getRevision()
        {
            return revision;
        }

        public void setRevision(String revision)
        {
            this.revision = revision;
        }

        public String getUpdated()
        {
            return updated;
        }

        public void setUpdated(String updated)
        {
            this.updated = updated;
        }

        public String getStatus()
        {
            return status;
        }

        public void setStatus(String status)
        {
            this.status = status;
        }

        public String getChart()
        {
            return chart;
        }

        public void setChart(String chart)
        {
            this.chart = chart;
        }

        public String getAppVersion()
        {
            return appVersion;
        }

        public void setAppVersion(String appVersion)
        {
            this.appVersion = appVersion;
        }
    }
}
