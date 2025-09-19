package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.OverallHealthStatus;

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
@Component("com.lnjoying.justice.ims.harbor.api.HealthApi")
public class HealthApi
{
    private ApiClient apiClient;

    public HealthApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public HealthApi(ApiClient apiClient)
    {
        this.apiClient = apiClient;
    }

    public HealthApi(String basePath, String username, String password)
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
     * Check the status of Harbor components
     * Check the status of Harbor components
     * <p><b>200</b> - The health status of Harbor components
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return OverallHealthStatus
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OverallHealthStatus getHealth(String xRequestId) throws RestClientException
    {
        return getHealthWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Check the status of Harbor components
     * Check the status of Harbor components
     * <p><b>200</b> - The health status of Harbor components
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;OverallHealthStatus&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OverallHealthStatus> getHealthWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/health").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));

        final String[] accepts = {
                "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"basic"};

        ParameterizedTypeReference<OverallHealthStatus> returnType = new ParameterizedTypeReference<OverallHealthStatus>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
