package com.lnjoying.justice.aos.helm.command;

import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/5 17:10
 */

public abstract class AbstractRegistryCommand<V> extends AbstractCommand<V>
{

    public static final String COMMAND_REGISTRY = "registry";

    /**
     * by default, this command will not allow adding official repos that have been permanently deleted.
     * This disables that behavior
     */
    private boolean allowDeprecatedRepos;

    /**
     * verify certificates of HTTPS-enabled servers using this CA bundle
     */
    private String caFile;

    /**
     * identify HTTPS client using this SSL certificate file
     */
    private String certFile;

    /**
     * replace (overwrite) the repo if it already exists
     */
    private boolean forceUpdate;

    /**
     * skip tls certificate checks for the repository
     */
    private boolean insecureSkipTlsVerify;

    /**
     *  identify HTTPS client using this SSL key file
     */
    private String keyFile;

    /**
     * Ignored. Formerly, it would disabled forced updates. It is deprecated by force-update.
     */
    private boolean noUpdate;

    /**
     * pass credentials to all domains
     */
    private boolean passCredentials;

    /**
     * chart repository password
     */
    private String password;

    /**
     * read chart repository password from stdin
     */
    private String passwordStdin;

    private String username;

    private boolean insecure;

    public AbstractRegistryCommand()
    {
        super();
    }

    public AbstractRegistryCommand(Logger logger)
    {
        super(logger);
    }

    @Override
    protected void moreFlags(List<String> flags)
    {
        super.moreFlags(flags);

        if(isForceUpdate())
        {
            flags.add("--force-update");
        }

        if (isInsecureSkipTlsVerify())
        {
            flags.add("--insecure-skip-tls-verify");
        }

        if (isNoUpdate())
        {
            flags.add("--no-update");
        }

        getCaFile().ifPresent(cafFile -> {
            flags.add("--ca-file");
            flags.add(cafFile);
        });

        getCertFile().ifPresent(cafFile -> {
            flags.add("--cert-file");
            flags.add(certFile);
        });

        getKeyFile().ifPresent(keyFile -> {
            flags.add("--key-file");
            flags.add(keyFile);
        });

        getPassword().ifPresent(password -> {
            flags.add("--password");
            flags.add(password);
        });

        getUsername().ifPresent(username -> {
            flags.add("--username");
            flags.add(username);
        });

        if (isInsecure())
        {
            flags.add("--insecure");
        }
    }

    public boolean isAllowDeprecatedRepos()
    {
        return allowDeprecatedRepos;
    }

    public void setAllowDeprecatedRepos(boolean allowDeprecatedRepos)
    {
        this.allowDeprecatedRepos = allowDeprecatedRepos;
    }

    public Optional<String> getCaFile()
    {
        return Optional.ofNullable(caFile);
    }

    public void setCaFile(String caFile)
    {
        this.caFile = caFile;
    }

    public Optional<String> getCertFile()
    {
        return Optional.ofNullable(certFile);
    }

    public void setCertFile(String certFile)
    {
        this.certFile = certFile;
    }

    public boolean isForceUpdate()
    {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate)
    {
        this.forceUpdate = forceUpdate;
    }

    public boolean isInsecureSkipTlsVerify()
    {
        return insecureSkipTlsVerify;
    }

    public void setInsecureSkipTlsVerify(boolean insecureSkipTlsVerify)
    {
        this.insecureSkipTlsVerify = insecureSkipTlsVerify;
    }

    public Optional<String> getKeyFile()
    {
        return Optional.ofNullable(keyFile);
    }

    public void setKeyFile(String keyFile)
    {
        this.keyFile = keyFile;
    }

    public boolean isNoUpdate()
    {
        return noUpdate;
    }

    public void setNoUpdate(boolean noUpdate)
    {
        this.noUpdate = noUpdate;
    }

    public boolean isPassCredentials()
    {
        return passCredentials;
    }

    public void setPassCredentials(boolean passCredentials)
    {
        this.passCredentials = passCredentials;
    }

    public Optional<String> getPassword()
    {
        return Optional.ofNullable(password);
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Optional<String> getPasswordStdin()
    {
        return Optional.ofNullable(passwordStdin);
    }

    public void setPasswordStdin(String passwordStdin)
    {
        this.passwordStdin = passwordStdin;
    }

    public Optional<String> getUsername()
    {
        return Optional.ofNullable(username);
    }

    public void setUsername(String username)
    {
        this.username = username;
    }


    public boolean isInsecure()
    {
        return insecure;
    }


    public void setInsecure(boolean insecure)
    {
        this.insecure = insecure;
    }

}
