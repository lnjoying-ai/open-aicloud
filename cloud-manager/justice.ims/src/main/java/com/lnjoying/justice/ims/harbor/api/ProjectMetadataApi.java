package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

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
@Component("com.lnjoying.justice.ims.harbor.api.ProjectMetadataApi")
public class ProjectMetadataApi
{
    private ApiClient apiClient;

    public ProjectMetadataApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public ProjectMetadataApi(ApiClient apiClient)
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
     * Add metadata for the specific project
     * Add metadata for the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param metadata        (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void addProjectMetadatas(String projectNameOrId, String xRequestId, Boolean xIsResourceName, Object metadata) throws RestClientException
    {
        addProjectMetadatasWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName, metadata);
    }

    /**
     * Add metadata for the specific project
     * Add metadata for the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param metadata        (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> addProjectMetadatasWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName, Object metadata) throws RestClientException
    {
        Object postBody = metadata;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling addProjectMetadatas");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/metadatas/").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));
        if (xIsResourceName != null)
            headerParams.add("X-Is-Resource-Name", apiClient.parameterToString(xIsResourceName));

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
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Delete the specific metadata for the specific project
     * Delete the specific metadata for the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param metaName        The name of metadata. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteProjectMetadata(String projectNameOrId, String metaName, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        deleteProjectMetadataWithHttpInfo(projectNameOrId, metaName, xRequestId, xIsResourceName);
    }

    /**
     * Delete the specific metadata for the specific project
     * Delete the specific metadata for the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param metaName        The name of metadata. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteProjectMetadataWithHttpInfo(String projectNameOrId, String metaName, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling deleteProjectMetadata");
        }

        // verify the required parameter 'metaName' is set
        if (metaName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'metaName' when calling deleteProjectMetadata");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        uriVariables.put("meta_name", metaName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/metadatas/{meta_name}").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));
        if (xIsResourceName != null)
            headerParams.add("X-Is-Resource-Name", apiClient.parameterToString(xIsResourceName));

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
     * Get the specific metadata of the specific project
     * Get the specific metadata of the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param metaName        The name of metadata. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return Map&lt;String, String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Map<String, String> getProjectMetadata(String projectNameOrId, String metaName, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        return getProjectMetadataWithHttpInfo(projectNameOrId, metaName, xRequestId, xIsResourceName).getBody();
    }

    /**
     * Get the specific metadata of the specific project
     * Get the specific metadata of the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param metaName        The name of metadata. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Map&lt;String, String&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Map<String, String>> getProjectMetadataWithHttpInfo(String projectNameOrId, String metaName, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling getProjectMetadata");
        }

        // verify the required parameter 'metaName' is set
        if (metaName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'metaName' when calling getProjectMetadata");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        uriVariables.put("meta_name", metaName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/metadatas/{meta_name}").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));
        if (xIsResourceName != null)
            headerParams.add("X-Is-Resource-Name", apiClient.parameterToString(xIsResourceName));

        final String[] accepts = {
                "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"basic"};

        ParameterizedTypeReference<Map<String, String>> returnType = new ParameterizedTypeReference<Map<String, String>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get the metadata of the specific project
     * Get the metadata of the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return Map&lt;String, String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Map<String, String> listProjectMetadatas(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        return listProjectMetadatasWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName).getBody();
    }

    /**
     * Get the metadata of the specific project
     * Get the metadata of the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Map&lt;String, String&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Map<String, String>> listProjectMetadatasWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling listProjectMetadatas");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/metadatas/").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));
        if (xIsResourceName != null)
            headerParams.add("X-Is-Resource-Name", apiClient.parameterToString(xIsResourceName));

        final String[] accepts = {
                "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"basic"};

        ParameterizedTypeReference<Map<String, String>> returnType = new ParameterizedTypeReference<Map<String, String>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update the specific metadata for the specific project
     * Update the specific metadata for the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param metaName        The name of metadata. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param metadata        (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateProjectMetadata(String projectNameOrId, String metaName, String xRequestId, Boolean xIsResourceName, Object metadata) throws RestClientException
    {
        updateProjectMetadataWithHttpInfo(projectNameOrId, metaName, xRequestId, xIsResourceName, metadata);
    }

    /**
     * Update the specific metadata for the specific project
     * Update the specific metadata for the specific project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param metaName        The name of metadata. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param metadata        (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateProjectMetadataWithHttpInfo(String projectNameOrId, String metaName, String xRequestId, Boolean xIsResourceName, Object metadata) throws RestClientException
    {
        Object postBody = metadata;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling updateProjectMetadata");
        }

        // verify the required parameter 'metaName' is set
        if (metaName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'metaName' when calling updateProjectMetadata");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        uriVariables.put("meta_name", metaName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/metadatas/{meta_name}").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));
        if (xIsResourceName != null)
            headerParams.add("X-Is-Resource-Name", apiClient.parameterToString(xIsResourceName));

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
