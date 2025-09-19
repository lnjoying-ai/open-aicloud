package com.lnjoying.justice.iam.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lnjoying.justice.iam.common.constant.AuthType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class SmsAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //attempt Authentication when Content-Type is json
        String content_type =  request.getContentType();
        if (content_type != null && content_type.toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE))
        {
            //use jackson to deserialize json
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream())
            {
                SmsAuthenticationBean authenticationBean = mapper.readValue(is,SmsAuthenticationBean.class);
                request.setAttribute(UserHeadInfo.USERNAME, authenticationBean.getPhone());
                authRequest = new UsernamePasswordAuthenticationToken(
                        AuthType.SMS_AUTH_PREFIX+authenticationBean.getPhone(), authenticationBean.getCode());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
            }
            finally
            {
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }

        //transmit it to UsernamePasswordAuthenticationFilter
        else {
            return super.attemptAuthentication(request, response);
        }
    }

}