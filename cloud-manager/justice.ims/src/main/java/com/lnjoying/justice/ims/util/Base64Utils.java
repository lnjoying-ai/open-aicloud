package com.lnjoying.justice.ims.util;

import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static com.lnjoying.justice.schema.common.ErrorCode.ENCODE_FAILED;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * base64 tools
 *
 * @author merak
 **/

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
            throw new ImsWebSystemException(ENCODE_FAILED, INFO);
        }
    }
}
