package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.SupportedWebhookEventTypes;
import com.lnjoying.justice.ims.harbor.model.WebhookLastTrigger;
import com.lnjoying.justice.ims.harbor.model.WebhookPolicy;

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
@Component("com.lnjoying.justice.ims.harbor.api.WebhookApi")
public class WebhookApi
{
    private ApiClient apiClient;

    public WebhookApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public WebhookApi(ApiClient apiClient)
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
     * Create project webhook policy.
     * This endpoint create a webhook policy if the project does not have one.
     * <p><b>201</b> - Project webhook policy create successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param policy          Properties \&quot;targets\&quot; and \&quot;event_types\&quot; needed. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createWebhookPolicyOfProject(String projectNameOrId, WebhookPolicy policy, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        createWebhookPolicyOfProjectWithHttpInfo(projectNameOrId, policy, xRequestId, xIsResourceName);
    }

    /**
     * Create project webhook policy.
     * This endpoint create a webhook policy if the project does not have one.
     * <p><b>201</b> - Project webhook policy create successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param policy          Properties \&quot;targets\&quot; and \&quot;event_types\&quot; needed. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createWebhookPolicyOfProjectWithHttpInfo(String projectNameOrId, WebhookPolicy policy, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = policy;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling createWebhookPolicyOfProject");
        }

        // verify the required parameter 'policy' is set
        if (policy == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'policy' when calling createWebhookPolicyOfProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/webhook/policies").buildAndExpand(uriVariables).toUriString();

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
     * Delete webhook policy of a project
     * This endpoint is aimed to delete webhookpolicy of a project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param webhookPolicyId The ID of the webhook policy (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteWebhookPolicyOfProject(String projectNameOrId, Long webhookPolicyId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        deleteWebhookPolicyOfProjectWithHttpInfo(projectNameOrId, webhookPolicyId, xRequestId, xIsResourceName);
    }

    /**
     * Delete webhook policy of a project
     * This endpoint is aimed to delete webhookpolicy of a project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param webhookPolicyId The ID of the webhook policy (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteWebhookPolicyOfProjectWithHttpInfo(String projectNameOrId, Long webhookPolicyId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling deleteWebhookPolicyOfProject");
        }

        // verify the required parameter 'webhookPolicyId' is set
        if (webhookPolicyId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'webhookPolicyId' when calling deleteWebhookPolicyOfProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        uriVariables.put("webhook_policy_id", webhookPolicyId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/webhook/policies/{webhook_policy_id}").buildAndExpand(uriVariables).toUriString();

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
     * Get supported event types and notify types.
     * Get supportted event types and notify types.
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return SupportedWebhookEventTypes
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SupportedWebhookEventTypes getSupportedEventTypes(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        return getSupportedEventTypesWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName).getBody();
    }

    /**
     * Get supported event types and notify types.
     * Get supportted event types and notify types.
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;SupportedWebhookEventTypes&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SupportedWebhookEventTypes> getSupportedEventTypesWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling getSupportedEventTypes");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/webhook/events").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<SupportedWebhookEventTypes> returnType = new ParameterizedTypeReference<SupportedWebhookEventTypes>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get project webhook policy
     * This endpoint returns specified webhook policy of a project.
     * <p><b>200</b> - Get webhook policy successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param webhookPolicyId The ID of the webhook policy (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return WebhookPolicy
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public WebhookPolicy getWebhookPolicyOfProject(String projectNameOrId, Long webhookPolicyId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        return getWebhookPolicyOfProjectWithHttpInfo(projectNameOrId, webhookPolicyId, xRequestId, xIsResourceName).getBody();
    }

    /**
     * Get project webhook policy
     * This endpoint returns specified webhook policy of a project.
     * <p><b>200</b> - Get webhook policy successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param webhookPolicyId The ID of the webhook policy (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;WebhookPolicy&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<WebhookPolicy> getWebhookPolicyOfProjectWithHttpInfo(String projectNameOrId, Long webhookPolicyId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling getWebhookPolicyOfProject");
        }

        // verify the required parameter 'webhookPolicyId' is set
        if (webhookPolicyId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'webhookPolicyId' when calling getWebhookPolicyOfProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        uriVariables.put("webhook_policy_id", webhookPolicyId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/webhook/policies/{webhook_policy_id}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<WebhookPolicy> returnType = new ParameterizedTypeReference<WebhookPolicy>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get project webhook policy last trigger info
     * This endpoint returns last trigger information of project webhook policy.
     * <p><b>200</b> - Test webhook connection successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return List&lt;WebhookLastTrigger&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<WebhookLastTrigger> lastTrigger(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        return lastTriggerWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName).getBody();
    }

    /**
     * Get project webhook policy last trigger info
     * This endpoint returns last trigger information of project webhook policy.
     * <p><b>200</b> - Test webhook connection successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;List&lt;WebhookLastTrigger&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<WebhookLastTrigger>> lastTriggerWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling lastTrigger");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/webhook/lasttrigger").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<List<WebhookLastTrigger>> returnType = new ParameterizedTypeReference<List<WebhookLastTrigger>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List project webhook policies.
     * This endpoint returns webhook policies of a project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param sort            Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param q               Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param page            The page number (optional, default to 1)
     * @param pageSize        The size of per page (optional, default to 10)
     * @return List&lt;WebhookPolicy&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<WebhookPolicy> listWebhookPoliciesOfProject(String projectNameOrId, String xRequestId, Boolean xIsResourceName, String sort, String q, Long page, Long pageSize) throws RestClientException
    {
        return listWebhookPoliciesOfProjectWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName, sort, q, page, pageSize).getBody();
    }

    /**
     * List project webhook policies.
     * This endpoint returns webhook policies of a project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param sort            Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param q               Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param page            The page number (optional, default to 1)
     * @param pageSize        The size of per page (optional, default to 10)
     * @return ResponseEntity&lt;List&lt;WebhookPolicy&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<WebhookPolicy>> listWebhookPoliciesOfProjectWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName, String sort, String q, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling listWebhookPoliciesOfProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/webhook/policies").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sort", sort));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
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

        ParameterizedTypeReference<List<WebhookPolicy>> returnType = new ParameterizedTypeReference<List<WebhookPolicy>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update webhook policy of a project.
     * This endpoint is aimed to update the webhook policy of a project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param webhookPolicyId The ID of the webhook policy (required)
     * @param policy          All properties needed except \&quot;id\&quot;, \&quot;project_id\&quot;, \&quot;creation_time\&quot;, \&quot;update_time\&quot;. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateWebhookPolicyOfProject(String projectNameOrId, Long webhookPolicyId, WebhookPolicy policy, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        updateWebhookPolicyOfProjectWithHttpInfo(projectNameOrId, webhookPolicyId, policy, xRequestId, xIsResourceName);
    }

    /**
     * Update webhook policy of a project.
     * This endpoint is aimed to update the webhook policy of a project.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param webhookPolicyId The ID of the webhook policy (required)
     * @param policy          All properties needed except \&quot;id\&quot;, \&quot;project_id\&quot;, \&quot;creation_time\&quot;, \&quot;update_time\&quot;. (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateWebhookPolicyOfProjectWithHttpInfo(String projectNameOrId, Long webhookPolicyId, WebhookPolicy policy, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = policy;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling updateWebhookPolicyOfProject");
        }

        // verify the required parameter 'webhookPolicyId' is set
        if (webhookPolicyId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'webhookPolicyId' when calling updateWebhookPolicyOfProject");
        }

        // verify the required parameter 'policy' is set
        if (policy == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'policy' when calling updateWebhookPolicyOfProject");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        uriVariables.put("webhook_policy_id", webhookPolicyId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/webhook/policies/{webhook_policy_id}").buildAndExpand(uriVariables).toUriString();

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
