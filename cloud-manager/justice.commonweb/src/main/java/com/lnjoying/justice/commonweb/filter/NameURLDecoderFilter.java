package com.lnjoying.justice.commonweb.filter;

import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.vertx.core.MultiMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.foundation.vertx.http.StandardHttpServletRequestEx;
import org.apache.servicecomb.foundation.vertx.http.VertxServerRequestToHttpServletRequest;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.springframework.core.Ordered;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import static com.lnjoying.justice.schema.constant.UserHeadInfo.AUTIORITIES;

/**
 * @Description: Use URLDecoder.decode to decode bpName and userName to solve the problem of Chinese header
 * @Author: Merak
 * @Date: 2022/6/21 11:07
 */

@Slf4j
public class NameURLDecoderFilter implements HttpServerFilter
{

    private static final String[] EXCLUDE_MICROSERVICE_NAME = new String[]{"ums"};

    @Override
    public int getOrder()
    {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    @SneakyThrows
    @Override
    public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx requestEx)
    {
        if (invocation.isEdge())
        {
            return null;
        }

        boolean skip = Arrays.asList(EXCLUDE_MICROSERVICE_NAME).contains(invocation.getMicroserviceName());
        if (skip)
        {
            return null;
        }

        String userName = requestEx.getHeader(UserHeadInfo.USERNAME);
        String decodeUserName = null;
        if (StringUtils.isNotBlank(userName))
        {
            decodeUserName = URLDecoder.decode(userName, StandardCharsets.UTF_8.name());
        }

        String bpName = requestEx.getHeader(UserHeadInfo.BpName);
        String decodeBpName = null;
        if (StringUtils.isNotBlank(bpName))
        {
            decodeBpName = URLDecoder.decode(bpName, StandardCharsets.UTF_8.name());
        }

        try
        {
            if (requestEx instanceof VertxServerRequestToHttpServletRequest)
            {
                MultiMap headers = ((VertxServerRequestToHttpServletRequest) requestEx).getContext().request().headers();
                if (StringUtils.isNotEmpty(decodeUserName) && headers.contains(UserHeadInfo.USERNAME))
                {
                    headers.remove(UserHeadInfo.USERNAME);
                    headers.add(UserHeadInfo.USERNAME, decodeUserName);
                }

                if (StringUtils.isNotEmpty(decodeUserName) && headers.contains(UserHeadInfo.BpName))
                {
                    headers.remove(UserHeadInfo.BpName);
                    headers.add(UserHeadInfo.BpName, decodeBpName);
                }
            }
            else if (requestEx instanceof StandardHttpServletRequestEx)
            {
                StandardHttpServletRequestEx request = (StandardHttpServletRequestEx) requestEx;
                if (StringUtils.isNotBlank(request.getHeader(UserHeadInfo.USERNAME)))
                {
                    request.setHeader(UserHeadInfo.USERNAME, decodeUserName);
                }

                if (StringUtils.isNotBlank(request.getHeader(UserHeadInfo.BpName)))
                {
                    request.setHeader(UserHeadInfo.BpName, decodeBpName);
                }
            }
        }
        catch (Exception e)
        {
            log.error("set encode header error:{}", e);
        }

        return null;
    }
}
