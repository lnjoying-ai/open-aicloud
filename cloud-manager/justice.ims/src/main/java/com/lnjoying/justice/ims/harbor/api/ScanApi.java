package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

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
@Component("com.lnjoying.justice.ims.harbor.api.ScanApi")
public class ScanApi
{
    private ApiClient apiClient;

    public ScanApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public ScanApi(ApiClient apiClient)
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
     * Get the log of the scan report
     * Get the log of the scan report
     * <p><b>200</b> - Successfully get scan log file
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param reportId       The report id to get the log (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String getReportLog(String projectName, String repositoryName, String reference, String reportId, String xRequestId) throws RestClientException
    {
        return getReportLogWithHttpInfo(projectName, repositoryName, reference, reportId, xRequestId).getBody();
    }

    /**
     * Get the log of the scan report
     * Get the log of the scan report
     * <p><b>200</b> - Successfully get scan log file
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param reportId       The report id to get the log (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<String> getReportLogWithHttpInfo(String projectName, String repositoryName, String reference, String reportId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling getReportLog");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling getReportLog");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling getReportLog");
        }

        // verify the required parameter 'reportId' is set
        if (reportId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reportId' when calling getReportLog");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        uriVariables.put("report_id", reportId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/scan/{report_id}/log").buildAndExpand(uriVariables).toUriString();

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
     * Scan the artifact
     * Scan the specified artifact
     * <p><b>202</b> - Accepted
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void scanArtifact(String projectName, String repositoryName, String reference, String xRequestId) throws RestClientException
    {
        scanArtifactWithHttpInfo(projectName, repositoryName, reference, xRequestId);
    }

    /**
     * Scan the artifact
     * Scan the specified artifact
     * <p><b>202</b> - Accepted
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> scanArtifactWithHttpInfo(String projectName, String repositoryName, String reference, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling scanArtifact");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling scanArtifact");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling scanArtifact");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/scan").buildAndExpand(uriVariables).toUriString();

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
}
