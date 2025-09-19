package com.lnjoying.justice.aos.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.lnjoying.justice.aos.config.SecurityModeConfig;
import com.lnjoying.justice.commonweb.util.AESUtils;
import org.apache.commons.lang.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * Encryption utils using AES
 *
 * @author merak
 **/

public class AesCryptoUtils
{
    /**
     * Only 128 bit keys can be used, 256 keys cannot be used,
     * otherwise it may be thrownIllegal key size or default parameters
     */
    private static final String key = "4ae0564783f74b14";

    private AesCryptoUtils()
    {
    }

    ;

    /**
     * Encryption, using UTF-8 encoding
     *
     * @param content
     * @return Encrypted Hex
     */
    public static String encryptHex(String content)
    {
        if (StringUtils.isEmpty(content))
        {
            content = "";
        }
        return getInstance().encryptHex(content);
    }

    /**
     * Decrypt the string represented by Hex (hexadecimal) or Base64, the default UTF-8 encoding
     *
     * @param encryptHex
     * @return Decrypted String
     */
    public static String decryptStr(String encryptHex)
    {
        return getInstance().decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
    }

    public static SymmetricCrypto getInstance()
    {
        return Holder.INSTANCE;
    }

    private static class Holder
    {
        static SymmetricCrypto INSTANCE = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes(StandardCharsets.UTF_8));
    }

    public static String cbcEncryptHex(String content)
    {
        if (SecurityModeConfig.securityMode)
        {
            return  AESUtils.cbcEncrypt(content, SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv);
        }

        return content;
    }

    public static String cbcDecryptStr(String encryptHex)
    {
        if (SecurityModeConfig.securityMode)
        {
            return AESUtils.cbcDecrypt(encryptHex, SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv);
        }

        return encryptHex;
    }
}
