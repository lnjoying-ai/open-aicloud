package com.lnjoying.justice.cmp.utils.osclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.client.OSAuthReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.client.OSAuthRsp;
import com.lnjoying.justice.cmp.utils.CustomErrorHandler;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import io.vertx.core.http.HttpServerRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.VertxServerRequestToHttpServletRequest;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.*;

@Data
public class OSClientV3
{
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").create();
    private static Gson defaultGson = new GsonBuilder().serializeNulls().create();

    private String cloudId;
    private String cloudProductShort;
    private Map<String, ServiceEndpoint> serviceEndpoints;
    private String user;
    private String password;
    private String projectName;
    private String projectId;
    private String domainName;
    private String domainId;
    private OSToken osToken;

    @Data
    public class OSToken
    {
        private String XSubjectToken;
        private OSAuthRsp osAuthRsp;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServiceEndpoint
    {
        private String ip;
        private Integer port;
        private String urlPrefix;
    }

    public ResponseEntity<String> sendHttpGetRequestToCloud(String path)
    {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            requestHeaders.set("Content-Type", "application/json");
            requestHeaders.set("Connection", "false");
            path = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/",
                    this.cloudProductShort, "/clouds/", this.cloudId, path);
            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<String> response = restTemplate.exchange(path, HttpMethod.GET, requestEntity, String.class);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> sendHttpGetRequestToCloud(String path, Map<String, String> headers)
    {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            requestHeaders.set("Content-Type", "application/json");
            path = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/",
                    this.cloudProductShort, "/clouds/", this.cloudId, path);
            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));
            requestHeaders.set("Connection", "false");

            if (headers != null) headers.forEach((key, value) -> {requestHeaders.add(key, value);});

            HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<String> response = restTemplate.exchange(path, HttpMethod.GET, requestEntity, String.class);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> sendHttpPostRequestToCloud(String path, Object body)
    {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            requestHeaders.set("Content-Type", "application/json");
            path = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/",
                    this.cloudProductShort, "/clouds/", this.cloudId, path);
            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            HttpEntity<String> requestEntity = new HttpEntity<>(gson.toJson(body), requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<String> response = restTemplate.exchange(path, HttpMethod.POST, requestEntity, String.class);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity sendHttpRequestToCloud()
    {
        VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements())
            {
                String headerName = (String) headerNames.nextElement();
                requestHeaders.add(headerName, request.getHeader(headerName));
            }

            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
            vertxRequestField.setAccessible(true);
            HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

            String url = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/", httpServerRequest.uri().split("/cmp/v1/")[1]);

            HttpEntity<String> requestEntity = new HttpEntity<>(request.getContext().getBody().toString(), requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

            checkResponse(response.getStatusCode(), response.getBody());

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity sendHttpRequestToCloud(Map<String, String> headers)
    {
        VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements())
            {
                String headerName = (String) headerNames.nextElement();
                requestHeaders.add(headerName, request.getHeader(headerName));
            }

            if (headers != null) headers.forEach((key, value) -> {requestHeaders.add(key, value);});

            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
            vertxRequestField.setAccessible(true);
            HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

            String url = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/", httpServerRequest.uri().split("/cmp/v1/")[1]);

            HttpEntity<String> requestEntity = new HttpEntity<>(request.getContext().getBody().toString(), requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

            checkResponse(response.getStatusCode(), response.getBody());

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity sendHttpRequestToCloud(Object body)
    {
        return sendHttpRequestToCloud(body, null);
    }

    public ResponseEntity sendHttpRequestToCloud(Object body, Map<String, String> headers)
    {
        VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements())
            {
                String headerName = (String) headerNames.nextElement();
                requestHeaders.add(headerName, request.getHeader(headerName));
            }

            if (headers != null) headers.forEach((key, value) -> {requestHeaders.add(key, value);});

            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
            vertxRequestField.setAccessible(true);
            HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

            String url = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/", httpServerRequest.uri().split("/cmp/v1/")[1]);

            HttpEntity<String> requestEntity = new HttpEntity<>(gson.toJson(body), requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

            checkResponse(response.getStatusCode(), response.getBody());

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity sendHttpRequestToCloudWithoutExt(Object body)
    {
        VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements())
            {
                String headerName = (String) headerNames.nextElement();
                requestHeaders.add(headerName, request.getHeader(headerName));
            }

            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
            vertxRequestField.setAccessible(true);
            HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

            String url = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/", httpServerRequest.uri().split("/cmp/v1/")[1].replace("/extension", ""));

            HttpEntity<String> requestEntity = new HttpEntity<>(body instanceof String ? (String)body : gson.toJson(body), requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

            checkResponse(response.getStatusCode(), response.getBody());

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity sendHttpRequestToCloudWithProject()
    {
        VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements())
            {
                String headerName = (String) headerNames.nextElement();
                requestHeaders.add(headerName, request.getHeader(headerName));
            }

            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
            vertxRequestField.setAccessible(true);
            HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

            String url = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/", httpServerRequest.uri().split("/cmp/v1/")[1].replace("%7Bproject_id%7D", "{project_id}").replace("{project_id}", OSClientUtil.getOSProject(this)));

            HttpEntity<String> requestEntity = new HttpEntity<>(request.getContext().getBody().toString(), requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

            checkResponse(response.getStatusCode(), response.getBody());

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity sendHttpRequestToCloudWithProject(Map<String, String> headers)
    {
        VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements())
            {
                String headerName = (String) headerNames.nextElement();
                requestHeaders.add(headerName, request.getHeader(headerName));
            }

            if (headers != null) headers.forEach((key, value) -> {requestHeaders.add(key, value);});

            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
            vertxRequestField.setAccessible(true);
            HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

            String url = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/", httpServerRequest.uri().split("/cmp/v1/")[1].replace("%7Bproject_id%7D", "{project_id}").replace("{project_id}", OSClientUtil.getOSProject(this)));

            HttpEntity<String> requestEntity = new HttpEntity<>(request.getContext().getBody().toString(), requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

            checkResponse(response.getStatusCode(), response.getBody());

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity sendHttpRequestToCloudWithProject(Object body)
    {
        return sendHttpRequestToCloudWithProject(body, null);
    }

    public ResponseEntity sendHttpRequestToCloudWithProject(Object body, Map<String, String> headers)
    {
        VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements())
            {
                String headerName = (String) headerNames.nextElement();
                requestHeaders.add(headerName, request.getHeader(headerName));
            }

            if (headers != null) headers.forEach((key, value) -> {requestHeaders.add(key, value);});

            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
            vertxRequestField.setAccessible(true);
            HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

            String url = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/", httpServerRequest.uri().split("/cmp/v1/")[1].replace("%7Bproject_id%7D", "{project_id}").replace("{project_id}", OSClientUtil.getOSProject(this)));

            HttpEntity<String> requestEntity = new HttpEntity<>(gson.toJson(body), requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

            checkResponse(response.getStatusCode(), response.getBody());

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity sendHttpRequestToCloud(String url, HttpMethod method, Object body, Map<String, String> headers)
    {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 60 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            if (headers != null) headers.forEach(requestHeaders::add);

            requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", OSClientUtil.getCloudManagerConfig().getCloudManagerToken()));

            HttpEntity<String> requestEntity;
            if (body == null)
            {
                requestEntity = new HttpEntity<>(requestHeaders);
            }
            else
            {
                requestEntity = new HttpEntity<>(defaultGson.toJson(body), requestHeaders);
            }
            restTemplate.setErrorHandler(new CustomErrorHandler());

            url = Utils.buildStr("http://", OSClientUtil.getCloudManagerConfig().getServiceGwUrl(), "/proxy/",
                    this.cloudProductShort, "/clouds/", this.cloudId, url);

            ResponseEntity<Object> response = restTemplate.exchange(url, method, requestEntity, Object.class);

            checkResponse(response.getStatusCode(), response.getBody());

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean authenticate()
    {
        OSAuthReq.OSUser osUser = new OSAuthReq.OSUser(null, user, null, password);

        OSAuthReq.OSIdentity osIdentity = new OSAuthReq.OSIdentity();
        osIdentity.setMethods(new ArrayList<>());
        osIdentity.getMethods().add("password");
        osIdentity.setPassword(new OSAuthReq.OSPassword(osUser));
        OSAuthReq.OSScope scope = new OSAuthReq.OSScope();
        if (StringUtils.isNotEmpty(this.projectId))
        {
            scope.setProject(new HashMap<>());
            scope.getProject().put("id", this.projectId);
        }
        else
        {
            scope.setSystem(new HashMap<>());
            scope.getSystem().put("all", true);
        }
        OSAuthReq osAuthReq = new OSAuthReq(new OSAuthReq.OSAuth(osIdentity, scope));
        ResponseEntity<String> response = sendHttpPostRequestToCloud("/v3/auth/tokens", osAuthReq);
        if (response.getStatusCodeValue() != 201)
        {
            return false;
        }
        OSAuthRsp authRsp = gson.fromJson(response.getBody(), OSAuthRsp.class);

        OSToken osToken = new OSToken();
        osToken.setOsAuthRsp(authRsp);
        osToken.setXSubjectToken(response.getHeaders().getFirst("X-Subject-Token"));
        this.osToken = osToken;
        return true;
    }

    public String getToken()
    {
        Date now = new Date();
        if (this.osToken == null || this.osToken.osAuthRsp.getToken().getExpiresAt().getTime() < (now.getTime() + 5 * 60 * 1000))
        {
            if (! this.authenticate())
            {
                return null;
            }
        }
        return this.osToken.getXSubjectToken();
    }

    private void checkResponse(HttpStatus httpStatus, Object body)
    {
        if (httpStatus.equals(HttpStatus.BAD_REQUEST) || httpStatus.equals(HttpStatus.CONFLICT))
        {
            try
            {
                ErrorCode errorCode = ErrorCode.CUSTOM_ERROR;
                ((Map) body).forEach((key, value)-> {
                    Map resultMap = (Map) value;
                    if (resultMap.containsKey("code")) errorCode.setCode((int)resultMap.get("code"));
                    if (resultMap.containsKey("message")) errorCode.setMessage((String) resultMap.get("message"));
                });
                throw new WebSystemException(errorCode, ErrorLevel.INFO);
            }
            catch (WebSystemException e)
            {
                throw e;
            }
            catch (Exception e)
            {

            }
        }
        else if (httpStatus.equals(HttpStatus.UNAUTHORIZED))
        {
            ErrorCode errorCode = ErrorCode.CUSTOM_ERROR;
            errorCode.setMessage("Please retry later");
            throw new WebSystemException(errorCode, ErrorLevel.INFO);
        }
    }
}
