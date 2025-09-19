package com.lnjoying.justice.servicegw.util;

import io.netty.handler.codec.spdy.SpdyVersion;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 5/27/22 11:03 PM
 */
public class SpdyUtil
{
    public static SpdyVersion getSpdyVersion(String version)
    {
        switch (version)
        {
            case "SPDY/3.1":
                return SpdyVersion.SPDY_3_1;
            default:
                return SpdyVersion.SPDY_3_1;
        }
    }
}
