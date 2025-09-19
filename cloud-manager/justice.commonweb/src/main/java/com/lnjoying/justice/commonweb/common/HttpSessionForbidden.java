package com.lnjoying.justice.commonweb.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class HttpSessionForbidden extends HttpServletRequestWrapper
{
    public HttpSessionForbidden(HttpServletRequest request)
    {
        super(request);
    }
    @Override
    public HttpSession getSession()
    {
        return null;
    }
    @Override
    public HttpSession getSession(boolean create)
    {
        return null;
    }
}