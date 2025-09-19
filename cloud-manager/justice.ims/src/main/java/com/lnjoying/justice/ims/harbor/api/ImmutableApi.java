package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.ImmutableRule;

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
@Component("com.lnjoying.justice.ims.harbor.api.ImmutableApi")
public class ImmutableApi
{
    private ApiClient apiClient;

    public ImmutableApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public ImmutableApi(ApiClient apiClient)
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
     * Add an immutable tag rule to current project
     * This endpoint add an immutable tag rule to the project
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param immutableRule   (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createImmuRule(String projectNameOrId, ImmutableRule immutableRule, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        createImmuRuleWithHttpInfo(projectNameOrId, immutableRule, xRequestId, xIsResourceName);
    }

    /**
     * Add an immutable tag rule to current project
     * This endpoint add an immutable tag rule to the project
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param immutableRule   (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createImmuRuleWithHttpInfo(String projectNameOrId, ImmutableRule immutableRule, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = immutableRule;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling createImmuRule");
        }

        // verify the required parameter 'immutableRule' is set
        if (immutableRule == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'immutableRule' when calling createImmuRule");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/immutabletagrules").buildAndExpand(uriVariables).toUriString();

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
     * Delete the immutable tag rule.
     *
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param immutableRuleId The ID of the immutable rule (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteImmuRule(String projectNameOrId, Long immutableRuleId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        deleteImmuRuleWithHttpInfo(projectNameOrId, immutableRuleId, xRequestId, xIsResourceName);
    }

    /**
     * Delete the immutable tag rule.
     *
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param immutableRuleId The ID of the immutable rule (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteImmuRuleWithHttpInfo(String projectNameOrId, Long immutableRuleId, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling deleteImmuRule");
        }

        // verify the required parameter 'immutableRuleId' is set
        if (immutableRuleId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'immutableRuleId' when calling deleteImmuRule");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        uriVariables.put("immutable_rule_id", immutableRuleId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/immutabletagrules/{immutable_rule_id}").buildAndExpand(uriVariables).toUriString();

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
     * List all immutable tag rules of current project
     * This endpoint returns the immutable tag rules of a project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param page            The page number (optional, default to 1)
     * @param pageSize        The size of per page (optional, default to 10)
     * @param q               Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort            Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return List&lt;ImmutableRule&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<ImmutableRule> listImmuRules(String projectNameOrId, String xRequestId, Boolean xIsResourceName, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        return listImmuRulesWithHttpInfo(projectNameOrId, xRequestId, xIsResourceName, page, pageSize, q, sort).getBody();
    }

    /**
     * List all immutable tag rules of current project
     * This endpoint returns the immutable tag rules of a project
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @param page            The page number (optional, default to 1)
     * @param pageSize        The size of per page (optional, default to 10)
     * @param q               Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort            Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @return ResponseEntity&lt;List&lt;ImmutableRule&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<ImmutableRule>> listImmuRulesWithHttpInfo(String projectNameOrId, String xRequestId, Boolean xIsResourceName, Long page, Long pageSize, String q, String sort) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling listImmuRules");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/immutabletagrules").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sort", sort));

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

        ParameterizedTypeReference<List<ImmutableRule>> returnType = new ParameterizedTypeReference<List<ImmutableRule>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update the immutable tag rule or enable or disable the rule
     *
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param immutableRuleId The ID of the immutable rule (required)
     * @param immutableRule   (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateImmuRule(String projectNameOrId, Long immutableRuleId, ImmutableRule immutableRule, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        updateImmuRuleWithHttpInfo(projectNameOrId, immutableRuleId, immutableRule, xRequestId, xIsResourceName);
    }

    /**
     * Update the immutable tag rule or enable or disable the rule
     *
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param projectNameOrId The name or id of the project (required)
     * @param immutableRuleId The ID of the immutable rule (required)
     * @param immutableRule   (required)
     * @param xRequestId      An unique ID for the request (optional)
     * @param xIsResourceName The flag to indicate whether the parameter which supports both name and id in the path is the name of the resource. When the X-Is-Resource-Name is false and the parameter can be converted to an integer, the parameter will be as an id, otherwise, it will be as a name. (optional, default to false)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateImmuRuleWithHttpInfo(String projectNameOrId, Long immutableRuleId, ImmutableRule immutableRule, String xRequestId, Boolean xIsResourceName) throws RestClientException
    {
        Object postBody = immutableRule;

        // verify the required parameter 'projectNameOrId' is set
        if (projectNameOrId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectNameOrId' when calling updateImmuRule");
        }

        // verify the required parameter 'immutableRuleId' is set
        if (immutableRuleId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'immutableRuleId' when calling updateImmuRule");
        }

        // verify the required parameter 'immutableRule' is set
        if (immutableRule == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'immutableRule' when calling updateImmuRule");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name_or_id", projectNameOrId);
        uriVariables.put("immutable_rule_id", immutableRuleId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name_or_id}/immutabletagrules/{immutable_rule_id}").buildAndExpand(uriVariables).toUriString();

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
