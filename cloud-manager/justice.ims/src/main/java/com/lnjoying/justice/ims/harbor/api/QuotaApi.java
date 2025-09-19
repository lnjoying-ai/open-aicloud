package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Quota;
import com.lnjoying.justice.ims.harbor.model.QuotaUpdateReq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Component("com.lnjoying.justice.ims.harbor.api.QuotaApi")
public class QuotaApi
{
    private ApiClient apiClient;

    public QuotaApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public QuotaApi(ApiClient apiClient)
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
     * Get the specified quota
     * Get the specified quota
     * <p><b>200</b> - Successfully retrieved the quota.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param id         Quota ID (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return Quota
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Quota getQuota(Integer id, String xRequestId) throws RestClientException
    {
        return getQuotaWithHttpInfo(id, xRequestId).getBody();
    }

    /**
     * Get the specified quota
     * Get the specified quota
     * <p><b>200</b> - Successfully retrieved the quota.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param id         Quota ID (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Quota&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Quota> getQuotaWithHttpInfo(Integer id, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling getQuota");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/quotas/{id}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<Quota> returnType = new ParameterizedTypeReference<Quota>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List quotas
     * List quotas
     * <p><b>200</b> - Successfully retrieved the quotas.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId  An unique ID for the request (optional)
     * @param page        The page number (optional, default to 1)
     * @param pageSize    The size of per page (optional, default to 10)
     * @param reference   The reference type of quota. (optional)
     * @param referenceId The reference id of quota. (optional)
     * @param sort        Sort method, valid values include: &#39;hard.resource_name&#39;, &#39;-hard.resource_name&#39;, &#39;used.resource_name&#39;, &#39;-used.resource_name&#39;. Here &#39;-&#39; stands for descending order, resource_name should be the real resource name of the quota.  (optional)
     * @return List&lt;Quota&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Quota> listQuotas(String xRequestId, Long page, Long pageSize, String reference, String referenceId, String sort) throws RestClientException
    {
        return listQuotasWithHttpInfo(xRequestId, page, pageSize, reference, referenceId, sort).getBody();
    }

    /**
     * List quotas
     * List quotas
     * <p><b>200</b> - Successfully retrieved the quotas.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId  An unique ID for the request (optional)
     * @param page        The page number (optional, default to 1)
     * @param pageSize    The size of per page (optional, default to 10)
     * @param reference   The reference type of quota. (optional)
     * @param referenceId The reference id of quota. (optional)
     * @param sort        Sort method, valid values include: &#39;hard.resource_name&#39;, &#39;-hard.resource_name&#39;, &#39;used.resource_name&#39;, &#39;-used.resource_name&#39;. Here &#39;-&#39; stands for descending order, resource_name should be the real resource name of the quota.  (optional)
     * @return ResponseEntity&lt;List&lt;Quota&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Quota>> listQuotasWithHttpInfo(String xRequestId, Long page, Long pageSize, String reference, String referenceId, String sort) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/quotas").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "reference", reference));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "reference_id", referenceId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sort", sort));

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

        ParameterizedTypeReference<List<Quota>> returnType = new ParameterizedTypeReference<List<Quota>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update the specified quota
     * Update hard limits of the specified quota
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param id         Quota ID (required)
     * @param hard       The new hard limits for the quota (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateQuota(Integer id, QuotaUpdateReq hard, String xRequestId) throws RestClientException
    {
        updateQuotaWithHttpInfo(id, hard, xRequestId);
    }

    /**
     * Update the specified quota
     * Update hard limits of the specified quota
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param id         Quota ID (required)
     * @param hard       The new hard limits for the quota (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateQuotaWithHttpInfo(Integer id, QuotaUpdateReq hard, String xRequestId) throws RestClientException
    {
        Object postBody = hard;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling updateQuota");
        }

        // verify the required parameter 'hard' is set
        if (hard == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'hard' when calling updateQuota");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/quotas/{id}").buildAndExpand(uriVariables).toUriString();

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
