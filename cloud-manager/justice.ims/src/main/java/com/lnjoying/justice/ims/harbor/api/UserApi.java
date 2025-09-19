package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.OIDCCliSecretReq;
import com.lnjoying.justice.ims.harbor.model.PasswordReq;
import com.lnjoying.justice.ims.harbor.model.Permission;
import com.lnjoying.justice.ims.harbor.model.UserCreationReq;
import com.lnjoying.justice.ims.harbor.model.UserProfile;
import com.lnjoying.justice.ims.harbor.model.UserResp;
import com.lnjoying.justice.ims.harbor.model.UserSearchRespItem;
import com.lnjoying.justice.ims.harbor.model.UserSysAdminFlag;

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
@Component("com.lnjoying.justice.ims.harbor.api.UserApi")
public class UserApi
{
    private ApiClient apiClient;

    public UserApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public UserApi(ApiClient apiClient)
    {
        this.apiClient = apiClient;
    }

    public UserApi(String basePath, String username, String password)
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
     * Create a local user.
     * This API can be used only when the authentication mode is for local DB.  When self registration is disabled.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - When the  self registration is disabled, non-admin does not have permission to create user.  When self registration is enabled, this API can only be called from UI portal, calling it via script will get a 403 error.
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param userReq    The new user (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createUser(UserCreationReq userReq, String xRequestId) throws RestClientException
    {
        createUserWithHttpInfo(userReq, xRequestId);
    }

    /**
     * Create a local user.
     * This API can be used only when the authentication mode is for local DB.  When self registration is disabled.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - When the  self registration is disabled, non-admin does not have permission to create user.  When self registration is enabled, this API can only be called from UI portal, calling it via script will get a 403 error.
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param userReq    The new user (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createUserWithHttpInfo(UserCreationReq userReq, String xRequestId) throws RestClientException
    {
        Object postBody = userReq;

        // verify the required parameter 'userReq' is set
        if (userReq == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'userReq' when calling createUser");
        }

        String path = UriComponentsBuilder.fromPath("/users").build().toUriString();

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
     * Mark a registered user as be removed.
     * This endpoint let administrator of Harbor mark a registered user as removed.It actually won&#39;t be deleted from DB.
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param userId     User ID for marking as to be removed. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteUser(Integer userId, String xRequestId) throws RestClientException
    {
        deleteUserWithHttpInfo(userId, xRequestId);
    }

    /**
     * Mark a registered user as be removed.
     * This endpoint let administrator of Harbor mark a registered user as removed.It actually won&#39;t be deleted from DB.
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param userId     User ID for marking as to be removed. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteUserWithHttpInfo(Integer userId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'userId' is set
        if (userId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'userId' when calling deleteUser");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("user_id", userId);
        String path = UriComponentsBuilder.fromPath("/users/{user_id}").buildAndExpand(uriVariables).toUriString();

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
     * Get current user info.
     *
     * <p><b>200</b> - Get current user information successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return UserResp
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public UserResp getCurrentUserInfo(String xRequestId) throws RestClientException
    {
        return getCurrentUserInfoWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Get current user info.
     *
     * <p><b>200</b> - Get current user information successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;UserResp&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<UserResp> getCurrentUserInfoWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/users/current").build().toUriString();

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

        ParameterizedTypeReference<UserResp> returnType = new ParameterizedTypeReference<UserResp>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get current user permissions.
     *
     * <p><b>200</b> - Get current user permission successfully.
     * <p><b>401</b> - User need to log in first.
     * <p><b>500</b> - Internal errors.
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param scope      The scope for the permission (optional)
     * @param relative   If true, the resources in the response are relative to the scope, eg for resource &#39;/project/1/repository&#39; if relative is &#39;true&#39; then the resource in response will be &#39;repository&#39;.  (optional)
     * @return List&lt;Permission&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Permission> getCurrentUserPermissions(String xRequestId, String scope, Boolean relative) throws RestClientException
    {
        return getCurrentUserPermissionsWithHttpInfo(xRequestId, scope, relative).getBody();
    }

    /**
     * Get current user permissions.
     *
     * <p><b>200</b> - Get current user permission successfully.
     * <p><b>401</b> - User need to log in first.
     * <p><b>500</b> - Internal errors.
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param scope      The scope for the permission (optional)
     * @param relative   If true, the resources in the response are relative to the scope, eg for resource &#39;/project/1/repository&#39; if relative is &#39;true&#39; then the resource in response will be &#39;repository&#39;.  (optional)
     * @return ResponseEntity&lt;List&lt;Permission&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Permission>> getCurrentUserPermissionsWithHttpInfo(String xRequestId, String scope, Boolean relative) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/users/current/permissions").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "scope", scope));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "relative", relative));

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

        ParameterizedTypeReference<List<Permission>> returnType = new ParameterizedTypeReference<List<Permission>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get a user&#39;s profile.
     *
     * <p><b>200</b> - Get user&#39;s info successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param userId     (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return UserResp
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public UserResp getUser(Integer userId, String xRequestId) throws RestClientException
    {
        return getUserWithHttpInfo(userId, xRequestId).getBody();
    }

    /**
     * Get a user&#39;s profile.
     *
     * <p><b>200</b> - Get user&#39;s info successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param userId     (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;UserResp&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<UserResp> getUserWithHttpInfo(Integer userId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'userId' is set
        if (userId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'userId' when calling getUser");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("user_id", userId);
        String path = UriComponentsBuilder.fromPath("/users/{user_id}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<UserResp> returnType = new ParameterizedTypeReference<UserResp>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List users
     *
     * <p><b>200</b> - return the list of users.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @return List&lt;UserResp&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<UserResp> listUsers(String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        return listUsersWithHttpInfo(xRequestId, q, sort, page, pageSize).getBody();
    }

    /**
     * List users
     *
     * <p><b>200</b> - return the list of users.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @return ResponseEntity&lt;List&lt;UserResp&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<UserResp>> listUsersWithHttpInfo(String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/users").build().toUriString();

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

        ParameterizedTypeReference<List<UserResp>> returnType = new ParameterizedTypeReference<List<UserResp>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Search users by username
     * This endpoint is to search the users by username.  It&#39;s open for all authenticated requests.
     * <p><b>200</b> - Search users by username successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param username   Username for filtering results. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @return List&lt;UserSearchRespItem&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<UserSearchRespItem> searchUsers(String username, String xRequestId, Long page, Long pageSize) throws RestClientException
    {
        return searchUsersWithHttpInfo(username, xRequestId, page, pageSize).getBody();
    }

    /**
     * Search users by username
     * This endpoint is to search the users by username.  It&#39;s open for all authenticated requests.
     * <p><b>200</b> - Search users by username successfully.
     * <p><b>401</b> - Unauthorized
     * <p><b>500</b> - Internal server error
     *
     * @param username   Username for filtering results. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @return ResponseEntity&lt;List&lt;UserSearchRespItem&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<UserSearchRespItem>> searchUsersWithHttpInfo(String username, String xRequestId, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'username' is set
        if (username == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'username' when calling searchUsers");
        }

        String path = UriComponentsBuilder.fromPath("/users/search").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "username", username));

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

        ParameterizedTypeReference<List<UserSearchRespItem>> returnType = new ParameterizedTypeReference<List<UserSearchRespItem>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Set CLI secret for a user.
     * This endpoint let user generate a new CLI secret for himself.  This API only works when auth mode is set to &#39;OIDC&#39;. Once this API returns with successful status, the old secret will be invalid, as there will be only one CLI secret for a user.
     * <p><b>200</b> - The secret is successfully updated
     * <p><b>400</b> - Invalid user ID.  Or user is not onboarded via OIDC authentication. Or the secret does not meet the standard.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>412</b> - The auth mode of the system is not \&quot;oidc_auth\&quot;, or the user is not onboarded via OIDC AuthN.
     * <p><b>500</b> - Internal server error
     *
     * @param userId     User ID (required)
     * @param secret     (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void setCliSecret(Integer userId, OIDCCliSecretReq secret, String xRequestId) throws RestClientException
    {
        setCliSecretWithHttpInfo(userId, secret, xRequestId);
    }

    /**
     * Set CLI secret for a user.
     * This endpoint let user generate a new CLI secret for himself.  This API only works when auth mode is set to &#39;OIDC&#39;. Once this API returns with successful status, the old secret will be invalid, as there will be only one CLI secret for a user.
     * <p><b>200</b> - The secret is successfully updated
     * <p><b>400</b> - Invalid user ID.  Or user is not onboarded via OIDC authentication. Or the secret does not meet the standard.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>412</b> - The auth mode of the system is not \&quot;oidc_auth\&quot;, or the user is not onboarded via OIDC AuthN.
     * <p><b>500</b> - Internal server error
     *
     * @param userId     User ID (required)
     * @param secret     (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> setCliSecretWithHttpInfo(Integer userId, OIDCCliSecretReq secret, String xRequestId) throws RestClientException
    {
        Object postBody = secret;

        // verify the required parameter 'userId' is set
        if (userId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'userId' when calling setCliSecret");
        }

        // verify the required parameter 'secret' is set
        if (secret == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'secret' when calling setCliSecret");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("user_id", userId);
        String path = UriComponentsBuilder.fromPath("/users/{user_id}/cli_secret").buildAndExpand(uriVariables).toUriString();

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
     * Update a registered user to change to be an administrator of Harbor.
     *
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Unexpected internal errors.
     *
     * @param userId       (required)
     * @param sysadminFlag Toggle a user to admin or not. (required)
     * @param xRequestId   An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void setUserSysAdmin(Integer userId, UserSysAdminFlag sysadminFlag, String xRequestId) throws RestClientException
    {
        setUserSysAdminWithHttpInfo(userId, sysadminFlag, xRequestId);
    }

    /**
     * Update a registered user to change to be an administrator of Harbor.
     *
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Unexpected internal errors.
     *
     * @param userId       (required)
     * @param sysadminFlag Toggle a user to admin or not. (required)
     * @param xRequestId   An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> setUserSysAdminWithHttpInfo(Integer userId, UserSysAdminFlag sysadminFlag, String xRequestId) throws RestClientException
    {
        Object postBody = sysadminFlag;

        // verify the required parameter 'userId' is set
        if (userId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'userId' when calling setUserSysAdmin");
        }

        // verify the required parameter 'sysadminFlag' is set
        if (sysadminFlag == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'sysadminFlag' when calling setUserSysAdmin");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("user_id", userId);
        String path = UriComponentsBuilder.fromPath("/users/{user_id}/sysadmin").buildAndExpand(uriVariables).toUriString();

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
     * Change the password on a user that already exists.
     * This endpoint is for user to update password. Users with the admin role can change any user&#39;s password. Regular users can change only their own password.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Invalid user ID; Password does not meet requirement
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - The caller does not have permission to update the password of the user with given ID, or the old password in request body is not correct.
     * <p><b>500</b> - Internal server error
     *
     * @param userId     (required)
     * @param password   Password to be updated, the attribute &#39;old_password&#39; is optional when the API is called by the system administrator. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateUserPassword(Integer userId, PasswordReq password, String xRequestId) throws RestClientException
    {
        updateUserPasswordWithHttpInfo(userId, password, xRequestId);
    }

    /**
     * Change the password on a user that already exists.
     * This endpoint is for user to update password. Users with the admin role can change any user&#39;s password. Regular users can change only their own password.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Invalid user ID; Password does not meet requirement
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - The caller does not have permission to update the password of the user with given ID, or the old password in request body is not correct.
     * <p><b>500</b> - Internal server error
     *
     * @param userId     (required)
     * @param password   Password to be updated, the attribute &#39;old_password&#39; is optional when the API is called by the system administrator. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateUserPasswordWithHttpInfo(Integer userId, PasswordReq password, String xRequestId) throws RestClientException
    {
        Object postBody = password;

        // verify the required parameter 'userId' is set
        if (userId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'userId' when calling updateUserPassword");
        }

        // verify the required parameter 'password' is set
        if (password == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'password' when calling updateUserPassword");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("user_id", userId);
        String path = UriComponentsBuilder.fromPath("/users/{user_id}/password").buildAndExpand(uriVariables).toUriString();

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
     * Update user&#39;s profile.
     *
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param userId     Registered user ID (required)
     * @param profile    Only email, realname and comment can be modified. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateUserProfile(Integer userId, UserProfile profile, String xRequestId) throws RestClientException
    {
        updateUserProfileWithHttpInfo(userId, profile, xRequestId);
    }

    /**
     * Update user&#39;s profile.
     *
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param userId     Registered user ID (required)
     * @param profile    Only email, realname and comment can be modified. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateUserProfileWithHttpInfo(Integer userId, UserProfile profile, String xRequestId) throws RestClientException
    {
        Object postBody = profile;

        // verify the required parameter 'userId' is set
        if (userId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'userId' when calling updateUserProfile");
        }

        // verify the required parameter 'profile' is set
        if (profile == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'profile' when calling updateUserProfile");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("user_id", userId);
        String path = UriComponentsBuilder.fromPath("/users/{user_id}").buildAndExpand(uriVariables).toUriString();

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
