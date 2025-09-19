package com.lnjoying.justice.commonweb.util;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
public class AESUtils
{
    /**
     * @Description:  AES ECB Encrypt
     * @Author:
     * @Date: 2022/10/21 09:53
     * @Return: java.lang.String
     */
    public static String ecbEncrypt(String data, byte[] key)
    {
        try
        {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return new BASE64Encoder().encode(encrypted);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description:  AES ECB Decrypt
     * @Author:
     * @Date: 2022/10/21 09:53
     * @Return: java.lang.String
     */
    public static String ecbDecrypt(String data, byte[] key)
    {
        try
        {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] original = cipher.doFinal(new BASE64Decoder().decodeBuffer(data));
            return new String(original,"utf-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description:  AES CBC Encrypt
     * @Author:
     * @Date: 2022/10/21 09:56
     * @Return: java.lang.String
     */
    public static String cbcEncrypt(String data, byte[] key, byte[] iv)
    {
        try
        {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0)
            {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(plaintext);

            return new BASE64Encoder().encode(encrypted).trim();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description:  AES CBC Decrypt
     * @Author:
     * @Date: 2022/10/21 10:00
     * @Return: java.lang.String
     */
    public static String cbcDecrypt(String data, byte[] key, byte[] iv)
    {
        try
        {
            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(new BASE64Decoder().decodeBuffer(data));
            return new String(original).trim();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args)
    {
        System.out.println(cbcEncrypt("hello", "asdfghjklqwertyu".getBytes(), "1234567890qwerty".getBytes()));
    }
} 