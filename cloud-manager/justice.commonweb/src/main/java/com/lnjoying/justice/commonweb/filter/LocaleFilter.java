package com.lnjoying.justice.commonweb.filter;

import com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.vertx.core.MultiMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.foundation.vertx.http.HttpServletResponseEx;
import org.apache.servicecomb.foundation.vertx.http.StandardHttpServletRequestEx;
import org.apache.servicecomb.foundation.vertx.http.VertxServerRequestToHttpServletRequest;
import org.apache.servicecomb.swagger.invocation.Response;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

/**
 * @Description: Use URLDecoder.decode to decode bpName and userName to solve the problem of Chinese header
 * @Author: Merak
 * @Date: 2022/6/21 11:07
 */

@Slf4j
public class LocaleFilter implements HttpServerFilter
{


    @Override
    public int getOrder()
    {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @SneakyThrows
    @Override
    public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx requestEx)
    {
        try
        {
            if (invocation.isEdge())
            {
                return null;
            }

            String acceptLanguage = requestEx.getHeader("Accept-Language");
            Locale customLocale = parseLocaleFromHeader(acceptLanguage);
            LocaleContextHolder.setLocale(customLocale, true);
        }
        catch (Exception e)
        {
            // do nothing
        }

        return null;
    }

    @Override
    public void beforeSendResponse(Invocation invocation, HttpServletResponseEx responseEx)
    {
        LocaleContextHolder.resetLocaleContext();
    }


    private Locale parseLocaleFromHeader(String header) {
        if (header != null) {
            String[] parts = header.split(",");
            String localePart = parts[0].trim();
            return Locale.forLanguageTag(localePart.replace('_', '-'));
        }
        return Locale.getDefault();
    }
}
