package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2021-08-31T16:17:12.981+08:00")
@Component("com.lnjoying.justice.ims.harbor.api.PingApi")
public class PingApi
{

    public static final String PING = "PING";

    public static final String PONG = "PONG";

    private ApiClient apiClient;

    public PingApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public PingApi(ApiClient apiClient)
    {
        this.apiClient = apiClient;
    }

    public PingApi(String basePath, String username, String password)
    {
        this.apiClient = new ApiClient(basePath, username, password);
    }

    public ApiClient getApiClient()
    {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient)
    {
        this.apiClient = apiClient;
    }

    /**
     * Ping Harbor to check if it&#39;s alive.
     * This API simply replies a pong to indicate the process to handle API is up, disregarding the health status of dependent components.
     * <p><b>200</b> - The API server is alive
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String getPing(String xRequestId) throws RestClientException
    {
        return getPingWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Ping Harbor to check if it&#39;s alive.
     * This API simply replies a pong to indicate the process to handle API is up, disregarding the health status of dependent components.
     * <p><b>200</b> - The API server is alive
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<String> getPingWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/ping").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));

        final String[] accepts = {
                "text/plain"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"basic"};

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
