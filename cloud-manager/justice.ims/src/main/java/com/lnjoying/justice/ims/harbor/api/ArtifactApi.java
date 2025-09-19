package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Artifact;
import com.lnjoying.justice.ims.harbor.model.Label;
import com.lnjoying.justice.ims.harbor.model.Tag;

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
@Component("com.lnjoying.justice.ims.harbor.api.ArtifactApi")
public class ArtifactApi
{
    private ApiClient apiClient;

    public ArtifactApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public ArtifactApi(ApiClient apiClient)
    {
        this.apiClient = apiClient;
    }

    public ArtifactApi(String basePath, String username, String password)
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
     * Add label to artifact
     * Add label to the specified artiact.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param label          The label that added to the artifact. Only the ID property is needed. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void addLabel(String projectName, String repositoryName, String reference, Label label, String xRequestId) throws RestClientException
    {
        addLabelWithHttpInfo(projectName, repositoryName, reference, label, xRequestId);
    }

    /**
     * Add label to artifact
     * Add label to the specified artiact.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param label          The label that added to the artifact. Only the ID property is needed. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> addLabelWithHttpInfo(String projectName, String repositoryName, String reference, Label label, String xRequestId) throws RestClientException
    {
        Object postBody = label;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling addLabel");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling addLabel");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling addLabel");
        }

        // verify the required parameter 'label' is set
        if (label == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'label' when calling addLabel");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/labels").buildAndExpand(uriVariables).toUriString();

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
     * Copy artifact
     * Copy the artifact specified in the \&quot;from\&quot; parameter to the repository.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>405</b> - Method not allowed
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param from           The artifact from which the new artifact is copied from, the format should be \&quot;project/repository:tag\&quot; or \&quot;project/repository@digest\&quot;. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void copyArtifact(String projectName, String repositoryName, String from, String xRequestId) throws RestClientException
    {
        copyArtifactWithHttpInfo(projectName, repositoryName, from, xRequestId);
    }

    /**
     * Copy artifact
     * Copy the artifact specified in the \&quot;from\&quot; parameter to the repository.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>405</b> - Method not allowed
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param from           The artifact from which the new artifact is copied from, the format should be \&quot;project/repository:tag\&quot; or \&quot;project/repository@digest\&quot;. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> copyArtifactWithHttpInfo(String projectName, String repositoryName, String from, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling copyArtifact");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling copyArtifact");
        }

        // verify the required parameter 'from' is set
        if (from == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'from' when calling copyArtifact");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "from", from));

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
     * Create tag
     * Create a tag for the specified artifact
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>405</b> - Method not allowed
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param tag            The JSON object of tag. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createTag(String projectName, String repositoryName, String reference, Tag tag, String xRequestId) throws RestClientException
    {
        createTagWithHttpInfo(projectName, repositoryName, reference, tag, xRequestId);
    }

    /**
     * Create tag
     * Create a tag for the specified artifact
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>405</b> - Method not allowed
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param tag            The JSON object of tag. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createTagWithHttpInfo(String projectName, String repositoryName, String reference, Tag tag, String xRequestId) throws RestClientException
    {
        Object postBody = tag;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling createTag");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling createTag");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling createTag");
        }

        // verify the required parameter 'tag' is set
        if (tag == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'tag' when calling createTag");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/tags").buildAndExpand(uriVariables).toUriString();

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
     * Delete the specific artifact
     * Delete the artifact specified by the reference under the project and repository. The reference can be digest or tag
     * <p><b>200</b> - Success
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
    public void deleteArtifact(String projectName, String repositoryName, String reference, String xRequestId) throws RestClientException
    {
        deleteArtifactWithHttpInfo(projectName, repositoryName, reference, xRequestId);
    }

    /**
     * Delete the specific artifact
     * Delete the artifact specified by the reference under the project and repository. The reference can be digest or tag
     * <p><b>200</b> - Success
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
    public ResponseEntity<Void> deleteArtifactWithHttpInfo(String projectName, String repositoryName, String reference, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling deleteArtifact");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling deleteArtifact");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling deleteArtifact");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}").buildAndExpand(uriVariables).toUriString();

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
     * Delete tag
     * Delete the tag of the specified artifact
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param tagName        The name of the tag (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteTag(String projectName, String repositoryName, String reference, String tagName, String xRequestId) throws RestClientException
    {
        deleteTagWithHttpInfo(projectName, repositoryName, reference, tagName, xRequestId);
    }

    /**
     * Delete tag
     * Delete the tag of the specified artifact
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param tagName        The name of the tag (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteTagWithHttpInfo(String projectName, String repositoryName, String reference, String tagName, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling deleteTag");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling deleteTag");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling deleteTag");
        }

        // verify the required parameter 'tagName' is set
        if (tagName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'tagName' when calling deleteTag");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        uriVariables.put("tag_name", tagName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/tags/{tag_name}").buildAndExpand(uriVariables).toUriString();

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
     * Get the addition of the specific artifact
     * Get the addition of the artifact specified by the reference under the project and repository.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param addition       The type of addition. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String getAddition(String projectName, String repositoryName, String reference, String addition, String xRequestId) throws RestClientException
    {
        return getAdditionWithHttpInfo(projectName, repositoryName, reference, addition, xRequestId).getBody();
    }

    /**
     * Get the addition of the specific artifact
     * Get the addition of the artifact specified by the reference under the project and repository.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param addition       The type of addition. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<String> getAdditionWithHttpInfo(String projectName, String repositoryName, String reference, String addition, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling getAddition");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling getAddition");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling getAddition");
        }

        // verify the required parameter 'addition' is set
        if (addition == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'addition' when calling getAddition");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        uriVariables.put("addition", addition);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/additions/{addition}").buildAndExpand(uriVariables).toUriString();

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

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get the specific artifact
     * Get the artifact specified by the reference under the project and repository. The reference can be digest or tag.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName            The name of the project (required)
     * @param repositoryName         The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference              The reference of the artifact, can be digest or tag (required)
     * @param xRequestId             An unique ID for the request (optional)
     * @param page                   The page number (optional, default to 1)
     * @param pageSize               The size of per page (optional, default to 10)
     * @param xAcceptVulnerabilities A comma-separated lists of MIME types for the scan report or scan summary. The first mime type will be used when the report found for it. Currently the mime type supports &#39;application/vnd.scanner.adapter.vuln.report.harbor+json; version&#x3D;1.0&#39; and &#39;application/vnd.security.vulnerability.report; version&#x3D;1.1&#39; (optional, default to application/vnd.scanner.adapter.vuln.report.harbor+json; version=1.0)
     * @param withTag                Specify whether the tags are inclued inside the returning artifacts (optional, default to true)
     * @param withLabel              Specify whether the labels are inclued inside the returning artifacts (optional, default to false)
     * @param withScanOverview       Specify whether the scan overview is inclued inside the returning artifacts (optional, default to false)
     * @param withSignature          Specify whether the signature is inclued inside the returning artifacts (optional, default to false)
     * @param withImmutableStatus    Specify whether the immutable status is inclued inside the tags of the returning artifacts. Only works when setting \&quot;with_tag&#x3D;true\&quot; (optional, default to false)
     * @return Artifact
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Artifact getArtifact(String projectName, String repositoryName, String reference, String xRequestId, Long page, Long pageSize, String xAcceptVulnerabilities, Boolean withTag, Boolean withLabel, Boolean withScanOverview, Boolean withSignature, Boolean withImmutableStatus) throws RestClientException
    {
        return getArtifactWithHttpInfo(projectName, repositoryName, reference, xRequestId, page, pageSize, xAcceptVulnerabilities, withTag, withLabel, withScanOverview, withSignature, withImmutableStatus).getBody();
    }

    /**
     * Get the specific artifact
     * Get the artifact specified by the reference under the project and repository. The reference can be digest or tag.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName            The name of the project (required)
     * @param repositoryName         The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference              The reference of the artifact, can be digest or tag (required)
     * @param xRequestId             An unique ID for the request (optional)
     * @param page                   The page number (optional, default to 1)
     * @param pageSize               The size of per page (optional, default to 10)
     * @param xAcceptVulnerabilities A comma-separated lists of MIME types for the scan report or scan summary. The first mime type will be used when the report found for it. Currently the mime type supports &#39;application/vnd.scanner.adapter.vuln.report.harbor+json; version&#x3D;1.0&#39; and &#39;application/vnd.security.vulnerability.report; version&#x3D;1.1&#39; (optional, default to application/vnd.scanner.adapter.vuln.report.harbor+json; version=1.0)
     * @param withTag                Specify whether the tags are inclued inside the returning artifacts (optional, default to true)
     * @param withLabel              Specify whether the labels are inclued inside the returning artifacts (optional, default to false)
     * @param withScanOverview       Specify whether the scan overview is inclued inside the returning artifacts (optional, default to false)
     * @param withSignature          Specify whether the signature is inclued inside the returning artifacts (optional, default to false)
     * @param withImmutableStatus    Specify whether the immutable status is inclued inside the tags of the returning artifacts. Only works when setting \&quot;with_tag&#x3D;true\&quot; (optional, default to false)
     * @return ResponseEntity&lt;Artifact&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Artifact> getArtifactWithHttpInfo(String projectName, String repositoryName, String reference, String xRequestId, Long page, Long pageSize, String xAcceptVulnerabilities, Boolean withTag, Boolean withLabel, Boolean withScanOverview, Boolean withSignature, Boolean withImmutableStatus) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling getArtifact");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling getArtifact");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling getArtifact");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_tag", withTag));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_label", withLabel));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_scan_overview", withScanOverview));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_signature", withSignature));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_immutable_status", withImmutableStatus));

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));
        if (xAcceptVulnerabilities != null)
            headerParams.add("X-Accept-Vulnerabilities", apiClient.parameterToString(xAcceptVulnerabilities));

        final String[] accepts = {
                "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"basic"};

        ParameterizedTypeReference<Artifact> returnType = new ParameterizedTypeReference<Artifact>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get the vulnerabilities addition of the specific artifact
     * Get the vulnerabilities addition of the artifact specified by the reference under the project and repository.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName            The name of the project (required)
     * @param repositoryName         The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference              The reference of the artifact, can be digest or tag (required)
     * @param xRequestId             An unique ID for the request (optional)
     * @param xAcceptVulnerabilities A comma-separated lists of MIME types for the scan report or scan summary. The first mime type will be used when the report found for it. Currently the mime type supports &#39;application/vnd.scanner.adapter.vuln.report.harbor+json; version&#x3D;1.0&#39; and &#39;application/vnd.security.vulnerability.report; version&#x3D;1.1&#39; (optional, default to application/vnd.scanner.adapter.vuln.report.harbor+json; version=1.0)
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String getVulnerabilitiesAddition(String projectName, String repositoryName, String reference, String xRequestId, String xAcceptVulnerabilities) throws RestClientException
    {
        return getVulnerabilitiesAdditionWithHttpInfo(projectName, repositoryName, reference, xRequestId, xAcceptVulnerabilities).getBody();
    }

    /**
     * Get the vulnerabilities addition of the specific artifact
     * Get the vulnerabilities addition of the artifact specified by the reference under the project and repository.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName            The name of the project (required)
     * @param repositoryName         The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference              The reference of the artifact, can be digest or tag (required)
     * @param xRequestId             An unique ID for the request (optional)
     * @param xAcceptVulnerabilities A comma-separated lists of MIME types for the scan report or scan summary. The first mime type will be used when the report found for it. Currently the mime type supports &#39;application/vnd.scanner.adapter.vuln.report.harbor+json; version&#x3D;1.0&#39; and &#39;application/vnd.security.vulnerability.report; version&#x3D;1.1&#39; (optional, default to application/vnd.scanner.adapter.vuln.report.harbor+json; version=1.0)
     * @return ResponseEntity&lt;String&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<String> getVulnerabilitiesAdditionWithHttpInfo(String projectName, String repositoryName, String reference, String xRequestId, String xAcceptVulnerabilities) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling getVulnerabilitiesAddition");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling getVulnerabilitiesAddition");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling getVulnerabilitiesAddition");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/additions/vulnerabilities").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));
        if (xAcceptVulnerabilities != null)
            headerParams.add("X-Accept-Vulnerabilities", apiClient.parameterToString(xAcceptVulnerabilities));

        final String[] accepts = {
                "application/json"
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
     * List artifacts
     * List artifacts under the specific project and repository. Except the basic properties, the other supported queries in \&quot;q\&quot; includes \&quot;tags&#x3D;*\&quot; to list only tagged artifacts, \&quot;tags&#x3D;nil\&quot; to list only untagged artifacts, \&quot;tags&#x3D;~v\&quot; to list artifacts whose tag fuzzy matches \&quot;v\&quot;, \&quot;tags&#x3D;v\&quot; to list artifact whose tag exactly matches \&quot;v\&quot;, \&quot;labels&#x3D;(id1, id2)\&quot; to list artifacts that both labels with id1 and id2 are added to
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName            The name of the project (required)
     * @param repositoryName         The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param xRequestId             An unique ID for the request (optional)
     * @param q                      Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort                   Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page                   The page number (optional, default to 1)
     * @param pageSize               The size of per page (optional, default to 10)
     * @param xAcceptVulnerabilities A comma-separated lists of MIME types for the scan report or scan summary. The first mime type will be used when the report found for it. Currently the mime type supports &#39;application/vnd.scanner.adapter.vuln.report.harbor+json; version&#x3D;1.0&#39; and &#39;application/vnd.security.vulnerability.report; version&#x3D;1.1&#39; (optional, default to application/vnd.scanner.adapter.vuln.report.harbor+json; version=1.0)
     * @param withTag                Specify whether the tags are included inside the returning artifacts (optional, default to true)
     * @param withLabel              Specify whether the labels are included inside the returning artifacts (optional, default to false)
     * @param withScanOverview       Specify whether the scan overview is included inside the returning artifacts (optional, default to false)
     * @param withSignature          Specify whether the signature is included inside the tags of the returning artifacts. Only works when setting \&quot;with_tag&#x3D;true\&quot; (optional, default to false)
     * @param withImmutableStatus    Specify whether the immutable status is included inside the tags of the returning artifacts. Only works when setting \&quot;with_tag&#x3D;true\&quot; (optional, default to false)
     * @return List&lt;Artifact&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Artifact> listArtifacts(String projectName, String repositoryName, String xRequestId, String q, String sort, Long page, Long pageSize, String xAcceptVulnerabilities, Boolean withTag, Boolean withLabel, Boolean withScanOverview, Boolean withSignature, Boolean withImmutableStatus) throws RestClientException
    {
        return listArtifactsWithHttpInfo(projectName, repositoryName, xRequestId, q, sort, page, pageSize, xAcceptVulnerabilities, withTag, withLabel, withScanOverview, withSignature, withImmutableStatus).getBody();
    }

    /**
     * List artifacts
     * List artifacts under the specific project and repository. Except the basic properties, the other supported queries in \&quot;q\&quot; includes \&quot;tags&#x3D;*\&quot; to list only tagged artifacts, \&quot;tags&#x3D;nil\&quot; to list only untagged artifacts, \&quot;tags&#x3D;~v\&quot; to list artifacts whose tag fuzzy matches \&quot;v\&quot;, \&quot;tags&#x3D;v\&quot; to list artifact whose tag exactly matches \&quot;v\&quot;, \&quot;labels&#x3D;(id1, id2)\&quot; to list artifacts that both labels with id1 and id2 are added to
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName            The name of the project (required)
     * @param repositoryName         The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param xRequestId             An unique ID for the request (optional)
     * @param q                      Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort                   Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page                   The page number (optional, default to 1)
     * @param pageSize               The size of per page (optional, default to 10)
     * @param xAcceptVulnerabilities A comma-separated lists of MIME types for the scan report or scan summary. The first mime type will be used when the report found for it. Currently the mime type supports &#39;application/vnd.scanner.adapter.vuln.report.harbor+json; version&#x3D;1.0&#39; and &#39;application/vnd.security.vulnerability.report; version&#x3D;1.1&#39; (optional, default to application/vnd.scanner.adapter.vuln.report.harbor+json; version=1.0)
     * @param withTag                Specify whether the tags are included inside the returning artifacts (optional, default to true)
     * @param withLabel              Specify whether the labels are included inside the returning artifacts (optional, default to false)
     * @param withScanOverview       Specify whether the scan overview is included inside the returning artifacts (optional, default to false)
     * @param withSignature          Specify whether the signature is included inside the tags of the returning artifacts. Only works when setting \&quot;with_tag&#x3D;true\&quot; (optional, default to false)
     * @param withImmutableStatus    Specify whether the immutable status is included inside the tags of the returning artifacts. Only works when setting \&quot;with_tag&#x3D;true\&quot; (optional, default to false)
     * @return ResponseEntity&lt;List&lt;Artifact&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Artifact>> listArtifactsWithHttpInfo(String projectName, String repositoryName, String xRequestId, String q, String sort, Long page, Long pageSize, String xAcceptVulnerabilities, Boolean withTag, Boolean withLabel, Boolean withScanOverview, Boolean withSignature, Boolean withImmutableStatus) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling listArtifacts");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling listArtifacts");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sort", sort));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_tag", withTag));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_label", withLabel));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_scan_overview", withScanOverview));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_signature", withSignature));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_immutable_status", withImmutableStatus));

        if (xRequestId != null)
            headerParams.add("X-Request-Id", apiClient.parameterToString(xRequestId));
        if (xAcceptVulnerabilities != null)
            headerParams.add("X-Accept-Vulnerabilities", apiClient.parameterToString(xAcceptVulnerabilities));

        final String[] accepts = {
                "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"basic"};

        ParameterizedTypeReference<List<Artifact>> returnType = new ParameterizedTypeReference<List<Artifact>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * List tags
     * List tags of the specific artifact
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName         The name of the project (required)
     * @param repositoryName      The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference           The reference of the artifact, can be digest or tag (required)
     * @param xRequestId          An unique ID for the request (optional)
     * @param q                   Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort                Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page                The page number (optional, default to 1)
     * @param pageSize            The size of per page (optional, default to 10)
     * @param withSignature       Specify whether the signature is included inside the returning tags (optional, default to false)
     * @param withImmutableStatus Specify whether the immutable status is included inside the returning tags (optional, default to false)
     * @return List&lt;Tag&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<Tag> listTags(String projectName, String repositoryName, String reference, String xRequestId, String q, String sort, Long page, Long pageSize, Boolean withSignature, Boolean withImmutableStatus) throws RestClientException
    {
        return listTagsWithHttpInfo(projectName, repositoryName, reference, xRequestId, q, sort, page, pageSize, withSignature, withImmutableStatus).getBody();
    }

    /**
     * List tags
     * List tags of the specific artifact
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>500</b> - Internal server error
     *
     * @param projectName         The name of the project (required)
     * @param repositoryName      The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference           The reference of the artifact, can be digest or tag (required)
     * @param xRequestId          An unique ID for the request (optional)
     * @param q                   Query string to query resources. Supported query patterns are \&quot;exact match(k&#x3D;v)\&quot;, \&quot;fuzzy match(k&#x3D;~v)\&quot;, \&quot;range(k&#x3D;[min~max])\&quot;, \&quot;list with union releationship(k&#x3D;{v1 v2 v3})\&quot; and \&quot;list with intersetion relationship(k&#x3D;(v1 v2 v3))\&quot;. The value of range and list can be string(enclosed by \&quot; or &#39;), integer or time(in format \&quot;2020-04-09 02:36:00\&quot;). All of these query patterns should be put in the query string \&quot;q&#x3D;xxx\&quot; and splitted by \&quot;,\&quot;. e.g. q&#x3D;k1&#x3D;v1,k2&#x3D;~v2,k3&#x3D;[min~max] (optional)
     * @param sort                Sort the resource list in ascending or descending order. e.g. sort by field1 in ascending orderr and field2 in descending order with \&quot;sort&#x3D;field1,-field2\&quot; (optional)
     * @param page                The page number (optional, default to 1)
     * @param pageSize            The size of per page (optional, default to 10)
     * @param withSignature       Specify whether the signature is included inside the returning tags (optional, default to false)
     * @param withImmutableStatus Specify whether the immutable status is included inside the returning tags (optional, default to false)
     * @return ResponseEntity&lt;List&lt;Tag&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<Tag>> listTagsWithHttpInfo(String projectName, String repositoryName, String reference, String xRequestId, String q, String sort, Long page, Long pageSize, Boolean withSignature, Boolean withImmutableStatus) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling listTags");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling listTags");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling listTags");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/tags").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "q", q));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sort", sort));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_signature", withSignature));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "with_immutable_status", withImmutableStatus));

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

        ParameterizedTypeReference<List<Tag>> returnType = new ParameterizedTypeReference<List<Tag>>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Remove label from artifact
     * Remove the label from the specified artiact.
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param labelId        The ID of the label that removed from the artifact. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void removeLabel(String projectName, String repositoryName, String reference, Long labelId, String xRequestId) throws RestClientException
    {
        removeLabelWithHttpInfo(projectName, repositoryName, reference, labelId, xRequestId);
    }

    /**
     * Remove label from artifact
     * Remove the label from the specified artiact.
     * <p><b>200</b> - Success
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not found
     * <p><b>409</b> - Conflict
     * <p><b>500</b> - Internal server error
     *
     * @param projectName    The name of the project (required)
     * @param repositoryName The name of the repository. If it contains slash, encode it with URL encoding. e.g. a/b -&gt; a%252Fb (required)
     * @param reference      The reference of the artifact, can be digest or tag (required)
     * @param labelId        The ID of the label that removed from the artifact. (required)
     * @param xRequestId     An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> removeLabelWithHttpInfo(String projectName, String repositoryName, String reference, Long labelId, String xRequestId) throws RestClientException
    {
        Object postBody = null;

        // verify the required parameter 'projectName' is set
        if (projectName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'projectName' when calling removeLabel");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'repositoryName' when calling removeLabel");
        }

        // verify the required parameter 'reference' is set
        if (reference == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'reference' when calling removeLabel");
        }

        // verify the required parameter 'labelId' is set
        if (labelId == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'labelId' when calling removeLabel");
        }

        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("project_name", projectName);
        uriVariables.put("repository_name", repositoryName);
        uriVariables.put("reference", reference);
        uriVariables.put("label_id", labelId);
        String path = UriComponentsBuilder.fromPath("/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/labels/{label_id}").buildAndExpand(uriVariables).toUriString();

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
}
