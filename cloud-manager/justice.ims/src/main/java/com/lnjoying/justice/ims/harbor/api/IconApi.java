package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Icon;

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
@Component("com.lnjoying.justice.ims.harbor.api.IconApi")
public class IconApi
{
    private ApiClient apiClient;

    public IconApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public IconApi(ApiClient apiClient)
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
     * Get artifact icon
     * Get the artifact icon with the specified digest. As the original icon image is resized and encoded before returning, the parameter \&quot;digest\&quot; in the path doesn&#39;t match the hash of the returned content
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param digest     The digest of the resource (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return Icon
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Icon getIcon(String digest, String xRequestId) throws RestClientException
    {
        return getIconWithHttpInfo(digest, xRequestId).getBody();
    }

    /**
     * Get artifact icon
     * Get the artifact icon with the specified digest. As the original icon image is resized and encoded before returning, the parameter \&quot;digest\&quot; in the path doesn&#39;t match the hash of the returned content
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param digest     The digest of the resource (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Icon&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Icon> getIconWithHttpInfo(String digest, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'digest' is set
        if (digest == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'digest' when calling getIcon");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("digest", digest);
        String path = UriComponentsBuilder.fromPath("/icons/{digest}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<Icon> returnType = new ParameterizedTypeReference<Icon>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
