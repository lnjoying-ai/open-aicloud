package com.lnjoying.justice.omc.prometheus.config;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/11 10:09
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "url")
public class RemoteReadConfig {
    private String url;
    private String remoteTimeout;
    private Map<String, String> headers;
    private boolean readRecent;
    private String name;
    private BasicAuth basicAuth;
    private Authorization authorization;
    private OAuth2 oAuth2;
    private String bearerToken;
    private String bearerTokenFile;
    private TLSConfig tlsConfig;
    private boolean followRedirects;
    private boolean enableHTTP2;
    private Map<String, String> requiredMatchers;
    private boolean filterExternalLabels;

    public static RemoteReadConfig newRemoteReadConfig(String url, String username, String password)
    {
        RemoteReadConfig remoteReadConfig = new RemoteReadConfig();
        RemoteReadConfig.BasicAuth basicAuth = new RemoteReadConfig.BasicAuth();
        basicAuth.setUsername("a");
        basicAuth.setPassword("b");
        remoteReadConfig.setUrl("http://12.com");
        remoteReadConfig.setBasicAuth(basicAuth);
        return remoteReadConfig;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicAuth {
        private String username;
        private String password;
        private String passwordFile;
    }

    @Data
    public static class Authorization {
        private String type;
        private String credentials;
        private String credentialsFile;
    }

    @Data
    public static class OAuth2 {
        private String clientID;
        private String clientSecret;
        private String clientSecretFile;
        private List<String> scopes;
        private String tokenURL;
        private Map<String, String> endpointParams;
        private TLSConfig tlsConfig;
    }


    @Data
    public static class TLSConfig {
        private String ca;
        private String cert;
        private String key;
        private String caFile;
        private String certFile;
        private String keyFile;
        private String serverName;
        private boolean insecureSkipVerify;
        private TLSVersion minVersion;
        private TLSVersion maxVersion;

    }

    public enum TLSVersion {
        TLS13, TLS12, TLS11, TLS10
    }


}