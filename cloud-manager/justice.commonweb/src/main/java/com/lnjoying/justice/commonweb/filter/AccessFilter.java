package com.lnjoying.justice.commonweb.filter;

import com.lnjoying.justice.schema.constant.UserHeadInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);

    private static ThreadLocal<HttpServletRequest> httpServletRequestHolder =
            new ThreadLocal<HttpServletRequest>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        LOGGER.info("access filter authorites: {} user Id: {}", request.getHeader(UserHeadInfo.AUTIORITIES), request.getHeader(UserHeadInfo.USERID));
        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        filterChain.doFilter(request, response);
    }
}
