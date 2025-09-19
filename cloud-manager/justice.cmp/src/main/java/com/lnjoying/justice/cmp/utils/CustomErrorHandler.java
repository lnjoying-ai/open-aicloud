package com.lnjoying.justice.cmp.utils;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomErrorHandler implements ResponseErrorHandler
{
    @Override
    public boolean hasError(@NonNull ClientHttpResponse clientHttpResponse) throws IOException
    {
        return true;
    }

    @Override
    public void handleError(@NonNull ClientHttpResponse clientHttpResponse) throws IOException
    {
        return;
    }
}
