package com.lnjoying.justice.iam.authz.opa.client.rest.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Function;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:46
 */

@Slf4j
public class OpaUrl
{

    private final String url;

    private OpaUrl(String url)
    {
        this.url = url;
    }

    /**
     * @return Normalized version of URL, removing multiple and trailing slashes
     */
    public OpaUrl normalized() {
        String normalizedValue = normalize(url);
        return new OpaUrl(normalizedValue);
    }

    public static OpaUrl of(String serverUrl, String endpoint) {
        return new OpaUrl(urlOf(serverUrl, endpoint));
    }


    public String getUrl()
    {
        return url;
    }

    private String normalize(String inputUrl) {
        String normalized = removeExtraSlashes()
                .andThen(removeTrailingSlash())
                .apply(inputUrl.trim());
        if (!inputUrl.equals(normalized)) {
            log.debug("Supplied URL [{}] is malformed, has to be normalized", inputUrl);
        }
        return normalized;
    }


    private static String urlOf(String url, String endpoint) {
        return url + "/" + Optional.ofNullable(endpoint).orElseThrow(() -> new InvalidEndpointException("Invalid endpoint: " + endpoint));
    }

    private Function<String, String> removeTrailingSlash() {
        return (input) -> input.endsWith("/") ? input.substring(0, input.length() - 1) : input;
    }

    private Function<String, String> removeExtraSlashes() {
        return (input) -> input.replaceAll("([^:])//+", "$1/");
    }


}
