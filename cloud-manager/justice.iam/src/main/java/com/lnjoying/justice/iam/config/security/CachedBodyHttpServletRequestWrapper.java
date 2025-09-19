package com.lnjoying.justice.iam.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/8/22 16:25
 */
@Slf4j
public class CachedBodyHttpServletRequestWrapper extends HttpServletRequestWrapper
{
    private final byte[] cachedBody;

    public CachedBodyHttpServletRequestWrapper(HttpServletRequest request)
    {
        super(request);
        byte[] cachedBody1;
        try
        {
            cachedBody1 = IOUtils.toByteArray(request.getInputStream());
        }
        catch (IOException e)
        {
            cachedBody1 = new byte[0];
            log.error("getInputStream error", e);
        }
        cachedBody = cachedBody1;

    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedBodyServletInputStream(cachedBody);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream, StandardCharsets.UTF_8));
    }

    private static class CachedBodyServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream byteArrayInputStream;

        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.byteArrayInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            return byteArrayInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            // No action required
        }

        @Override
        public int read() throws IOException {
            return byteArrayInputStream.read();
        }
    }

}
