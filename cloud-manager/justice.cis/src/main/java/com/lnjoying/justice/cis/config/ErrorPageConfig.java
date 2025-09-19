package com.lnjoying.justice.cis.config;

import com.lnjoying.justice.commonweb.controller.BaseErrorPageConfig;
import org.springframework.stereotype.Component;

@Component
public class ErrorPageConfig extends BaseErrorPageConfig
{
}
//
//public class ErrorPageConfig implements ErrorPageRegistrar
//{
//    @Override
//    public void registerErrorPages(ErrorPageRegistry registry)
//    {
//        ErrorPage[] errorPages = new ErrorPage[]
//                                {
//                                        new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401"),
//                                        new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"),
//                                        new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"),
//                                        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"),
//                                        new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error/405"),
//                                        new ErrorPage(Throwable.class, "/error/500")
//                                };
//        registry.addErrorPages(errorPages);
//
//    }
//
//}
