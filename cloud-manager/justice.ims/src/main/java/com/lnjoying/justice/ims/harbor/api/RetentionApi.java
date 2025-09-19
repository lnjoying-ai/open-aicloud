package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Body;
import com.lnjoying.justice.ims.harbor.model.Body1;
import com.lnjoying.justice.ims.harbor.model.RetentionExecution;
import com.lnjoying.justice.ims.harbor.model.RetentionExecutionTask;
import com.lnjoying.justice.ims.harbor.model.RetentionMetadata;
import com.lnjoying.justice.ims.harbor.model.RetentionPolicy;

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
@Component("com.lnjoying.justice.ims.harbor.api.RetentionApi")
public class RetentionApi
{
    private ApiClient apiClient;

    public RetentionApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public RetentionApi(ApiClient apiClient)
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
     * Create Retention Policy
     * Create Retention Policy, you can reference metadatas API for the policy model. You can check project metadatas to find whether a retention policy is already binded. This method should only be called when no retention policy binded to project yet.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param policy     Create Retention Policy successfully. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createRetention(RetentionPolicy policy, String xRequestId) throws RestClientException
    {
        createRetentionWithHttpInfo(policy, xRequestId);
    }

    /**
     * Create Retention Policy
     * Create Retention Policy, you can reference metadatas API for the policy model. You can check project metadatas to find whether a retention policy is already binded. This method should only be called when no retention policy binded to project yet.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param policy     Create Retention Policy successfully. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createRetentionWithHttpInfo(RetentionPolicy policy, String xRequestId) throws RestClientException
    {
        Object postBody = policy;

        // verify the required parameter 'policy' is set
        if (policy == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'policy' when calling createRetention");
        }

        String path = UriComponentsBuilder.fromPath("/retentions").build().toUriString();

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
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Delete Retention Policy
     * Delete Retention Policy, you can reference metadatas API for the policy model. You can check project metadatas to find whether a retention policy is already binded. This method should only be called when retention policy has already binded to project.
     * <p><b>200</b> - Update Retention Policy successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteRetention(Long id, String xRequestId) throws RestClientException
    {
        deleteRetentionWithHttpInfo(id, xRequestId);
    }

    /**
     * Delete Retention Policy
     * Delete Retention Policy, you can reference metadatas API for the policy model. You can check project metadatas to find whether a retention policy is already binded. This method should only be called when retention policy has already binded to project.
     * <p><b>200</b> - Update Retention Policy successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteRetentionWithHttpInfo(Long id, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling deleteRetention");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/retentions/{id}").buildAndExpand(uriVariables).toUriString();

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
     * Get Retention Metadatas
     * Get Retention Metadatas.
     * <p><b>200</b> - Get Retention Metadatas successfully.
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return RetentionMetadata
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetentionMetadata getRentenitionMetadata(String xRequestId) throws RestClientException
    {
        return getRentenitionMetadataWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Get Retention Metadatas
     * Get Retention Metadatas.
     * <p><b>200</b> - Get Retention Metadatas successfully.
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;RetentionMetadata&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetentionMetadata> getRentenitionMetadataWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/retentions/metadatas").build().toUriString();

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

        ParameterizedTypeReference<RetentionMetadata> returnType = new ParameterizedTypeReference<RetentionMetadata>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get Retention Policy
     * Get Retention Policy.
     * <p><b>200</b> - Get Retention Policy successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return RetentionPolicy
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetentionPolicy getRetention(Long id, String xRequestId) throws RestClientException
    {
        return getRetentionWithHttpInfo(id, xRequestId).getBody();
    }

    /**
     * Get Retention Policy
     * Get Retention Policy.
     * <p><b>200</b> - Get Retention Policy successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;RetentionPolicy&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetentionPolicy> getRetentionWithHttpInfo(Long id, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling getRetention");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/retentions/{id}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<RetentionPolicy> returnType = new ParameterizedTypeReference<RetentionPolicy>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get Retention job task log
     * Get Retention job task log, tags ratain or deletion detail will be shown in a table.
     * <p><b>200</b> - Get Retention job task log successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param eid        Retention execution ID. (required)
     * @param tid        Retention execution ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String getRetentionTaskLog(Long id, Long eid, Long tid, String xRequestId) throws RestClientException
    {
        return getRetentionTaskLogWithHttpInfo(id, eid, tid, xRequestId).getBody();
    }

    /**
     * Get Retention job task log
     * Get Retention job task log, tags ratain or deletion detail will be shown in a table.
     * <p><b>200</b> - Get Retention job task log successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param eid        Retention execution ID. (required)
     * @param tid        Retention execution ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<String> getRetentionTaskLogWithHttpInfo(Long id, Long eid, Long tid, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling getRetentionTaskLog");
        }

        // verify the required parameter 'eid' is set
        if (eid == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'eid' when calling getRetentionTaskLog");
        }

        // verify the required parameter 'tid' is set
        if (tid == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'tid' when calling getRetentionTaskLog");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        uriVariables.put("eid", eid);
        uriVariables.put("tid", tid);
        String path = UriComponentsBuilder.fromPath("/retentions/{id}/executions/{eid}/tasks/{tid}").buildAndExpand(uriVariables).toUriString();

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

    /**
     * Get Retention executions
     * Get Retention executions, execution status may be delayed before job service schedule it up.
     * <p><b>200</b> - Get a Retention execution successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @param page       The page number. (optional)
     * @param pageSize   The size of per page. (optional)
     * @return List&lt;RetentionExecution&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<RetentionExecution> listRetentionExecutions(Long id, String xRequestId, Long page, Long pageSize) throws RestClientException
    {
        return listRetentionExecutionsWithHttpInfo(id, xRequestId, page, pageSize).getBody();
    }

    /**
     * Get Retention executions
     * Get Retention executions, execution status may be delayed before job service schedule it up.
     * <p><b>200</b> - Get a Retention execution successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @param page       The page number. (optional)
     * @param pageSize   The size of per page. (optional)
     * @return ResponseEntity&lt;List&lt;RetentionExecution&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<RetentionExecution>> listRetentionExecutionsWithHttpInfo(Long id, String xRequestId, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling listRetentionExecutions");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/retentions/{id}/executions").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

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

        ParameterizedTypeReference<List<RetentionExecution>> returnType = new ParameterizedTypeReference<List<RetentionExecution>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get Retention tasks
     * Get Retention tasks, each repository as a task.
     * <p><b>200</b> - Get Retention job tasks successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param eid        Retention execution ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @param page       The page number. (optional)
     * @param pageSize   The size of per page. (optional)
     * @return List&lt;RetentionExecutionTask&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<RetentionExecutionTask> listRetentionTasks(Long id, Long eid, String xRequestId, Long page, Long pageSize) throws RestClientException
    {
        return listRetentionTasksWithHttpInfo(id, eid, xRequestId, page, pageSize).getBody();
    }

    /**
     * Get Retention tasks
     * Get Retention tasks, each repository as a task.
     * <p><b>200</b> - Get Retention job tasks successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param eid        Retention execution ID. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @param page       The page number. (optional)
     * @param pageSize   The size of per page. (optional)
     * @return ResponseEntity&lt;List&lt;RetentionExecutionTask&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<RetentionExecutionTask>> listRetentionTasksWithHttpInfo(Long id, Long eid, String xRequestId, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling listRetentionTasks");
        }

        // verify the required parameter 'eid' is set
        if (eid == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'eid' when calling listRetentionTasks");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        uriVariables.put("eid", eid);
        String path = UriComponentsBuilder.fromPath("/retentions/{id}/executions/{eid}/tasks").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

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

        ParameterizedTypeReference<List<RetentionExecutionTask>> returnType = new ParameterizedTypeReference<List<RetentionExecutionTask>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Stop a Retention execution
     * Stop a Retention execution, only support \&quot;stop\&quot; action now.
     * <p><b>200</b> - Stop a Retention job successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param eid        Retention execution ID. (required)
     * @param body       The action, only support \&quot;stop\&quot; now. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void operateRetentionExecution(Long id, Long eid, Body1 body, String xRequestId) throws RestClientException
    {
        operateRetentionExecutionWithHttpInfo(id, eid, body, xRequestId);
    }

    /**
     * Stop a Retention execution
     * Stop a Retention execution, only support \&quot;stop\&quot; action now.
     * <p><b>200</b> - Stop a Retention job successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param eid        Retention execution ID. (required)
     * @param body       The action, only support \&quot;stop\&quot; now. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> operateRetentionExecutionWithHttpInfo(Long id, Long eid, Body1 body, String xRequestId) throws RestClientException
    {
        Object postBody = body;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling operateRetentionExecution");
        }

        // verify the required parameter 'eid' is set
        if (eid == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'eid' when calling operateRetentionExecution");
        }

        // verify the required parameter 'body' is set
        if (body == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling operateRetentionExecution");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        uriVariables.put("eid", eid);
        String path = UriComponentsBuilder.fromPath("/retentions/{id}/executions/{eid}").buildAndExpand(uriVariables).toUriString();

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
        return apiClient.invokeAPI(path, HttpMethod.PATCH, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Trigger a Retention Execution
     * Trigger a Retention Execution, if dry_run is True, nothing would be deleted actually.
     * <p><b>200</b> - Trigger a Retention job successfully.
     * <p><b>201</b> - Created
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param body       (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void triggerRetentionExecution(Long id, Body body, String xRequestId) throws RestClientException
    {
        triggerRetentionExecutionWithHttpInfo(id, body, xRequestId);
    }

    /**
     * Trigger a Retention Execution
     * Trigger a Retention Execution, if dry_run is True, nothing would be deleted actually.
     * <p><b>200</b> - Trigger a Retention job successfully.
     * <p><b>201</b> - Created
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param body       (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> triggerRetentionExecutionWithHttpInfo(Long id, Body body, String xRequestId) throws RestClientException
    {
        Object postBody = body;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling triggerRetentionExecution");
        }

        // verify the required parameter 'body' is set
        if (body == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling triggerRetentionExecution");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/retentions/{id}/executions").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update Retention Policy
     * Update Retention Policy, you can reference metadatas API for the policy model. You can check project metadatas to find whether a retention policy is already binded. This method should only be called when retention policy has already binded to project.
     * <p><b>200</b> - Update Retention Policy successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param policy     (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateRetention(Long id, RetentionPolicy policy, String xRequestId) throws RestClientException
    {
        updateRetentionWithHttpInfo(id, policy, xRequestId);
    }

    /**
     * Update Retention Policy
     * Update Retention Policy, you can reference metadatas API for the policy model. You can check project metadatas to find whether a retention policy is already binded. This method should only be called when retention policy has already binded to project.
     * <p><b>200</b> - Update Retention Policy successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param id         Retention ID. (required)
     * @param policy     (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateRetentionWithHttpInfo(Long id, RetentionPolicy policy, String xRequestId) throws RestClientException
    {
        Object postBody = policy;

        // verify the required parameter 'id' is set
        if (id == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling updateRetention");
        }

        // verify the required parameter 'policy' is set
        if (policy == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'policy' when calling updateRetention");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/retentions/{id}").buildAndExpand(uriVariables).toUriString();

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
