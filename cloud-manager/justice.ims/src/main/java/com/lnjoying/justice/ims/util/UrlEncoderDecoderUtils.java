package com.lnjoying.justice.ims.util;

import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.lnjoying.justice.schema.common.ErrorCode.DECODE_FAILED;
import static com.lnjoying.justice.schema.common.ErrorCode.ENCODE_FAILED;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * URLEncoder utils
 *
 * @author merak
 **/

public class UrlEncoderDecoderUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Base64Utils.class);

    public static String encodeUtf8(String plainText)
    {
        try
        {
            return URLEncoder.encode(plainText, StandardCharsets.UTF_8.toString());
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("encode text failed!", e);
            throw new ImsWebSystemException(ENCODE_FAILED, INFO);
        }
    }

    public static String decodeUtf8(String encodeText)
    {
        try
        {
            return URLDecoder.decode(encodeText, StandardCharsets.UTF_8.toString());
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("decode text failed!", e);
            throw new ImsWebSystemException(DECODE_FAILED, INFO);
        }
    }
}
