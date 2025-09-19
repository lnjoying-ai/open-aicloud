package com.lnjoying.justice.commonweb.controller;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
//import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//@Component
public class BaseErrorPageConfig implements ErrorPageRegistrar
{
    @Override
    public void registerErrorPages(ErrorPageRegistry registry)
    {
        ErrorPage[] errorPages = new ErrorPage[]
                                {
                                        new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"),
                                        new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401"),
                                        new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"),
                                        new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"),
                                        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"),
                                        new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error/405"),
                                        new ErrorPage(Throwable.class, "/error/500")
                                };
        registry.addErrorPages(errorPages);

    }

}
