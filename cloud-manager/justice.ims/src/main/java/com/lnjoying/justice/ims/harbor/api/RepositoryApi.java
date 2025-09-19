package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Repository;

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
@Component("com.lnjoying.justice.ims.harbor.api.RepositoryApi")
public class RepositoryApi
{
    private ApiClient apiClient;

    public RepositoryApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public RepositoryApi(ApiClient apiClient)
    {
        this.apiClient = apiClient;
    }

    public RepositoryApi(String basePath, String username, String password)
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
     * Delete repository
     * Delete the repository specified by name
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteRepository(String projectName, String repositoryName, String xRequestId) throws RestClientException
    {
        deleteRepositoryWithHttpInfo(projectName, repositoryName, xRequestId);
    }

    /**
     * Delete repository
     * Delete the repository specified by name
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteRepositoryWithHttpInfo(String projectName, String repositoryName, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling deleteRepository");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling deleteRepository");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}").buildAndExpand(uriVariables).toUriString();

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
        return apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get repository
     * Get the repository specified by name
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return Repository
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Repository getRepository(String projectName, String repositoryName, String xRequestId) throws RestClientException
    {
        return getRepositoryWithHttpInfo(projectName, repositoryName, xRequestId).getBody();
    }

    /**
     * Get repository
     * Get the repository specified by name
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Repository&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Repository> getRepositoryWithHttpInfo(String projectName, String repositoryName, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling getRepository");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling getRepository");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<Repository> returnType = new ParameterizedTypeReference<Repository>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List all authorized repositories
     * List all authorized repositories
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @return List&lt;Repository&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Repository> listAllRepositories(String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        return listAllRepositoriesWithHttpInfo(xRequestId, q, sort, page, pageSize).getBody();
    }

    /**
     * List all authorized repositories
     * List all authorized repositories
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @return ResponseEntity&lt;List&lt;Repository&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Repository>> listAllRepositoriesWithHttpInfo(String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/repositories").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sort", sort));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));

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

        ParameterizedTypeReference<List<Repository>> returnType = new ParameterizedTypeReference<List<Repository>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List repositories
     * List repositories of the specified project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @param q           Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort        Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page        The page number (optional, default to 1)
     * @param pageSize    The size of per page (optional, default to 10)
     * @return List&lt;Repository&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Repository> listRepositories(String projectName, String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        return listRepositoriesWithHttpInfo(projectName, xRequestId, q, sort, page, pageSize).getBody();
    }

    /**
     * List repositories
     * List repositories of the specified project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @param q           Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort        Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page        The page number (optional, default to 1)
     * @param pageSize    The size of per page (optional, default to 10)
     * @return ResponseEntity&lt;List&lt;Repository&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Repository>> listRepositoriesWithHttpInfo(String projectName, String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling listRepositories");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sort", sort));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));

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

        ParameterizedTypeReference<List<Repository>> returnType = new ParameterizedTypeReference<List<Repository>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update repository
     * Update the repository specified by name
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param repository     The JSON object of repository. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateRepository(String projectName, String repositoryName, Repository repository, String xRequestId) throws RestClientException
    {
        updateRepositoryWithHttpInfo(projectName, repositoryName, repository, xRequestId);
    }

    /**
     * Update repository
     * Update the repository specified by name
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param repository     The JSON object of repository. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateRepositoryWithHttpInfo(String projectName, String repositoryName, Repository repository, String xRequestId) throws RestClientException
    {
        Object postBody = repository;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling updateRepository");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling updateRepository");
        }

        // verify the required parameter 'repository' is set
        if (repository == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repository' when calling updateRepository");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}").buildAndExpand(uriVariables).toUriString();

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
