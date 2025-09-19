package com.lnjoying.justice.iam.config;

import com.lnjoying.justice.commonweb.util.FileUtils;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Decoder;

import java.io.IOException;

@Data
public class SecurityModeConfig
{
    private static Logger LOGGER = LogManager.getLogger();

    public static boolean securityMode;
    public static byte[] aesCbcKey;
    public static byte[] aesCbcIv;

    public static void loadSecurityModeConfig()
    {
        securityMode = false;
        try
        {
            loadAesCbcKey();
            loadAesCbcIv();
            if (aesCbcKey.length > 0 && aesCbcIv.length > 0)
            {
                securityMode = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        LOGGER.info("Security Mode: {}", securityMode);
    }

    private static void loadAesCbcKey() throws IOException
    {
        String aesCbcKeyBase64 = FileUtils.getContentFromBaseConfigPathResource("/root/ssl/lnjoying.key");
        aesCbcKey = new BASE64Decoder().decodeBuffer(aesCbcKeyBase64);
    }

    private static void loadAesCbcIv() throws IOException
    {
        String aesCbcIvBase64 = FileUtils.getContentFromBaseConfigPathResource("/root/ssl/lnjoying.iv");
        aesCbcIv = new BASE64Decoder().decodeBuffer(aesCbcIvBase64);
    }
}
