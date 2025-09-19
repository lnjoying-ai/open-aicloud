package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.LdapConf;
import com.lnjoying.justice.ims.harbor.model.LdapImportUsers;
import com.lnjoying.justice.ims.harbor.model.LdapPingResult;
import com.lnjoying.justice.ims.harbor.model.LdapUser;
import com.lnjoying.justice.ims.harbor.model.UserGroup;

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
@Component("com.lnjoying.justice.ims.harbor.api.LdapApi")
public class LdapApi
{
    private ApiClient apiClient;

    public LdapApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public LdapApi(ApiClient apiClient)
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
     * Import selected available ldap users.
     * This endpoint adds the selected available ldap users to harbor based on related configuration parameters from the system. System will try to guess the user email address and realname, add to harbor user information. If have errors when import user, will return the list of importing failed uid and the failed reason.
     * <p><b>200</b> - Add ldap users successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Failed import some users.
     * <p><b>500</b> - Internal server error
     *
     * @param uidList    The uid listed for importing. This list will check users validity of ldap service based on configuration from the system. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void importLdapUser(LdapImportUsers uidList, String xRequestId) throws RestClientException
    {
        importLdapUserWithHttpInfo(uidList, xRequestId);
    }

    /**
     * Import selected available ldap users.
     * This endpoint adds the selected available ldap users to harbor based on related configuration parameters from the system. System will try to guess the user email address and realname, add to harbor user information. If have errors when import user, will return the list of importing failed uid and the failed reason.
     * <p><b>200</b> - Add ldap users successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Failed import some users.
     * <p><b>500</b> - Internal server error
     *
     * @param uidList    The uid listed for importing. This list will check users validity of ldap service based on configuration from the system. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> importLdapUserWithHttpInfo(LdapImportUsers uidList, String xRequestId) throws RestClientException
    {
        Object postBody = uidList;

        // verify the required parameter 'uidList' is set
        if (uidList == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'uidList' when calling importLdapUser");
        }

        String path = UriComponentsBuilder.fromPath("/ldap/users/import").build().toUriString();

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
     * Ping available ldap service.
     * This endpoint ping the available ldap service for test related configuration parameters.
     * <p><b>200</b> - Ping ldap service successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param ldapconf   ldap configuration. support input ldap service configuration. If it is a empty request, will load current configuration from the system. (optional)
     * @return LdapPingResult
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public LdapPingResult pingLdap(String xRequestId, LdapConf ldapconf) throws RestClientException
    {
        return pingLdapWithHttpInfo(xRequestId, ldapconf).getBody();
    }

    /**
     * Ping available ldap service.
     * This endpoint ping the available ldap service for test related configuration parameters.
     * <p><b>200</b> - Ping ldap service successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param ldapconf   ldap configuration. support input ldap service configuration. If it is a empty request, will load current configuration from the system. (optional)
     * @return ResponseEntity&lt;LdapPingResult&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<LdapPingResult> pingLdapWithHttpInfo(String xRequestId, LdapConf ldapconf) throws RestClientException
    {
        Object postBody = ldapconf;

        String path = UriComponentsBuilder.fromPath("/ldap/ping").build().toUriString();

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

        ParameterizedTypeReference<LdapPingResult> returnType = new ParameterizedTypeReference<LdapPingResult>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Search available ldap groups.
     * This endpoint searches the available ldap groups based on related configuration parameters. support to search by groupname or groupdn.
     * <p><b>200</b> - Search ldap group successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param groupname  Ldap group name (optional)
     * @param groupdn    The LDAP group DN (optional)
     * @return List&lt;UserGroup&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<UserGroup> searchLdapGroup(String xRequestId, String groupname, String groupdn) throws RestClientException
    {
        return searchLdapGroupWithHttpInfo(xRequestId, groupname, groupdn).getBody();
    }

    /**
     * Search available ldap groups.
     * This endpoint searches the available ldap groups based on related configuration parameters. support to search by groupname or groupdn.
     * <p><b>200</b> - Search ldap group successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param groupname  Ldap group name (optional)
     * @param groupdn    The LDAP group DN (optional)
     * @return ResponseEntity&lt;List&lt;UserGroup&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<UserGroup>> searchLdapGroupWithHttpInfo(String xRequestId, String groupname, String groupdn) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/ldap/groups/search").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "groupname", groupname));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "groupdn", groupdn));

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
     * Search available ldap users.
     * This endpoint searches the available ldap users based on related configuration parameters. Support searched by input ladp configuration, load configuration from the system and specific filter.
     * <p><b>200</b> - Search ldap users successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param username   Registered user ID (optional)
     * @return List&lt;LdapUser&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<LdapUser> searchLdapUser(String xRequestId, String username) throws RestClientException
    {
        return searchLdapUserWithHttpInfo(xRequestId, username).getBody();
    }

    /**
     * Search available ldap users.
     * This endpoint searches the available ldap users based on related configuration parameters. Support searched by input ladp configuration, load configuration from the system and specific filter.
     * <p><b>200</b> - Search ldap users successfully.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param username   Registered user ID (optional)
     * @return ResponseEntity&lt;List&lt;LdapUser&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<LdapUser>> searchLdapUserWithHttpInfo(String xRequestId, String username) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/ldap/users/search").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

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

        ParameterizedTypeReference<List<LdapUser>> returnType = new ParameterizedTypeReference<List<LdapUser>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
