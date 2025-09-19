package com.lnjoying.justice.commonweb.filter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lnjoying.justice.schema.constant.UserHeadInfo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

public class HttpTraceLogFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpTraceLogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        LOGGER.info("log filter authorites: {} user Id: {}", request.getHeader(UserHeadInfo.AUTIORITIES), request.getHeader(UserHeadInfo.USERID));

        String accessId = UUID.randomUUID().toString();

        try
        {
            logForRequest(accessId, request);
            filterChain.doFilter(request, response);
        }
        finally
        {
            logForResponse(accessId, response);
//            updateResponse(response);
        }
    }

    private void logForRequest(String accessId, HttpServletRequest request) {
        HttpRequestTraceLog requestTraceLog = new HttpRequestTraceLog();
        requestTraceLog.setAccessId(accessId);
        requestTraceLog.setTime(LocalDateTime.now().toString());
        requestTraceLog.setPath(request.getRequestURI());
        requestTraceLog.setMethod(request.getMethod());
        requestTraceLog.setParameterMap(new Gson().toJson(request.getParameterMap()));
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Http request trace log: {}", new Gson().toJson(requestTraceLog));
        }
    }

    private void logForResponse(String accessId, HttpServletResponse response) {
        HttpResponseTraceLog responseTraceLog = new HttpResponseTraceLog();
        responseTraceLog.setAccessId(accessId);


        responseTraceLog.setStatus(response.getStatus());
        responseTraceLog.setTime(LocalDateTime.now().toString());
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Http response trace log: {}", new Gson().toJson(responseTraceLog));
        }
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils
            .getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (responseWrapper != null) {
            responseWrapper.copyBodyToResponse();
        }
    }

    private String getErrorMessage(ContentCachingResponseWrapper response) {
        ContentCachingResponseWrapper wrapper = WebUtils
            .getNativeResponse(response, ContentCachingResponseWrapper.class);
        String result = "";
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    String payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                    logger.error("read paylod is" + payload);
                    JsonElement element = new JsonParser().parse(payload).getAsJsonObject().get("message");
                    if (element != null && !element.isJsonNull()) {
                        result = element.getAsString();
                    }
                } catch (UnsupportedEncodingException e) {
                    result = "read response body exception";
                }
            }
        }
        return result;
    }

    @Setter
    @Getter
    private static class HttpRequestTraceLog {
        private String accessId;

        private String path;

        private String userId;

        private String parameterMap;

        private String method;

        private String time;

        private String requestBody;
    }

    @Setter
    @Getter
    private static class HttpResponseTraceLog {
        private String accessId;

        private Integer status;

        private String time;

        private String body;

        /**
         * bad or good response.
         *
         * @return ture of false
         */
        public boolean isGoodResponse() {
            return status == HttpStatus.OK.value();
        }
    }
}
