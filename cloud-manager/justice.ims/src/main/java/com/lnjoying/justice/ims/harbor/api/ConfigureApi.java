package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Configurations;
import com.lnjoying.justice.ims.harbor.model.ConfigurationsResponse;
import com.lnjoying.justice.ims.harbor.model.InternalConfigurationsResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2021-08-31T16:17:12.981+08:00")
@Component("com.lnjoying.justice.ims.harbor.api.ConfigureApi")
public class ConfigureApi
{
    private ApiClient apiClient;

    public ConfigureApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public ConfigureApi(ApiClient apiClient)
    {
        this.apiClient = apiClient;
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
     * Get system configurations.
     * This endpoint is for retrieving system configurations that only provides for admin user.
     * <p><b>200</b> - Get system configurations successfully. The response body is a map.
     * <p><b>401</b> - User need to log in first.ß
     * <p><b>403</b> - User does not have permission of admin role.
     * <p><b>500</b> - Unexpected internal errors.
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ConfigurationsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ConfigurationsResponse getConfigurations(String xRequestId) throws RestClientException
    {
        return getConfigurationsWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Get system configurations.
     * This endpoint is for retrieving system configurations that only provides for admin user.
     * <p><b>200</b> - Get system configurations successfully. The response body is a map.
     * <p><b>401</b> - User need to log in first.ß
     * <p><b>403</b> - User does not have permission of admin role.
     * <p><b>500</b> - Unexpected internal errors.
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;ConfigurationsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ConfigurationsResponse> getConfigurationsWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/configurations").build().toUriString();

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

        ParameterizedTypeReference<ConfigurationsResponse> returnType = new ParameterizedTypeReference<ConfigurationsResponse>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get internal configurations.
     * This endpoint is for retrieving system configurations that only provides for internal api call.
     * <p><b>200</b> - Get system configurations successfully. The response body is a map.
     * <p><b>401</b> - User need to log in first.
     * <p><b>403</b> - User does not have permission of admin role.
     * <p><b>500</b> - Unexpected internal errors.
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return InternalConfigurationsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public InternalConfigurationsResponse getInternalconfig(String xRequestId) throws RestClientException
    {
        return getInternalconfigWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Get internal configurations.
     * This endpoint is for retrieving system configurations that only provides for internal api call.
     * <p><b>200</b> - Get system configurations successfully. The response body is a map.
     * <p><b>401</b> - User need to log in first.
     * <p><b>403</b> - User does not have permission of admin role.
     * <p><b>500</b> - Unexpected internal errors.
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;InternalConfigurationsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<InternalConfigurationsResponse> getInternalconfigWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/internalconfig").build().toUriString();

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

        ParameterizedTypeReference<InternalConfigurationsResponse> returnType = new ParameterizedTypeReference<InternalConfigurationsResponse>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Modify system configurations.
     * This endpoint is for modifying system configurations that only provides for admin user.
     * <p><b>200</b> - Modify system configurations successfully.
     * <p><b>401</b> - User need to log in first.
     * <p><b>403</b> - User does not have permission of admin role.
     * <p><b>500</b> - Unexpected internal errors.
     *
     * @param configurations The configuration map can contain a subset of the attributes of the schema, which are to be updated. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateConfigurations(Configurations configurations, String xRequestId) throws RestClientException
    {
        updateConfigurationsWithHttpInfo(configurations, xRequestId);
    }

    /**
     * Modify system configurations.
     * This endpoint is for modifying system configurations that only provides for admin user.
     * <p><b>200</b> - Modify system configurations successfully.
     * <p><b>401</b> - User need to log in first.
     * <p><b>403</b> - User does not have permission of admin role.
     * <p><b>500</b> - Unexpected internal errors.
     *
     * @param configurations The configuration map can contain a subset of the attributes of the schema, which are to be updated. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateConfigurationsWithHttpInfo(Configurations configurations, String xRequestId) throws RestClientException
    {
        Object postBody = configurations;

        // verify the required parameter 'configurations' is set
        if (configurations == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'configurations' when calling updateConfigurations");
        }

        String path = UriComponentsBuilder.fromPath("/configurations").build().toUriString();

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

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.PUT, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
