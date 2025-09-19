package com.lnjoying.justice.commonweb.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * request utils
 *
 * @author merak
 **/

public class RequestUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtils.class);

    private static final String HTTP = "http";

    private static final String HTTPS = "https";

    private static final int PORT_80 = 80;

    private static final int PORT_443 = 443;

    private static final String UNKNOWN = "unknown";

    /**
     * Remove request specified parameters
     *
     * @param request
     * @param paramName
     * @return
     */
    public String removeParam(HttpServletRequest request, String paramName)
    {
        String queryString = "";
        Enumeration keys = request.getParameterNames();
        while (keys.hasMoreElements())
        {
            String key = (String) keys.nextElement();
            if (key.equals(paramName))
            {
                continue;
            }
            if ("".equals(queryString))
            {
                queryString = key + "=" + request.getParameter(key);
            }
            else
            {
                queryString += "&" + key + "=" + request.getParameter(key);
            }
        }
        return queryString;
    }

    /**
     * Get request base Path
     *
     * @param request
     * @return
     */
    public static String getBasePath(HttpServletRequest request)
    {
        StringBuffer basePath = new StringBuffer();
        String scheme = request.getScheme();
        String domain = request.getServerName();
        int port = request.getServerPort();
        basePath.append(scheme);
        basePath.append("://");
        basePath.append(domain);
        if (HTTP.equalsIgnoreCase(scheme) && PORT_80 != port)
        {
            basePath.append(":").append(String.valueOf(port));
        }
        else if (HTTPS.equalsIgnoreCase(scheme) && port != PORT_443)
        {
            basePath.append(":").append(String.valueOf(port));
        }
        return basePath.toString();
    }

    public static final List<String> CLIENT_IP_HEADER_NAMES_SET = new ArrayList<>();
    static {
        CLIENT_IP_HEADER_NAMES_SET.add("X-Forwarded-For");
        CLIENT_IP_HEADER_NAMES_SET.add("x-forwarded-for");
        CLIENT_IP_HEADER_NAMES_SET.add("X-real-ip");
        CLIENT_IP_HEADER_NAMES_SET.add("Proxy-Client-IP");
        CLIENT_IP_HEADER_NAMES_SET.add("WL-Proxy-Client-IP");
    }

    /**
     * Get ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request)
    {
        if (request == null)
        {
            return "";
        }
        String ip = null;
        for (String headerName : CLIENT_IP_HEADER_NAMES_SET)
        {
            ip = request.getHeader(headerName);
            LOGGER.debug("Getting client ip from the header {}, the value is {}.", headerName, ip);
            if (!StringUtils.isEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip))
            {
                break;
            }
        }

        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }

        // the first IP is the real IP
        if (!StringUtils.isEmpty(ip))
        {
            return ip.split(",")[0];
        }
        else
        {
            return UNKNOWN;
        }
    }
}
