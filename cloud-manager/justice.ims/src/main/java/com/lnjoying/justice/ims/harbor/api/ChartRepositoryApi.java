package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Label;

import java.util.ArrayList;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2022-09-09T18:08:43.633+08:00")
@Component("com.lnjoying.justice.ims.harbor.api.ChartRepositoryApi")
public class ChartRepositoryApi {
    private ApiClient apiClient;

    public ChartRepositoryApi() {
        this(new ApiClient());
    }

    @Autowired
    public ChartRepositoryApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ChartRepositoryApi(String basePath, String username, String password) {
        this.apiClient = new ApiClient(basePath, username, password);
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Return the attahced labels of chart.
     * Return the attahced labels of the specified chart version.
     * <p><b>200</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @param version The chart version (required)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void chartrepoRepoChartsNameVersionLabelsGet(String repo, String name, String version) throws RestClientException {
        chartrepoRepoChartsNameVersionLabelsGetWithHttpInfo(repo, name, version);
    }

    /**
     * Return the attahced labels of chart.
     * Return the attahced labels of the specified chart version.
     * <p><b>200</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @param version The chart version (required)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> chartrepoRepoChartsNameVersionLabelsGetWithHttpInfo(String repo, String name, String version) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'repo' is set
        if (repo == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repo' when calling chartrepoRepoChartsNameVersionLabelsGet");
        }
        
        // verify the required parameter 'name' is set
        if (name == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'name' when calling chartrepoRepoChartsNameVersionLabelsGet");
        }
        
        // verify the required parameter 'version' is set
        if (version == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'version' when calling chartrepoRepoChartsNameVersionLabelsGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("repo", repo);
        uriVariables.put("name", name);
        uriVariables.put("version", version);
        String path = UriComponentsBuilder.fromPath("/chartrepo/{repo}/charts/{name}/{version}/labels").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json", "text/plain"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Remove label from chart.
     * Remove label from the specified chart version.
     * <p><b>200</b> - The label is successfully unmarked from the chart version.
     * <p><b>400</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @param version The chart version (required)
     * @param id The label ID (required)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void chartrepoRepoChartsNameVersionLabelsIdDelete(String repo, String name, String version, Integer id) throws RestClientException {
        chartrepoRepoChartsNameVersionLabelsIdDeleteWithHttpInfo(repo, name, version, id);
    }

    /**
     * Remove chart.
     * Remove specified chart.
     * <p><b>200</b> - successfully unmarked from the chart .
     * <p><b>400</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void chartrepoRepoChartsNameDelete(String repo, String name) throws RestClientException {
        chartrepoRepoChartsNameDeleteWithHttpInfo(repo, name);
    }

    /**
     * Remove version from chart.
     * Remove version from the specified chart
     * <p><b>200</b> -successfully unmarked from the chart version.
     * <p><b>400</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @param version The chart version (required)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> chartrepoRepoChartsNameDeleteWithHttpInfo(String repo, String name) throws RestClientException {
        Object postBody = null;

        // verify the required parameter 'repo' is set
        if (repo == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repo' when calling chartrepoRepoChartsNameVersionLabelsIdDelete");
        }

        // verify the required parameter 'name' is set
        if (name == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'name' when calling chartrepoRepoChartsNameVersionLabelsIdDelete");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("repo", repo);
        uriVariables.put("name", name);
        String path = UriComponentsBuilder.fromPath("/chartrepo/{repo}/charts/{name}").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {
                "application/json", "text/plain"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Remove version from chart.
     * Remove specified chart version.
     * <p><b>200</b> - successfully unmarked from the chart version.
     * <p><b>400</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @param version The chart version (required)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void chartrepoRepoChartsNameVersionIdDelete(String repo, String name, String version) throws RestClientException {
        chartrepoRepoChartsNameVersionIdDeleteWithHttpInfo(repo, name, version);
    }

    /**
     * Remove version from chart.
     * Remove version from the specified chart
     * <p><b>200</b> -successfully unmarked from the chart version.
     * <p><b>400</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @param version The chart version (required)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> chartrepoRepoChartsNameVersionIdDeleteWithHttpInfo(String repo, String name, String version) throws RestClientException {
        Object postBody = null;

        // verify the required parameter 'repo' is set
        if (repo == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repo' when calling chartrepoRepoChartsNameVersionLabelsIdDelete");
        }

        // verify the required parameter 'name' is set
        if (name == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'name' when calling chartrepoRepoChartsNameVersionLabelsIdDelete");
        }

        // verify the required parameter 'version' is set
        if (version == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'version' when calling chartrepoRepoChartsNameVersionLabelsIdDelete");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("repo", repo);
        uriVariables.put("name", name);
        uriVariables.put("version", version);
        String path = UriComponentsBuilder.fromPath("/chartrepo/{repo}/charts/{name}/{version}").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {
                "application/json", "text/plain"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Remove label from chart.
     * Remove label from the specified chart version.
     * <p><b>200</b> - The label is successfully unmarked from the chart version.
     * <p><b>400</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @param version The chart version (required)
     * @param id The label ID (required)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> chartrepoRepoChartsNameVersionLabelsIdDeleteWithHttpInfo(String repo, String name, String version, Integer id) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'repo' is set
        if (repo == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repo' when calling chartrepoRepoChartsNameVersionLabelsIdDelete");
        }
        
        // verify the required parameter 'name' is set
        if (name == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'name' when calling chartrepoRepoChartsNameVersionLabelsIdDelete");
        }
        
        // verify the required parameter 'version' is set
        if (version == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'version' when calling chartrepoRepoChartsNameVersionLabelsIdDelete");
        }
        
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling chartrepoRepoChartsNameVersionLabelsIdDelete");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("repo", repo);
        uriVariables.put("name", name);
        uriVariables.put("version", version);
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/chartrepo/{repo}/charts/{name}/{version}/labels/{id}").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json", "text/plain"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Mark label to chart.
     * Mark label to the specified chart version.
     * <p><b>200</b> - The label is successfully marked to the chart version.
     * <p><b>400</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>409</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @param version The chart version (required)
     * @param label The label being marked to the chart version (required)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void chartrepoRepoChartsNameVersionLabelsPost(String repo, String name, String version, Label label) throws RestClientException {
        chartrepoRepoChartsNameVersionLabelsPostWithHttpInfo(repo, name, version, label);
    }

    /**
     * Mark label to chart.
     * Mark label to the specified chart version.
     * <p><b>200</b> - The label is successfully marked to the chart version.
     * <p><b>400</b>
     * <p><b>401</b>
     * <p><b>403</b>
     * <p><b>404</b>
     * <p><b>409</b>
     * <p><b>500</b>
     * @param repo The project name (required)
     * @param name The chart name (required)
     * @param version The chart version (required)
     * @param label The label being marked to the chart version (required)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> chartrepoRepoChartsNameVersionLabelsPostWithHttpInfo(String repo, String name, String version, Label label) throws RestClientException {
        Object postBody = label;
        
        // verify the required parameter 'repo' is set
        if (repo == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repo' when calling chartrepoRepoChartsNameVersionLabelsPost");
        }
        
        // verify the required parameter 'name' is set
        if (name == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'name' when calling chartrepoRepoChartsNameVersionLabelsPost");
        }
        
        // verify the required parameter 'version' is set
        if (version == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'version' when calling chartrepoRepoChartsNameVersionLabelsPost");
        }
        
        // verify the required parameter 'label' is set
        if (label == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'label' when calling chartrepoRepoChartsNameVersionLabelsPost");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("repo", repo);
        uriVariables.put("name", name);
        uriVariables.put("version", version);
        String path = UriComponentsBuilder.fromPath("/chartrepo/{repo}/charts/{name}/{version}/labels").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json", "text/plain"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
