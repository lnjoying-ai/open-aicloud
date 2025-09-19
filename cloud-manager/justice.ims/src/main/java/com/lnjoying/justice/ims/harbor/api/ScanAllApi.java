package com.lnjoying.justice.ims.harbor.api;

import com.lnjoying.justice.ims.harbor.ApiClient;

import com.lnjoying.justice.ims.harbor.model.Schedule;
import com.lnjoying.justice.ims.harbor.model.Stats;

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
@Component("com.lnjoying.justice.ims.harbor.api.ScanAllApi")
public class ScanAllApi
{
    private ApiClient apiClient;

    public ScanAllApi()
    {
        this(new ApiClient());
    }

    @Autowired
    public ScanAllApi(ApiClient apiClient)
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
     * Create a schedule or a manual trigger for the scan all job.
     * This endpoint is for creating a schedule or a manual trigger for the scan all job, which scans all of images in Harbor.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>409</b> - Conflict
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param schedule   Create a schedule or a manual trigger for the scan all job. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createScanAllSchedule(Schedule schedule, String xRequestId) throws RestClientException
    {
        createScanAllScheduleWithHttpInfo(schedule, xRequestId);
    }

    /**
     * Create a schedule or a manual trigger for the scan all job.
     * This endpoint is for creating a schedule or a manual trigger for the scan all job, which scans all of images in Harbor.
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>409</b> - Conflict
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param schedule   Create a schedule or a manual trigger for the scan all job. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createScanAllScheduleWithHttpInfo(Schedule schedule, String xRequestId) throws RestClientException
    {
        Object postBody = schedule;

        // verify the required parameter 'schedule' is set
        if (schedule == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'schedule' when calling createScanAllSchedule");
        }

        String path = UriComponentsBuilder.fromPath("/system/scanAll/schedule").build().toUriString();

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
     * Get the metrics of the latest scan all process
     * Get the metrics of the latest scan all process
     * <p><b>200</b> - OK
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return Stats
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Stats getLatestScanAllMetrics(String xRequestId) throws RestClientException
    {
        return getLatestScanAllMetricsWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Get the metrics of the latest scan all process
     * Get the metrics of the latest scan all process
     * <p><b>200</b> - OK
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Stats&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Stats> getLatestScanAllMetricsWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/scans/all/metrics").build().toUriString();

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

        ParameterizedTypeReference<Stats> returnType = new ParameterizedTypeReference<Stats>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get the metrics of the latest scheduled scan all process
     * Get the metrics of the latest scheduled scan all process
     * <p><b>200</b> - OK
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return Stats
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    @Deprecated
    public Stats getLatestScheduledScanAllMetrics(String xRequestId) throws RestClientException
    {
        return getLatestScheduledScanAllMetricsWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Get the metrics of the latest scheduled scan all process
     * Get the metrics of the latest scheduled scan all process
     * <p><b>200</b> - OK
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Stats&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    @Deprecated
    public ResponseEntity<Stats> getLatestScheduledScanAllMetricsWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/scans/schedule/metrics").build().toUriString();

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

        ParameterizedTypeReference<Stats> returnType = new ParameterizedTypeReference<Stats>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get scan all&#39;s schedule.
     * This endpoint is for getting a schedule for the scan all job, which scans all of images in Harbor.
     * <p><b>200</b> - Get a schedule for the scan all job, which scans all of images in Harbor.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return Schedule
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Schedule getScanAllSchedule(String xRequestId) throws RestClientException
    {
        return getScanAllScheduleWithHttpInfo(xRequestId).getBody();
    }

    /**
     * Get scan all&#39;s schedule.
     * This endpoint is for getting a schedule for the scan all job, which scans all of images in Harbor.
     * <p><b>200</b> - Get a schedule for the scan all job, which scans all of images in Harbor.
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Schedule&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Schedule> getScanAllScheduleWithHttpInfo(String xRequestId) throws RestClientException
    {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/system/scanAll/schedule").build().toUriString();

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

        ParameterizedTypeReference<Schedule> returnType = new ParameterizedTypeReference<Schedule>()
        {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update scan all&#39;s schedule.
     * This endpoint is for updating the schedule of scan all job, which scans all of images in Harbor.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param schedule   Updates the schedule of scan all job, which scans all of images in Harbor. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateScanAllSchedule(Schedule schedule, String xRequestId) throws RestClientException
    {
        updateScanAllScheduleWithHttpInfo(schedule, xRequestId);
    }

    /**
     * Update scan all&#39;s schedule.
     * This endpoint is for updating the schedule of scan all job, which scans all of images in Harbor.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>412</b> - Precondition failed
     * <p><b>500</b> - Internal server error
     *
     * @param schedule   Updates the schedule of scan all job, which scans all of images in Harbor. (required)
     * @param xRequestId An unique ID for the request (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateScanAllScheduleWithHttpInfo(Schedule schedule, String xRequestId) throws RestClientException
    {
        Object postBody = schedule;

        // verify the required parameter 'schedule' is set
        if (schedule == null)
        {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'schedule' when calling updateScanAllSchedule");
        }

        String path = UriComponentsBuilder.fromPath("/system/scanAll/schedule").build().toUriString();

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
