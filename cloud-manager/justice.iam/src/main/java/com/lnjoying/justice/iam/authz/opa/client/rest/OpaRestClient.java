package com.lnjoying.justice.iam.authz.opa.client.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lnjoying.justice.iam.authz.opa.client.OpaClientException;
import com.lnjoying.justice.iam.authz.opa.client.OpaClientConfiguration;
import com.lnjoying.justice.iam.authz.opa.client.rest.util.OpaUrl;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:43
 */

public class OpaRestClient
{
    private final OpaClientConfiguration config;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public OpaRestClient(OpaClientConfiguration config, RestTemplate restTemplate, ObjectMapper objectMapper)
    {
        this.config = config;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }


    public  <T, R> ResponseEntity<R> putRequest(String endpoint,  T body, Class<R> responseType)
    {
        OpaUrl url = OpaUrl.of(config.getEndPointUrl(), endpoint).normalized();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> httpEntity = new HttpEntity(body, httpHeaders);
        this.restTemplate.put(url.getUrl(), httpEntity);
        return null;
    }


    public  <T, R> ResponseEntity<R> postRequest(String endpoint,  T body, Class<R> responseType)
    {
        OpaUrl url = OpaUrl.of(config.getEndPointUrl(), endpoint).normalized();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> httpEntity = new HttpEntity(body, httpHeaders);
        return this.restTemplate.postForEntity(url.getUrl(), httpEntity, responseType);
    }


    public  <R> ResponseEntity<R> get(String endpoint, Class<R> responseType)
    {
        OpaUrl url = OpaUrl.of(config.getEndPointUrl(), endpoint).normalized();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return (ResponseEntity<R>) this.restTemplate.getForEntity(url.getUrl(), responseType);
    }

    public void delete(String endpoint, Map<String, ?> uriVariables)
    {
        OpaUrl url = OpaUrl.of(config.getEndPointUrl(), endpoint).normalized();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
       this.restTemplate.delete(url.getUrl(), uriVariables);
    }


    public static<R> void checkResponse(ResponseEntity<R> response) {
        if (HttpStatus.OK.value() != response.getStatusCodeValue()) {
            int statusCode = response.getStatusCodeValue();
            String message = String.format("opa returns an error: %d", statusCode);
            throw new OpaClientException(message);
        }
    }
}
