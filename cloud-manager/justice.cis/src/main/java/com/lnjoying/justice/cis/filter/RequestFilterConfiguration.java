package com.lnjoying.justice.cis.filter;

import com.lnjoying.justice.commonweb.filter.AccessFilter;
import com.lnjoying.justice.commonweb.filter.HttpTraceLogFilter;
import com.lnjoying.justice.commonweb.filter.RequestHolder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;

@Configuration
public class RequestFilterConfiguration {

    /**
     * Register log filter.
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> authFilterRegistrationBean() {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new HttpTraceLogFilter());
        registration.addUrlPatterns("/cis/v1/*");
        registration.setName("HttpTraceLogFilter");
        registration.setOrder(0);
        return registration;
    }

    /**
     * Registers json request size filter.
     *
     * @return instance of filter registration bean
     */
    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> jsonRequestSizeFilterRegistrationBean() {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JsonRequestSizeLimitFilter());
        registration.addUrlPatterns("/cis/v1/*");
        registration.setName("JsonRequestSizeLimitFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> accessTokenFilterRegistrationBean() {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AccessFilter());
        registration.setName("accessTokenFilters");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> accessHolederRegistrationBean() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestHolder());
        registration.setName("RequestHolder");
        registration.setOrder(3);
        return registration;
    }
}
