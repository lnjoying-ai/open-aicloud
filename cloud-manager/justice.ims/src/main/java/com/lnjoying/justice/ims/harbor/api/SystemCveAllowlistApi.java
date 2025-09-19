package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.CVEAllowlist;

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
@Component("com.lnjoying.justice.ims.harbor.api.SystemCveAllowlistApi")
public class SystemCveAllowlistApi
{
    private ApiClient apiClient;

    public SystemCveAllowlistApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public SystemCveAllowlistApi(ApiClient apiClient)
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
     * Get the system level allowlist of CVE.
     * Get the system level allowlist of CVE.  This API can be called by all authenticated users.
     * <p><b>200</b> - Successfully retrieved the CVE allowlist.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return CVEAllowlist
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public CVEAllowlist getSystemCVEAllowlist(String xRequestId) throws RestClientException
    {
        return getSystemCVEAllowlistWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Get the system level allowlist of CVE.
     * Get the system level allowlist of CVE.  This API can be called by all authenticated users.
     * <p><b>200</b> - Successfully retrieved the CVE allowlist.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;CVEAllowlist&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CVEAllowlist> getSystemCVEAllowlistWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/system/CVEAllowlist").build().toUriString();

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

        ParameterizedTypeReference<CVEAllowlist> returnType = new ParameterizedTypeReference<CVEAllowlist>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update the system level allowlist of CVE.
     * This API overwrites the system level allowlist of CVE with the list in request body.  Only system Admin has permission to call this API.
     * <p><b>200</b> - Successfully updated the CVE allowlist.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param allowlist  The allowlist with new content (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void putSystemCVEAllowlist(String xRequestId, CVEAllowlist allowlist) throws RestClientException
    {
        putSystemCVEAllowlistWithHttpInfo(xRequestId, allowlist);
    }

    /**
     * Update the system level allowlist of CVE.
     * This API overwrites the system level allowlist of CVE with the list in request body.  Only system Admin has permission to call this API.
     * <p><b>200</b> - Successfully updated the CVE allowlist.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param allowlist  The allowlist with new content (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> putSystemCVEAllowlistWithHttpInfo(String xRequestId, CVEAllowlist allowlist) throws RestClientException
    {
        Object postBody = allowlist;

        String path = UriComponentsBuilder.fromPath("/system/CVEAllowlist").build().toUriString();

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
