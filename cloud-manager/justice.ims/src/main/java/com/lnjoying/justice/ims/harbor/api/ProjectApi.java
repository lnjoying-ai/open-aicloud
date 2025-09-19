package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.AuditLog;
import com.lnjoying.justice.ims.harbor.model.Project;
import com.lnjoying.justice.ims.harbor.model.ProjectDeletable;
import com.lnjoying.justice.ims.harbor.model.ProjectReq;
import com.lnjoying.justice.ims.harbor.model.ProjectScanner;
import com.lnjoying.justice.ims.harbor.model.ProjectSummary;
import com.lnjoying.justice.ims.harbor.model.ScannerRegistration;

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
@Component("com.lnjoying.justice.ims.harbor.api.ProjectApi")
public class ProjectApi
{
    private ApiClient apiClient;

    public ProjectApi()
    {
        this(new ApiClient());
    }


    @Autowired
    public ProjectApi(ApiClient apiClient)
    {
        this.apiClient = apiClient;
    }

    public ProjectApi(String basePath, String username, String password)
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
     * Create a new project.
     * This endpoint is for user to create a new project.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param project                 New created project. (required)
     * @param xRequestId              An unique ID for the request (optional)
     * @param xResourceNameInLocation The flag to indicate whether to return the name of the resource in Location. When X-Resource-Name-In-Location is true, the Location will return the name of the resource. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createProject(ProjectReq project, String xRequestId, Boolean xResourceNameInLocation) throws RestClientException
    {
        createProjectWithHttpInfo(project, xRequestId, xResourceNameInLocation);
    }

    /**
     * Create a new project.
     * This endpoint is for user to create a new project.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param project                 New created project. (required)
     * @param xRequestId              An unique ID for the request (optional)
     * @param xResourceNameInLocation The flag to indicate whether to return the name of the resource in Location. When X-Resource-Name-In-Location is true, the Location will return the name of the resource. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createProjectWithHttpInfo(ProjectReq project, String xRequestId, Boolean xResourceNameInLocation) throws RestClientException
    {
        Object postBody = project;

        // verify the required parameter 'project' is set
        if (project == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'project' when calling createProject");
        }

        String path = UriComponentsBuilder.fromPath("/projects").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));
        if (xResourceNameInLocation != null)
            headerParams.add("X-Resource-Name-In-Location", apiClient.parameterToString(xResourceNameInLocation));

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
     * Delete project by projectID
     * This endpoint is aimed to delete project by project ID.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteProject(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        deleteProjectWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName);
    }

    /**
     * Delete project by projectID
     * This endpoint is aimed to delete project by project ID.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteProjectWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling deleteProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}").buildAndExpand(uriVariables).toUriString();

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
     * Get recent logs of the projects
     * Get recent logs of the projects
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @param q           Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort        Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page        The page number (optional, default to 1)
     * @param pageSize    The size of per page (optional, default to 10)
     * @return List&lt;AuditLog&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<AuditLog> getLogs(String projectName, String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        return getLogsWithHttpInfo(projectName, xRequestId, q, sort, page, pageSize).getBody();
    }

    /**
     * Get recent logs of the projects
     * Get recent logs of the projects
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @param q           Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort        Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page        The page number (optional, default to 1)
     * @param pageSize    The size of per page (optional, default to 10)
     * @return ResponseEntity&lt;List&lt;AuditLog&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<AuditLog>> getLogsWithHttpInfo(String projectName, String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling getLogs");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/logs").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<List<AuditLog>> returnType = new ParameterizedTypeReference<List<AuditLog>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Return specific project detail information
     * This endpoint returns specific project information by project ID.
     * <p><b>200</b> - Return matched project information.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return Project
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Project getProject(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        return getProjectWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName).getBody();
    }

    /**
     * Return specific project detail information
     * This endpoint returns specific project information by project ID.
     * <p><b>200</b> - Return matched project information.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Project&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Project> getProjectWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling getProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<Project> returnType = new ParameterizedTypeReference<Project>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get the deletable status of the project
     * Get the deletable status of the project
     * <p><b>200</b> - Return deletable status of the project.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ProjectDeletable
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ProjectDeletable getProjectDeletable(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        return getProjectDeletableWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName).getBody();
    }

    /**
     * Get the deletable status of the project
     * Get the deletable status of the project
     * <p><b>200</b> - Return deletable status of the project.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;ProjectDeletable&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ProjectDeletable> getProjectDeletableWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling getProjectDeletable");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/_deletable").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<ProjectDeletable> returnType = new ParameterizedTypeReference<ProjectDeletable>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get summary of the project.
     * Get summary of the project.
     * <p><b>200</b> - Get summary of the project successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ProjectSummary
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ProjectSummary getProjectSummary(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        return getProjectSummaryWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName).getBody();
    }

    /**
     * Get summary of the project.
     * Get summary of the project.
     * <p><b>200</b> - Get summary of the project successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;ProjectSummary&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ProjectSummary> getProjectSummaryWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling getProjectSummary");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/summary").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<ProjectSummary> returnType = new ParameterizedTypeReference<ProjectSummary>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get project level scanner
     * Get the scanner registration of the specified project. If no scanner registration is configured for the specified project, the system default scanner registration will be returned.
     * <p><b>200</b> - The details of the scanner registration.
     * <p><b>400</b> - Bad project ID
     * <p><b>401</b> - Unauthorized request
     * <p><b>403</b> - Request is not allowed
     * <p><b>404</b> - The requested object is not found
     * <p><b>500</b> - Internal server error happened
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ScannerRegistration
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ScannerRegistration getScannerOfProject(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        return getScannerOfProjectWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName).getBody();
    }

    /**
     * Get project level scanner
     * Get the scanner registration of the specified project. If no scanner registration is configured for the specified project, the system default scanner registration will be returned.
     * <p><b>200</b> - The details of the scanner registration.
     * <p><b>400</b> - Bad project ID
     * <p><b>401</b> - Unauthorized request
     * <p><b>403</b> - Request is not allowed
     * <p><b>404</b> - The requested object is not found
     * <p><b>500</b> - Internal server error happened
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;ScannerRegistration&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ScannerRegistration> getScannerOfProjectWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling getScannerOfProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/scanner").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<ScannerRegistration> returnType = new ParameterizedTypeReference<ScannerRegistration>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Check if the project name user provided already exists.
     * This endpoint is used to check if the project name provided already exist.
     * <p><b>200</b> - Success
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName Project name for checking exists. (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void headProject(String projectName, String xRequestId) throws RestClientException
    {
        headProjectWithHttpInfo(projectName, xRequestId);
    }

    /**
     * Check if the project name user provided already exists.
     * This endpoint is used to check if the project name provided already exist.
     * <p><b>200</b> - Success
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName Project name for checking exists. (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> headProjectWithHttpInfo(String projectName, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling headProject");
        }

        String path = UriComponentsBuilder.fromPath("/projects").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "project_name", projectName));

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
        return apiClient.invokeAPI(path, HttpMethod.HEAD, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List projects
     * This endpoint returns projects created by Harbor.
     * <p><b>200</b> - Return all matched projects.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param name       The name of project. (optional)
     * @param _public    The project is public or private. (optional)
     * @param owner      The name of project owner. (optional)
     * @param withDetail Bool value indicating whether return detailed information of the project (optional, default to true)
     * @return List&lt;Project&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Project> listProjects(String xRequestId, String q, Long page, Long pageSize, String sort, String name, Boolean _public, String owner, Boolean withDetail) throws RestClientException
    {
        return listProjectsWithHttpInfo(xRequestId, q, page, pageSize, sort, name, _public, owner, withDetail).getBody();
    }

    /**
     * List projects
     * This endpoint returns projects created by Harbor.
     * <p><b>200</b> - Return all matched projects.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param name       The name of project. (optional)
     * @param _public    The project is public or private. (optional)
     * @param owner      The name of project owner. (optional)
     * @param withDetail Bool value indicating whether return detailed information of the project (optional, default to true)
     * @return ResponseEntity&lt;List&lt;Project&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Project>> listProjectsWithHttpInfo(String xRequestId, String q, Long page, Long pageSize, String sort, String name, Boolean _public, String owner, Boolean withDetail) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/projects").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sort", sort));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name", name));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "public", _public));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "owner", owner));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_detail", withDetail));

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

        ParameterizedTypeReference<List<Project>> returnType = new ParameterizedTypeReference<List<Project>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get scanner registration candidates for configurating project level scanner
     * Retrieve the system configured scanner registrations as candidates of setting project level scanner.
     * <p><b>200</b> - A list of scanner registrations.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param q               Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort            Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page            The page number (optional, default to 1)
     * @param pageSize        The size of per page (optional, default to 10)
     * @return List&lt;ScannerRegistration&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<ScannerRegistration> listScannerCandidatesOfProject(String projectNameOrId, String xRequestId, Boolean xIsResourceName, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        return listScannerCandidatesOfProjectWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName, q, sort, page, pageSize).getBody();
    }

    /**
     * Get scanner registration candidates for configurating project level scanner
     * Retrieve the system configured scanner registrations as candidates of setting project level scanner.
     * <p><b>200</b> - A list of scanner registrations.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param q               Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort            Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page            The page number (optional, default to 1)
     * @param pageSize        The size of per page (optional, default to 10)
     * @return ResponseEntity&lt;List&lt;ScannerRegistration&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<ScannerRegistration>> listScannerCandidatesOfProjectWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling listScannerCandidatesOfProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/scanner/candidates").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sort", sort));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));

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

        ParameterizedTypeReference<List<ScannerRegistration>> returnType = new ParameterizedTypeReference<List<ScannerRegistration>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Configure scanner for the specified project
     * Set one of the system configured scanner registration as the indepndent scanner of the specified project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param payload         (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void setScannerOfProject(String projectNameOrId, ProjectScanner payload, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        setScannerOfProjectWithHttpInfo(projectNameOrId, payload, xRequestId, xIsResourceName);
    }

    /**
     * Configure scanner for the specified project
     * Set one of the system configured scanner registration as the indepndent scanner of the specified project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param payload         (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> setScannerOfProjectWithHttpInfo(String projectNameOrId, ProjectScanner payload, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = payload;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling setScannerOfProject");
        }

        // verify the required parameter 'payload' is set
        if (payload == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'payload' when calling setScannerOfProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/scanner").buildAndExpand(uriVariables).toUriString();

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

    /**
     * Update properties for a selected project.
     * This endpoint is aimed to update the properties of a project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param project         Updates of project. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateProject(String projectNameOrId, ProjectReq project, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        updateProjectWithHttpInfo(projectNameOrId, project, xRequestId, xIsResourceName);
    }

    /**
     * Update properties for a selected project.
     * This endpoint is aimed to update the properties of a project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param project         Updates of project. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateProjectWithHttpInfo(String projectNameOrId, ProjectReq project, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = project;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling updateProject");
        }

        // verify the required parameter 'project' is set
        if (project == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'project' when calling updateProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}").buildAndExpand(uriVariables).toUriString();

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
