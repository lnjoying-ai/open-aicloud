package com.lnjoying.justice.aos.helm.command;

import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/17 10:39
 */

public abstract class AbstractChartPathCommand<V> extends AbstractChartCommand<V>
{
    /**
     * specify a version constraint for the chart version to use.
     * This constraint can be a specific tag (e.g. 1.1.1) or it may reference a valid range (e.g. ^2.0.0).
     * If this is not specified, the latest version is used"
     */
    private String version;

    /**
     * verify the package before using it
     */
    private boolean verify;

    /**
     * location of public keys used for verification
     */
    private String keyring;

    /**
     * chart repository url where to locate the requested chart
     */
    private String repo;

    /**
     * chart repository username where to locate the requested chart
     */
    private String username;

    /**
     * chart repository password where to locate the requested chart
     */
    private String password;

    /**
     * identify HTTPS client using this SSL certificate file
     */
    private String certFile;

    /**
     * identify HTTPS client using this SSL key file
     */
    private String keyFile;

    /**
     * skip tls certificate checks for the chart download
     */
    private boolean insecureSkipTlsVerify;

    /**
     * verify certificates of HTTPS-enabled servers using this CA bundle
     */
    private String caFile;

    /**
     * pass credentials to all domains
     */
    private boolean passCredentials;

    public AbstractChartPathCommand()
    {
        super();
    }

    public AbstractChartPathCommand(Logger logger)
    {
        super(logger);
    }

    @Override
    protected void moreFlags(List<String> flags)
    {
        super.moreFlags(flags);

        getVersion().ifPresent(version -> {
            flags.add("--version");
            flags.add(version);
        });

        if (isVerify())
        {
            flags.add("--verify");
        }

        getKeyring().ifPresent(keyring -> {
            flags.add("--keyring");
            flags.add(keyring);
        });

        getRepo().ifPresent(repo -> {
            flags.add("--repo");
            flags.add(repo);
        });

        getUsername().ifPresent(username -> {
            flags.add("--username");
            flags.add(username);
        });

        getPassword().ifPresent(password -> {
            flags.add("--password");
            flags.add(password);
        });

        getCertFile().ifPresent(certFile -> {
            flags.add("--cert-file");
            flags.add(certFile);
        });

        getKeyFile().ifPresent(keyFile -> {
            flags.add("--key-file");
            flags.add(keyFile);
        });

        if (isInsecureSkipTlsVerify())
        {
            flags.add("--insecure-skip-tls-verify");
        }

        getCaFile().ifPresent(caFile -> {
            flags.add("--ca-file");
            flags.add(keyFile);
        });

        if (isPassCredentials())
        {
            flags.add("--pass-credentials");
        }
    }

    public Optional<String> getVersion()
    {
        return Optional.ofNullable(version);
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public boolean isVerify()
    {
        return verify;
    }

    public void setVerify(boolean verify)
    {
        this.verify = verify;
    }

    public Optional<String> getKeyring()
    {
        return Optional.ofNullable(keyring);
    }

    public void setKeyring(String keyring)
    {
        this.keyring = keyring;
    }

    public Optional<String> getRepo()
    {
        return Optional.ofNullable(repo);
    }

    public void setRepo(String repo)
    {
        this.repo = repo;
    }

    public Optional<String> getUsername()
    {
        return Optional.ofNullable(username);
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Optional<String> getPassword()
    {
        return Optional.ofNullable(password);
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Optional<String> getCertFile()
    {
        return Optional.ofNullable(certFile);
    }

    public void setCertFile(String certFile)
    {
        this.certFile = certFile;
    }

    public boolean isInsecureSkipTlsVerify()
    {
        return insecureSkipTlsVerify;
    }

    public void setInsecureSkipTlsVerify(boolean insecureSkipTlsVerify)
    {
        this.insecureSkipTlsVerify = insecureSkipTlsVerify;
    }

    public Optional<String> getCaFile()
    {
        return Optional.ofNullable(caFile);
    }

    public void setCaFile(String caFile)
    {
        this.caFile = caFile;
    }

    public boolean isPassCredentials()
    {
        return passCredentials;
    }

    public void setPassCredentials(boolean passCredentials)
    {
        this.passCredentials = passCredentials;
    }

    public Optional<String> getKeyFile()
    {
        return Optional.ofNullable(keyFile);
    }

    public void setKeyFile(String keyFile)
    {
        this.keyFile = keyFile;
    }
}
