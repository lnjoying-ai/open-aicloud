package com.lnjoying.justice.aos.helm.kubectl;

import com.lnjoying.justice.aos.helm.AbstractKubectlCommand;
import com.lnjoying.justice.aos.helm.command.AbstractCommand;
import com.lnjoying.justice.aos.helm.command.AbstractResultParser;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.impl.ListCommand;
import com.lnjoying.justice.aos.helm.command.result.DefaultCommandResult;
import com.lnjoying.justice.aos.helm.command.result.ListCommandResult;
import com.lnjoying.justice.aos.helm.inner.CommandLineExecutorRunner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * @Description: get pods with kubectl
 *     kubectl get pods -A  --selector 'app=seq, release=azure-seq'   -o template  --template  {{range .items}}{{.status.phase}}{{"\n"}}{{end}}
 *     kubectl get pods  --output jsonpath-as-json={.items..status.phase} -A  --selector 'app=seq, release=azure-seq'
 * @Author: Merak
 * @Date: 2022/9/1 19:58
 */

public class PodsGetCommand extends AbstractKubectlCommand<ListCommandResult>
{

    public static final String COMMAND_KUBECTL = "kubectl";

    public static final String COMMAND_GET = "get";

    public static final String COMMAND_PODS = "pods";

    private boolean allNamespaces;

    private Map<String, String> selector = new HashMap<>();

    private String output;

    @Override
    protected void moreFlags(List<String> flags)
    {
        getOutput().ifPresent(output -> {
            flags.add("--output");
            flags.add(output);
        });

        if (!CollectionUtils.isEmpty(selector))
        {
            flags.add("--selector");

            StringBuilder sb = new StringBuilder();
            selector.forEach((k, v) -> {
                sb.append(k + "=" + v + ",");
            });

            String selectorValue = StringUtils.removeEnd(sb.toString(), ",");
            flags.add(selectorValue);
        }

        if (isAllNamespaces())
        {
            flags.add("--all-namespaces");
        }
        super.moreFlags(flags);
    }

    @Override
    protected ListCommandResult internalCall() throws Exception
    {
        CommandLineExecutorRunner runner = new CommandLineExecutorRunner();

        //Consumer<String> loggingConsumer = s -> this.logger.info(s);
        ResultParser parsingConsumer = new ResultParser();
        //Consumer<String> compositeConsumer = parsingConsumer.andThen(parsingConsumer);

        List<String> commands = new ArrayList<>();
        commands.add(COMMAND_KUBECTL);
        commands.add(COMMAND_GET);
        commands.add(COMMAND_PODS);
        addAndCheckFlags(commands);

        runner.run(getCurrentDirectory(), parsingConsumer, commands.toArray(new String[0]));
        ListCommandResult<String> result = parsingConsumer.parse();
        record(commands, result);
        return result;
    }


    public static String setStatusJsonPath()
    {
        return "jsonpath-as-json={.items..status.phase}";
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
                if (s.startsWith("Error") || s.startsWith("error")) {
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

        JSON("json"),

        YAML("yaml"),

        GO_TEMPLATE("go-template"),

        GO_TEMPLATE_FILE("go-template-file"),

        TEMPLATE("template"),

        TEMPLATEFILE("templatefile"),

        JSONPATH("jsonpath"),

        JSONPATH_AS_JSON("jsonpath-as-json"),

        JSONPATH_FILE("jsonpath-file"),

        CUSTOM_COLUMNS("custom-columns"),

        CUSTOM_COLUMNS_FILE("custom-columns-file"),

        WIDE("WIDE");

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

    public enum PodStatus
    {

        /**
         * PodPending means the pod has been accepted by the system, but one or more of the containers
         *    has not been started. This includes time before being bound to a node, as well as time spent
         *    pulling images onto the host.
         */
        PodPending("Pending"),

        /**
         * PodRunning means the pod has been bound to a node and all of the containers have been started.
         *  At least one container is still running or is in the process of being restarted.
         */
        PodRunning("Running"),

        /**
         * PodSucceeded means that all containers in the pod have voluntarily terminated
         * with a container exit code of 0, and the system is not going to restart any of these containers.
         */
        PodSucceeded("Succeeded"),

        /**
         * PodFailed means that all containers in the pod have terminated, and at least one container has
         * terminated in a failure (exited with a non-zero exit code or was stopped by the system).
         */
        PodFailed("Failed"),

        /**
         * PodUnknown means that for some reason the state of the pod could not be obtained, typically due
         * to an error in communicating with the host of the pod.
         */
        PodUnknown("Unknown");

        private final String phase;

        PodStatus(String phase)
        {
            this.phase = phase;
        }

        public String getPhase()
        {
            return phase;
        }
    }

    public boolean isAllNamespaces()
    {
        return allNamespaces;
    }

    public void setAllNamespaces(boolean allNamespaces)
    {
        this.allNamespaces = allNamespaces;
    }


    public Map<String, String> getSelector()
    {
        return selector;
    }

    public void setSelector(Map<String, String> selector)
    {
        this.selector = selector;
    }

    public Optional<String> getOutput()
    {
        return Optional.ofNullable(output);
    }

    public void setOutput(String output)
    {
        this.output = output;
    }
}
