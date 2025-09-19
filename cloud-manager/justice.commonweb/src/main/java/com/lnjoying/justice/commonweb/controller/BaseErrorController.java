package com.lnjoying.justice.commonweb.controller;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Controller
//@RequestMapping("/error")
public class BaseErrorController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();
    @PostMapping(value = "/400")
    public String error_400()
    {
        throw new WebSystemException(ErrorCode.BAD_REQUST, ErrorLevel.INFO);
    }

    @PostMapping(value = "/401")
    public String error_401()
    {
        throw new WebSystemException(ErrorCode.InvalidAuthority, ErrorLevel.INFO);
    }

    @PostMapping(value = "/402")
    public String error_402()
    {
        throw new WebSystemException(ErrorCode.InvalidAuthority, ErrorLevel.INFO);
    }

    @PostMapping(value = "/403")
    public void error_403()
    {
        throw new WebSystemException(ErrorCode.InvalidAuthority, ErrorLevel.INFO);
    }

    @PostMapping(value = "/404")
    public String error_404()
    {
        throw new WebSystemException(ErrorCode.UNKNOW_SERVICE, ErrorLevel.INFO);
    }

    @PostMapping(value = "/405")
    public String error_405()
    {
        throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
    }

    @PostMapping(value = "/500")
    public String error_500()
    {
        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
    }
}
