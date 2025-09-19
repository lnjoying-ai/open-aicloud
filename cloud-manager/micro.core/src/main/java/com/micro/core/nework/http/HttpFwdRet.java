package com.micro.core.nework.http;

import lombok.Data;
import org.apache.http.Header;

import java.io.InputStream;

@Data
public class HttpFwdRet
{
    Integer status;
    Header[] headers;
    byte [] stream;
    InputStream iStream;
}
