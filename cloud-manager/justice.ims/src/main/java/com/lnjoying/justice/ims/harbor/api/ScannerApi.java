package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.IsDefault;
import com.lnjoying.justice.ims.harbor.model.ScannerAdapterMetadata;
import com.lnjoying.justice.ims.harbor.model.ScannerRegistration;
import com.lnjoying.justice.ims.harbor.model.ScannerRegistrationReq;
import com.lnjoying.justice.ims.harbor.model.ScannerRegistrationSettings;

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
@Component("com.lnjoying.justice.ims.harbor.api.ScannerApi")
public class ScannerApi
{
    private ApiClient apiClient;

    public ScannerApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public ScannerApi(ApiClient apiClient)
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
     * Create a scanner registration
     * Creats a new scanner registration with the given data.
     * <p><b>201</b> - Created successfully
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param registration A scanner registration to be created. (required)
     * @param xRequestId   An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createScanner(ScannerRegistrationReq registration, String xRequestId) throws RestClientException
    {
        createScannerWithHttpInfo(registration, xRequestId);
    }

    /**
     * Create a scanner registration
     * Creats a new scanner registration with the given data.
     * <p><b>201</b> - Created successfully
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param registration A scanner registration to be created. (required)
     * @param xRequestId   An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createScannerWithHttpInfo(ScannerRegistrationReq registration, String xRequestId) throws RestClientException
    {
        Object postBody = registration;

        // verify the required parameter 'registration' is set
        if (registration == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'registration' when calling createScanner");
        }

        String path = UriComponentsBuilder.fromPath("/scanners").build().toUriString();

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
     * Delete a scanner registration
     * Deletes the specified scanner registration.
     * <p><b>200</b> - Deleted successfully and return the deleted registration
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifier. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ScannerRegistration
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ScannerRegistration deleteScanner(String registrationId, String xRequestId) throws RestClientException
    {
        return deleteScannerWithHttpInfo(registrationId, xRequestId).getBody();
    }

    /**
     * Delete a scanner registration
     * Deletes the specified scanner registration.
     * <p><b>200</b> - Deleted successfully and return the deleted registration
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifier. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;ScannerRegistration&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ScannerRegistration> deleteScannerWithHttpInfo(String registrationId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'registrationId' is set
        if (registrationId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'registrationId' when calling deleteScanner");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("registration_id", registrationId);
        String path = UriComponentsBuilder.fromPath("/scanners/{registration_id}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<ScannerRegistration> returnType = new ParameterizedTypeReference<ScannerRegistration>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get a scanner registration details
     * Retruns the details of the specified scanner registration.
     * <p><b>200</b> - The details of the scanner registration.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifer. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ScannerRegistration
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ScannerRegistration getScanner(String registrationId, String xRequestId) throws RestClientException
    {
        return getScannerWithHttpInfo(registrationId, xRequestId).getBody();
    }

    /**
     * Get a scanner registration details
     * Retruns the details of the specified scanner registration.
     * <p><b>200</b> - The details of the scanner registration.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifer. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;ScannerRegistration&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ScannerRegistration> getScannerWithHttpInfo(String registrationId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'registrationId' is set
        if (registrationId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'registrationId' when calling getScanner");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("registration_id", registrationId);
        String path = UriComponentsBuilder.fromPath("/scanners/{registration_id}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<ScannerRegistration> returnType = new ParameterizedTypeReference<ScannerRegistration>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get the metadata of the specified scanner registration
     * Get the metadata of the specified scanner registration, including the capabilities and customized properties.
     * <p><b>200</b> - The metadata of the specified scanner adapter
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifier. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ScannerAdapterMetadata
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ScannerAdapterMetadata getScannerMetadata(String registrationId, String xRequestId) throws RestClientException
    {
        return getScannerMetadataWithHttpInfo(registrationId, xRequestId).getBody();
    }

    /**
     * Get the metadata of the specified scanner registration
     * Get the metadata of the specified scanner registration, including the capabilities and customized properties.
     * <p><b>200</b> - The metadata of the specified scanner adapter
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifier. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;ScannerAdapterMetadata&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ScannerAdapterMetadata> getScannerMetadataWithHttpInfo(String registrationId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'registrationId' is set
        if (registrationId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'registrationId' when calling getScannerMetadata");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("registration_id", registrationId);
        String path = UriComponentsBuilder.fromPath("/scanners/{registration_id}/metadata").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<ScannerAdapterMetadata> returnType = new ParameterizedTypeReference<ScannerAdapterMetadata>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List scanner registrations
     * Returns a list of currently configured scanner registrations.
     * <p><b>200</b> - A list of scanner registrations.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @return List&lt;ScannerRegistration&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<ScannerRegistration> listScanners(String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        return listScannersWithHttpInfo(xRequestId, q, sort, page, pageSize).getBody();
    }

    /**
     * List scanner registrations
     * Returns a list of currently configured scanner registrations.
     * <p><b>200</b> - A list of scanner registrations.
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @param q          Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort       Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page       The page number (optional, default to 1)
     * @param pageSize   The size of per page (optional, default to 10)
     * @return ResponseEntity&lt;List&lt;ScannerRegistration&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<ScannerRegistration>> listScannersWithHttpInfo(String xRequestId, String q, String sort, Long page, Long pageSize) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/scanners").build().toUriString();

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

        ParameterizedTypeReference<List<ScannerRegistration>> returnType = new ParameterizedTypeReference<List<ScannerRegistration>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Tests scanner registration settings
     * Pings scanner adapter to test endpoint URL and authorization settings.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param settings   A scanner registration settings to be tested. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void pingScanner(ScannerRegistrationSettings settings, String xRequestId) throws RestClientException
    {
        pingScannerWithHttpInfo(settings, xRequestId);
    }

    /**
     * Tests scanner registration settings
     * Pings scanner adapter to test endpoint URL and authorization settings.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param settings   A scanner registration settings to be tested. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> pingScannerWithHttpInfo(ScannerRegistrationSettings settings, String xRequestId) throws RestClientException
    {
        Object postBody = settings;

        // verify the required parameter 'settings' is set
        if (settings == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'settings' when calling pingScanner");
        }

        String path = UriComponentsBuilder.fromPath("/scanners/ping").build().toUriString();

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
     * Set system default scanner registration
     * Set the specified scanner registration as the system default one.
     * <p><b>200</b> - Successfully set the specified scanner registration as system default
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifier. (required)
     * @param payload        (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void setScannerAsDefault(String registrationId, IsDefault payload, String xRequestId) throws RestClientException
    {
        setScannerAsDefaultWithHttpInfo(registrationId, payload, xRequestId);
    }

    /**
     * Set system default scanner registration
     * Set the specified scanner registration as the system default one.
     * <p><b>200</b> - Successfully set the specified scanner registration as system default
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifier. (required)
     * @param payload        (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> setScannerAsDefaultWithHttpInfo(String registrationId, IsDefault payload, String xRequestId) throws RestClientException
    {
        Object postBody = payload;

        // verify the required parameter 'registrationId' is set
        if (registrationId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'registrationId' when calling setScannerAsDefault");
        }

        // verify the required parameter 'payload' is set
        if (payload == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'payload' when calling setScannerAsDefault");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("registration_id", registrationId);
        String path = UriComponentsBuilder.fromPath("/scanners/{registration_id}").buildAndExpand(uriVariables).toUriString();

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
     * Update a scanner registration
     * Updates the specified scanner registration.
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifier. (required)
     * @param registration   A scanner registraiton to be updated. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateScanner(String registrationId, ScannerRegistrationReq registration, String xRequestId) throws RestClientException
    {
        updateScannerWithHttpInfo(registrationId, registration, xRequestId);
    }

    /**
     * Update a scanner registration
     * Updates the specified scanner registration.
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param registrationId The scanner registration identifier. (required)
     * @param registration   A scanner registraiton to be updated. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateScannerWithHttpInfo(String registrationId, ScannerRegistrationReq registration, String xRequestId) throws RestClientException
    {
        Object postBody = registration;

        // verify the required parameter 'registrationId' is set
        if (registrationId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'registrationId' when calling updateScanner");
        }

        // verify the required parameter 'registration' is set
        if (registration == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'registration' when calling updateScanner");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("registration_id", registrationId);
        String path = UriComponentsBuilder.fromPath("/scanners/{registration_id}").buildAndExpand(uriVariables).toUriString();

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
