package com.lnjoying.justice.aos.helm.command;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

import static com.lnjoying.justice.aos.helm.util.AnonymizeUtils.anonymizePassword;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/29 21:06
 */

public abstract class AbstractCommand<V> implements Callable<V>
{
    public static final String COMMAND_HELM = "helm";
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * enable verbose output
     */
    private boolean debug;

    /**
     * the address and the port for the Kubernetes API server
     */
    private String kubeApiServer;

    /**
     * group to impersonate for the operation, this flag can be repeated to specify multiple groups.
     */
    private String[] kubeAsGroup;

    /**
     * username to impersonate for the operation
     */
    private String kubeAsUser;

    /**
     * the certificate authority file for the Kubernetes API server connection
     */
    private String kubeCaFile;

    /**
     * name of the kubeconfig context to use
     */
    private String kubeContext;

    /**
     *  bearer token used for authentication
     */
    private String kubeToken;

    /**
     * path to the kubeconfig file
     */
    private String kubeConfig;

    /**
     * namespace scope for this request
     */
    private String namespace;

    /**
     * path to the registry config file
     * helm\\registry\\config.json")
     */
    private String registryConfig;

    /**
     * path to the file containing cached repository indexes (default "helm\\repository")
     */
    private String repositoryCache;

    /**
     * path to the file containing repository names and URLs (default "C:\\Users\\ThinkPad\\AppData\\Roaming\\helm\\repositories.yaml")
     */
    private String repositoryConfig;

    private File currentDirectory;

    public AbstractCommand()
    {

    }

    public AbstractCommand(Logger logger)
    {
      this.logger = logger;
    }

    public boolean isDebug()
    {
        return this.debug;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    public Optional<String> getKubeContext()
    {
        return Optional.ofNullable(this.kubeContext);
    }

    public void setKubeContext(String kubeContext)
    {
        Objects.requireNonNull(kubeContext, "kubeContext must not be null");
        this.kubeContext = kubeContext;
    }

    public Optional<String> getKubeConfig()
    {
        return Optional.ofNullable(this.kubeConfig);
    }

    public void setKubeConfig(String kubeConfig)
    {
        Objects.requireNonNull(kubeConfig, "kubeConfig must not be null");
        this.kubeConfig = kubeConfig;
    }

    public Optional<String> getNamespace()
    {
        return Optional.ofNullable(namespace);
    }

    public void setNamespace(String namespace)
    {
        if (StringUtils.isBlank(namespace))
        {
            this.namespace = "default";
        }
        else
        {
            this.namespace = namespace;
        }
    }

    public final File getCurrentDirectory() {
        if (this.currentDirectory == null) {
            try {
                this.currentDirectory = new File(".").getCanonicalFile();
            } catch (IOException ex) {
                throw new UncheckedIOException("Failed to retrieve current directory!", ex);
            }
        }
        return this.currentDirectory;
    }

    public final void setCurrentDirectory(File currentDirectory) {
        Objects.requireNonNull(currentDirectory, "currentDirectory must not be null");
        this.currentDirectory = currentDirectory;
    }

    @Override
    public V call() throws Exception
    {
        return internalCall();
    }

    protected abstract V internalCall() throws Exception;

    protected void moreFlags(List<String> flags)
    {
        // set global flags
        if (isDebug())
        {
            flags.add("--debug");
        }

        getKubeContext().ifPresent(context -> {
            flags.add("--kube-context");
            flags.add(context);
        });

        getKubeConfig().ifPresent(config -> {
            flags.add("--kubeconfig");
            flags.add(config);
        });

        getNamespace().ifPresent(namespace -> {
            flags.add("--namespace");
            flags.add(namespace);
        });
    }

    protected void record(List<String> commands, AbstractCommandResult result)
    {
        anonymizePassword(commands);
        logger.info("command: {}, result:{}", String.join(" ", commands), result);
    }

    protected  void addAndCheckFlags(List<String> flags)
    {
        moreFlags(flags);
        validate(flags);
    }

    protected void validate(List<String> flags)
    {
        flags.removeIf(Objects::isNull);
    }


}
