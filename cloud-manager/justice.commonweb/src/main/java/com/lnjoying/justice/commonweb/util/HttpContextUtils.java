package com.lnjoying.justice.commonweb.util;

import javax.servlet.http.HttpServletRequest;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * request context tool
 *
 */
public class HttpContextUtils
{
	public static String getStrAttribute(String key)
	{
		HttpServletRequest request = getHttpServletRequest();
		return (String)request.getAttribute(key);
	}

	public static HttpServletRequest getHttpServletRequest()
	{
		HttpServletRequest request = null;
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null != attributes)
		{
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		}
		if (request == null)
		{
			request = (((HttpServletRequest)(ContextUtils.getInvocationContext()).getLocalContext().get("servicecomb-rest-request")));
		}
		return request;
	}

	public static Integer getIntAttribute(String key)
	{
		HttpServletRequest request = getHttpServletRequest();
		return (Integer)request.getAttribute(key);
	}
}

