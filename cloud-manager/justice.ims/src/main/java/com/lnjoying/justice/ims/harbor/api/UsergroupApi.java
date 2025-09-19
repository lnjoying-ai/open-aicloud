package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.UserGroup;

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
@Component("com.lnjoying.justice.ims.harbor.api.UsergroupApi")
public class UsergroupApi
{
    private ApiClient apiClient;

    public UsergroupApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public UsergroupApi(ApiClient apiClient)
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
     * Create user group
     * Create user group information
     * <p><b>201</b> - User group created successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param usergroup  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createUserGroup(String xRequestId, UserGroup usergroup) throws RestClientException
    {
        createUserGroupWithHttpInfo(xRequestId, usergroup);
    }

    /**
     * Create user group
     * Create user group information
     * <p><b>201</b> - User group created successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param usergroup  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createUserGroupWithHttpInfo(String xRequestId, UserGroup usergroup) throws RestClientException
    {
        Object postBody = usergroup;

        String path = UriComponentsBuilder.fromPath("/usergroups").build().toUriString();

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
     * Delete user group
     * Delete user group
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param groupId    (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteUserGroup(Integer groupId, String xRequestId) throws RestClientException
    {
        deleteUserGroupWithHttpInfo(groupId, xRequestId);
    }

    /**
     * Delete user group
     * Delete user group
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param groupId    (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteUserGroupWithHttpInfo(Integer groupId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'groupId' is set
        if (groupId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupId' when calling deleteUserGroup");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("group_id", groupId);
        String path = UriComponentsBuilder.fromPath("/usergroups/{group_id}").buildAndExpand(uriVariables).toUriString();

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
     * Get user group information
     * Get user group information
     * <p><b>200</b> - User group get successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param groupId    Group ID (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return UserGroup
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public UserGroup getUserGroup(Long groupId, String xRequestId) throws RestClientException
    {
        return getUserGroupWithHttpInfo(groupId, xRequestId).getBody();
    }

    /**
     * Get user group information
     * Get user group information
     * <p><b>200</b> - User group get successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param groupId    Group ID (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;UserGroup&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<UserGroup> getUserGroupWithHttpInfo(Long groupId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'groupId' is set
        if (groupId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupId' when calling getUserGroup");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("group_id", groupId);
        String path = UriComponentsBuilder.fromPath("/usergroups/{group_id}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<UserGroup> returnType = new ParameterizedTypeReference<UserGroup>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get all user groups information
     * Get all user groups information
     * <p><b>200</b> - Get user group successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return List&lt;UserGroup&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<UserGroup> listUserGroups(String xRequestId) throws RestClientException
    {
        return listUserGroupsWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Get all user groups information
     * Get all user groups information
     * <p><b>200</b> - Get user group successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;List&lt;UserGroup&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<UserGroup>> listUserGroupsWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/usergroups").build().toUriString();

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

        ParameterizedTypeReference<List<UserGroup>> returnType = new ParameterizedTypeReference<List<UserGroup>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update group information
     * Update user group information
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param groupId    Group ID (required)
     * @param xRequestId An unique ID for the request (optional)
     * @param usergroup  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateUserGroup(Long groupId, String xRequestId, UserGroup usergroup) throws RestClientException
    {
        updateUserGroupWithHttpInfo(groupId, xRequestId, usergroup);
    }

    /**
     * Update group information
     * Update user group information
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param groupId    Group ID (required)
     * @param xRequestId An unique ID for the request (optional)
     * @param usergroup  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateUserGroupWithHttpInfo(Long groupId, String xRequestId, UserGroup usergroup) throws RestClientException
    {
        Object postBody = usergroup;

        // verify the required parameter 'groupId' is set
        if (groupId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupId' when calling updateUserGroup");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("group_id", groupId);
        String path = UriComponentsBuilder.fromPath("/usergroups/{group_id}").buildAndExpand(uriVariables).toUriString();

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
