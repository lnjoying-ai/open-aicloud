package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Search;

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
@Component("com.lnjoying.justice.ims.harbor.api.SearchApi")
public class SearchApi
{
    private ApiClient apiClient;

    public SearchApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public SearchApi(ApiClient apiClient)
    {
        this.apiClient = apiClient;
    }

    public SearchApi(String basePath, String username, String password)
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
     * Search for projects, repositories and helm charts
     * The Search endpoint returns information about the projects, repositories and helm charts offered at public status or related to the current logged in user. The response includes the project, repository list and charts in a proper display order.
     * <p><b>200</b> - An array of search results
     * <p><b>500</b> - Internal server error
     *
     * @param q          Search parameter for project and repository name. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return Search
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Search search(String q, String xRequestId) throws RestClientException
    {
        return searchWithHttpInfo(q, xRequestId).getBody();
    }

    /**
     * Search for projects, repositories and helm charts
     * The Search endpoint returns information about the projects, repositories and helm charts offered at public status or related to the current logged in user. The response includes the project, repository list and charts in a proper display order.
     * <p><b>200</b> - An array of search results
     * <p><b>500</b> - Internal server error
     *
     * @param q          Search parameter for project and repository name. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Search&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Search> searchWithHttpInfo(String q, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'q' is set
        if (q == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'q' when calling search");
        }

        String path = UriComponentsBuilder.fromPath("/search").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));

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

        ParameterizedTypeReference<Search> returnType = new ParameterizedTypeReference<Search>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
