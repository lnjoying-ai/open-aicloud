package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Execution;
import com.lnjoying.justice.ims.harbor.model.Instance;
import com.lnjoying.justice.ims.harbor.model.Metadata;
import com.lnjoying.justice.ims.harbor.model.PreheatPolicy;
import com.lnjoying.justice.ims.harbor.model.ProviderUnderProject;
import com.lnjoying.justice.ims.harbor.model.Task;

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
@Component("com.lnjoying.justice.ims.harbor.api.PreheatApi")
public class PreheatApi
{
    private ApiClient apiClient;

    public PreheatApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public PreheatApi(ApiClient apiClient)
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
     * Create p2p provider instances
     * Create p2p provider instances
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param instance   The JSON object of instance. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createInstance(Instance instance, String xRequestId) throws RestClientException
    {
        createInstanceWithHttpInfo(instance, xRequestId);
    }

    /**
     * Create p2p provider instances
     * Create p2p provider instances
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param instance   The JSON object of instance. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createInstanceWithHttpInfo(Instance instance, String xRequestId) throws RestClientException
    {
        Object postBody = instance;

        // verify the required parameter 'instance' is set
        if (instance == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'instance' when calling createInstance");
        }

        String path = UriComponentsBuilder.fromPath("/p2p/preheat/instances").build().toUriString();

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
     * Create a preheat policy under a project
     * Create a preheat policy under a project
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param policy      The policy schema info (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createPolicy(String projectName, PreheatPolicy policy, String xRequestId) throws RestClientException
    {
        createPolicyWithHttpInfo(projectName, policy, xRequestId);
    }

    /**
     * Create a preheat policy under a project
     * Create a preheat policy under a project
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param policy      The policy schema info (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createPolicyWithHttpInfo(String projectName, PreheatPolicy policy, String xRequestId) throws RestClientException
    {
        Object postBody = policy;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling createPolicy");
        }

        // verify the required parameter 'policy' is set
        if (policy == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'policy' when calling createPolicy");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies").buildAndExpand(uriVariables).toUriString();

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
     * Delete the specified P2P provider instance
     * Delete the specified P2P provider instance
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param preheatInstanceName Instance Name (required)
     * @param xRequestId          An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteInstance(String preheatInstanceName, String xRequestId) throws RestClientException
    {
        deleteInstanceWithHttpInfo(preheatInstanceName, xRequestId);
    }

    /**
     * Delete the specified P2P provider instance
     * Delete the specified P2P provider instance
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param preheatInstanceName Instance Name (required)
     * @param xRequestId          An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteInstanceWithHttpInfo(String preheatInstanceName, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'preheatInstanceName' is set
        if (preheatInstanceName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatInstanceName' when calling deleteInstance");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("preheat_instance_name", preheatInstanceName);
        String path = UriComponentsBuilder.fromPath("/p2p/preheat/instances/{preheat_instance_name}").buildAndExpand(uriVariables).toUriString();

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
     * Delete a preheat policy
     * Delete a preheat policy
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deletePolicy(String projectName, String preheatPolicyName, String xRequestId) throws RestClientException
    {
        deletePolicyWithHttpInfo(projectName, preheatPolicyName, xRequestId);
    }

    /**
     * Delete a preheat policy
     * Delete a preheat policy
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deletePolicyWithHttpInfo(String projectName, String preheatPolicyName, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling deletePolicy");
        }

        // verify the required parameter 'preheatPolicyName' is set
        if (preheatPolicyName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatPolicyName' when calling deletePolicy");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("preheat_policy_name", preheatPolicyName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies/{preheat_policy_name}").buildAndExpand(uriVariables).toUriString();

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
     * Get a execution detail by id
     * Get a execution detail by id
     * <p><b>200</b> - Get execution success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param executionId       Execution ID (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return Execution
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Execution getExecution(String projectName, String preheatPolicyName, Integer executionId, String xRequestId) throws RestClientException
    {
        return getExecutionWithHttpInfo(projectName, preheatPolicyName, executionId, xRequestId).getBody();
    }

    /**
     * Get a execution detail by id
     * Get a execution detail by id
     * <p><b>200</b> - Get execution success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param executionId       Execution ID (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return ResponseEntity&lt;Execution&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Execution> getExecutionWithHttpInfo(String projectName, String preheatPolicyName, Integer executionId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling getExecution");
        }

        // verify the required parameter 'preheatPolicyName' is set
        if (preheatPolicyName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatPolicyName' when calling getExecution");
        }

        // verify the required parameter 'executionId' is set
        if (executionId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'executionId' when calling getExecution");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("preheat_policy_name", preheatPolicyName);
        uriVariables.put("execution_id", executionId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies/{preheat_policy_name}/executions/{execution_id}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<Execution> returnType = new ParameterizedTypeReference<Execution>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get a P2P provider instance
     * Get a P2P provider instance
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param preheatInstanceName Instance Name (required)
     * @param xRequestId          An unique ID for the request (optional)
     * @return Instance
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Instance getInstance(String preheatInstanceName, String xRequestId) throws RestClientException
    {
        return getInstanceWithHttpInfo(preheatInstanceName, xRequestId).getBody();
    }

    /**
     * Get a P2P provider instance
     * Get a P2P provider instance
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param preheatInstanceName Instance Name (required)
     * @param xRequestId          An unique ID for the request (optional)
     * @return ResponseEntity&lt;Instance&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Instance> getInstanceWithHttpInfo(String preheatInstanceName, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'preheatInstanceName' is set
        if (preheatInstanceName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatInstanceName' when calling getInstance");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("preheat_instance_name", preheatInstanceName);
        String path = UriComponentsBuilder.fromPath("/p2p/preheat/instances/{preheat_instance_name}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<Instance> returnType = new ParameterizedTypeReference<Instance>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get a preheat policy
     * Get a preheat policy
     * <p><b>200</b> - Get a preheat policy success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return PreheatPolicy
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PreheatPolicy getPolicy(String projectName, String preheatPolicyName, String xRequestId) throws RestClientException
    {
        return getPolicyWithHttpInfo(projectName, preheatPolicyName, xRequestId).getBody();
    }

    /**
     * Get a preheat policy
     * Get a preheat policy
     * <p><b>200</b> - Get a preheat policy success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return ResponseEntity&lt;PreheatPolicy&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PreheatPolicy> getPolicyWithHttpInfo(String projectName, String preheatPolicyName, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling getPolicy");
        }

        // verify the required parameter 'preheatPolicyName' is set
        if (preheatPolicyName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatPolicyName' when calling getPolicy");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("preheat_policy_name", preheatPolicyName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies/{preheat_policy_name}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<PreheatPolicy> returnType = new ParameterizedTypeReference<PreheatPolicy>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get the log text stream of the specified task for the given execution
     * Get the log text stream of the specified task for the given execution
     * <p><b>200</b> - Get log success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param executionId       Execution ID (required)
     * @param taskId            Task ID (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String getPreheatLog(String projectName, String preheatPolicyName, Integer executionId, Integer taskId, String xRequestId) throws RestClientException
    {
        return getPreheatLogWithHttpInfo(projectName, preheatPolicyName, executionId, taskId, xRequestId).getBody();
    }

    /**
     * Get the log text stream of the specified task for the given execution
     * Get the log text stream of the specified task for the given execution
     * <p><b>200</b> - Get log success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param executionId       Execution ID (required)
     * @param taskId            Task ID (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return ResponseEntity&lt;String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<String> getPreheatLogWithHttpInfo(String projectName, String preheatPolicyName, Integer executionId, Integer taskId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling getPreheatLog");
        }

        // verify the required parameter 'preheatPolicyName' is set
        if (preheatPolicyName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatPolicyName' when calling getPreheatLog");
        }

        // verify the required parameter 'executionId' is set
        if (executionId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'executionId' when calling getPreheatLog");
        }

        // verify the required parameter 'taskId' is set
        if (taskId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'taskId' when calling getPreheatLog");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("preheat_policy_name", preheatPolicyName);
        uriVariables.put("execution_id", executionId);
        uriVariables.put("task_id", taskId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies/{preheat_policy_name}/executions/{execution_id}/tasks/{task_id}/logs").buildAndExpand(uriVariables).toUriString();

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
     * List executions for the given policy
     * List executions for the given policy
     * <p><b>200</b> - List executions success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @param page              The page number (optional, default to 1)
     * @param pageSize          The size of per page (optional, default to 10)
     * @param q                 Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort              Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return List&lt;Execution&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Execution> listExecutions(String projectName, String preheatPolicyName, String xRequestId, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        return listExecutionsWithHttpInfo(projectName, preheatPolicyName, xRequestId, page, pageSize, q, sort).getBody();
    }

    /**
     * List executions for the given policy
     * List executions for the given policy
     * <p><b>200</b> - List executions success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @param page              The page number (optional, default to 1)
     * @param pageSize          The size of per page (optional, default to 10)
     * @param q                 Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort              Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return ResponseEntity&lt;List&lt;Execution&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Execution>> listExecutionsWithHttpInfo(String projectName, String preheatPolicyName, String xRequestId, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling listExecutions");
        }

        // verify the required parameter 'preheatPolicyName' is set
        if (preheatPolicyName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatPolicyName' when calling listExecutions");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("preheat_policy_name", preheatPolicyName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies/{preheat_policy_name}/executions").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
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

        ParameterizedTypeReference<List<Execution>> returnType = new ParameterizedTypeReference<List<Execution>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List P2P provider instances
     * List P2P provider instances
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return List&lt;Instance&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Instance> listInstances(String xRequestId, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        return listInstancesWithHttpInfo(xRequestId, page, pageSize, q, sort).getBody();
    }

    /**
     * List P2P provider instances
     * List P2P provider instances
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return ResponseEntity&lt;List&lt;Instance&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Instance>> listInstancesWithHttpInfo(String xRequestId, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/p2p/preheat/instances").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
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

        ParameterizedTypeReference<List<Instance>> returnType = new ParameterizedTypeReference<List<Instance>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List preheat policies
     * List preheat policies
     * <p><b>200</b> - List preheat policies success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @param page        The page number (optional, default to 1)
     * @param pageSize    The size of per page (optional, default to 10)
     * @param q           Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort        Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return List&lt;PreheatPolicy&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<PreheatPolicy> listPolicies(String projectName, String xRequestId, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        return listPoliciesWithHttpInfo(projectName, xRequestId, page, pageSize, q, sort).getBody();
    }

    /**
     * List preheat policies
     * List preheat policies
     * <p><b>200</b> - List preheat policies success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @param page        The page number (optional, default to 1)
     * @param pageSize    The size of per page (optional, default to 10)
     * @param q           Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort        Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return ResponseEntity&lt;List&lt;PreheatPolicy&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<PreheatPolicy>> listPoliciesWithHttpInfo(String projectName, String xRequestId, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling listPolicies");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
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

        ParameterizedTypeReference<List<PreheatPolicy>> returnType = new ParameterizedTypeReference<List<PreheatPolicy>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List P2P providers
     * List P2P providers
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return List&lt;Metadata&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Metadata> listProviders(String xRequestId) throws RestClientException
    {
        return listProvidersWithHttpInfo(xRequestId).getBody();
    }

    /**
     * List P2P providers
     * List P2P providers
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;List&lt;Metadata&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Metadata>> listProvidersWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/p2p/preheat/providers").build().toUriString();

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

        ParameterizedTypeReference<List<Metadata>> returnType = new ParameterizedTypeReference<List<Metadata>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get all providers at project level
     * Get all providers at project level
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @return List&lt;ProviderUnderProject&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<ProviderUnderProject> listProvidersUnderProject(String projectName, String xRequestId) throws RestClientException
    {
        return listProvidersUnderProjectWithHttpInfo(projectName, xRequestId).getBody();
    }

    /**
     * Get all providers at project level
     * Get all providers at project level
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName The name of the project (required)
     * @param xRequestId  An unique ID for the request (optional)
     * @return ResponseEntity&lt;List&lt;ProviderUnderProject&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<ProviderUnderProject>> listProvidersUnderProjectWithHttpInfo(String projectName, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling listProvidersUnderProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/providers").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<List<ProviderUnderProject>> returnType = new ParameterizedTypeReference<List<ProviderUnderProject>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List all the related tasks for the given execution
     * List all the related tasks for the given execution
     * <p><b>200</b> - List tasks success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param executionId       Execution ID (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @param page              The page number (optional, default to 1)
     * @param pageSize          The size of per page (optional, default to 10)
     * @param q                 Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort              Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return List&lt;Task&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Task> listTasks(String projectName, String preheatPolicyName, Integer executionId, String xRequestId, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        return listTasksWithHttpInfo(projectName, preheatPolicyName, executionId, xRequestId, page, pageSize, q, sort).getBody();
    }

    /**
     * List all the related tasks for the given execution
     * List all the related tasks for the given execution
     * <p><b>200</b> - List tasks success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param executionId       Execution ID (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @param page              The page number (optional, default to 1)
     * @param pageSize          The size of per page (optional, default to 10)
     * @param q                 Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort              Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return ResponseEntity&lt;List&lt;Task&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Task>> listTasksWithHttpInfo(String projectName, String preheatPolicyName, Integer executionId, String xRequestId, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling listTasks");
        }

        // verify the required parameter 'preheatPolicyName' is set
        if (preheatPolicyName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatPolicyName' when calling listTasks");
        }

        // verify the required parameter 'executionId' is set
        if (executionId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'executionId' when calling listTasks");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("preheat_policy_name", preheatPolicyName);
        uriVariables.put("execution_id", executionId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies/{preheat_policy_name}/executions/{execution_id}/tasks").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
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

        ParameterizedTypeReference<List<Task>> returnType = new ParameterizedTypeReference<List<Task>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Manual preheat
     * Manual preheat
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param policy            The policy schema info (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void manualPreheat(String projectName, String preheatPolicyName, PreheatPolicy policy, String xRequestId) throws RestClientException
    {
        manualPreheatWithHttpInfo(projectName, preheatPolicyName, policy, xRequestId);
    }

    /**
     * Manual preheat
     * Manual preheat
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param policy            The policy schema info (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> manualPreheatWithHttpInfo(String projectName, String preheatPolicyName, PreheatPolicy policy, String xRequestId) throws RestClientException
    {
        Object postBody = policy;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling manualPreheat");
        }

        // verify the required parameter 'preheatPolicyName' is set
        if (preheatPolicyName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatPolicyName' when calling manualPreheat");
        }

        // verify the required parameter 'policy' is set
        if (policy == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'policy' when calling manualPreheat");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("preheat_policy_name", preheatPolicyName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies/{preheat_policy_name}").buildAndExpand(uriVariables).toUriString();

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
     * Ping status of a instance.
     * This endpoint checks status of a instance, the instance can be given by ID or Endpoint URL (together with credential)
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Instance not found (when instance is provided by ID).
     * <p><b>500</b> - Internal server error
     *
     * @param instance   The JSON object of instance. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void pingInstances(Instance instance, String xRequestId) throws RestClientException
    {
        pingInstancesWithHttpInfo(instance, xRequestId);
    }

    /**
     * Ping status of a instance.
     * This endpoint checks status of a instance, the instance can be given by ID or Endpoint URL (together with credential)
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Instance not found (when instance is provided by ID).
     * <p><b>500</b> - Internal server error
     *
     * @param instance   The JSON object of instance. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> pingInstancesWithHttpInfo(Instance instance, String xRequestId) throws RestClientException
    {
        Object postBody = instance;

        // verify the required parameter 'instance' is set
        if (instance == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'instance' when calling pingInstances");
        }

        String path = UriComponentsBuilder.fromPath("/p2p/preheat/instances/ping").build().toUriString();

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
     * Stop a execution
     * Stop a execution
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param executionId       Execution ID (required)
     * @param execution         The data of execution (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void stopExecution(String projectName, String preheatPolicyName, Integer executionId, Execution execution, String xRequestId) throws RestClientException
    {
        stopExecutionWithHttpInfo(projectName, preheatPolicyName, executionId, execution, xRequestId);
    }

    /**
     * Stop a execution
     * Stop a execution
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param executionId       Execution ID (required)
     * @param execution         The data of execution (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> stopExecutionWithHttpInfo(String projectName, String preheatPolicyName, Integer executionId, Execution execution, String xRequestId) throws RestClientException
    {
        Object postBody = execution;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling stopExecution");
        }

        // verify the required parameter 'preheatPolicyName' is set
        if (preheatPolicyName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatPolicyName' when calling stopExecution");
        }

        // verify the required parameter 'executionId' is set
        if (executionId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'executionId' when calling stopExecution");
        }

        // verify the required parameter 'execution' is set
        if (execution == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'execution' when calling stopExecution");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("preheat_policy_name", preheatPolicyName);
        uriVariables.put("execution_id", executionId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies/{preheat_policy_name}/executions/{execution_id}").buildAndExpand(uriVariables).toUriString();

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
     * Update the specified P2P provider instance
     * Update the specified P2P provider instance
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param preheatInstanceName Instance Name (required)
     * @param instance            The instance to update (required)
     * @param xRequestId          An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateInstance(String preheatInstanceName, Instance instance, String xRequestId) throws RestClientException
    {
        updateInstanceWithHttpInfo(preheatInstanceName, instance, xRequestId);
    }

    /**
     * Update the specified P2P provider instance
     * Update the specified P2P provider instance
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param preheatInstanceName Instance Name (required)
     * @param instance            The instance to update (required)
     * @param xRequestId          An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateInstanceWithHttpInfo(String preheatInstanceName, Instance instance, String xRequestId) throws RestClientException
    {
        Object postBody = instance;

        // verify the required parameter 'preheatInstanceName' is set
        if (preheatInstanceName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatInstanceName' when calling updateInstance");
        }

        // verify the required parameter 'instance' is set
        if (instance == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'instance' when calling updateInstance");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("preheat_instance_name", preheatInstanceName);
        String path = UriComponentsBuilder.fromPath("/p2p/preheat/instances/{preheat_instance_name}").buildAndExpand(uriVariables).toUriString();

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

    /**
     * Update preheat policy
     * Update preheat policy
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param policy            The policy schema info (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updatePolicy(String projectName, String preheatPolicyName, PreheatPolicy policy, String xRequestId) throws RestClientException
    {
        updatePolicyWithHttpInfo(projectName, preheatPolicyName, policy, xRequestId);
    }

    /**
     * Update preheat policy
     * Update preheat policy
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName       The name of the project (required)
     * @param preheatPolicyName Preheat Policy Name (required)
     * @param policy            The policy schema info (required)
     * @param xRequestId        An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updatePolicyWithHttpInfo(String projectName, String preheatPolicyName, PreheatPolicy policy, String xRequestId) throws RestClientException
    {
        Object postBody = policy;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling updatePolicy");
        }

        // verify the required parameter 'preheatPolicyName' is set
        if (preheatPolicyName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'preheatPolicyName' when calling updatePolicy");
        }

        // verify the required parameter 'policy' is set
        if (policy == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'policy' when calling updatePolicy");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("preheat_policy_name", preheatPolicyName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/preheat/policies/{preheat_policy_name}").buildAndExpand(uriVariables).toUriString();

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
