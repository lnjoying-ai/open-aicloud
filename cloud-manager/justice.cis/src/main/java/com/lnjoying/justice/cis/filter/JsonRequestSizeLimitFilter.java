package com.lnjoying.justice.cis.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonRequestSizeLimitFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonRequestSizeLimitFilter.class);

    private static final long MAX_JSON_POST_SIZE = 2 * 1024 * 1024L; // 2MB

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if (isApplicationJson(request) && request.getContentLengthLong() > MAX_JSON_POST_SIZE) {
            LOGGER.error("request content size exceeded limit of 2MB");
            response.sendError(HttpStatus.BAD_REQUEST.value(), "request content size exceeded limit of 2MB");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isApplicationJson(HttpServletRequest httpRequest) {
        if (httpRequest.getHeader(HttpHeaders.CONTENT_TYPE) != null) {
            return MediaType.APPLICATION_JSON
                .isCompatibleWith(MediaType.parseMediaType(httpRequest.getHeader(HttpHeaders.CONTENT_TYPE)));
        }
        return false;
    }
}
