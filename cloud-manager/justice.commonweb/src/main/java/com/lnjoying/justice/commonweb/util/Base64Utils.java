package com.lnjoying.justice.commonweb.util;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;

import static com.lnjoying.justice.schema.common.ErrorCode.DECODE_FAILED;
import static com.lnjoying.justice.schema.common.ErrorCode.ENCODE_FAILED;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/17 14:57
 */

public class Base64Utils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Base64Utils.class);

    public static String encode(String plaintext)
    {
        try
        {
            String res = "";
            if (StringUtils.isNotBlank(plaintext))
            {
                res = Base64.getEncoder().encodeToString(plaintext.getBytes("utf-8"));
            }
            return res;
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("encode text failed!", e);
            throw new WebSystemException(ENCODE_FAILED, INFO);
        }
    }


    public static byte[] decode(String encodeText)
    {
        try
        {
            byte[] res = new byte[0];
            if (StringUtils.isNotBlank(encodeText))
            {
                res = Base64.getDecoder().decode(encodeText.getBytes("utf-8"));
            }
            return res;
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("decode text failed!", e);
            throw new WebSystemException(DECODE_FAILED, INFO);
        }
    }

    public static String decodeToString(String encodeText)
    {
        Charset charset = Charset.forName("UTF-8");
        byte[] decode = decode(encodeText);
        return new String(decode,charset);
    }
}
