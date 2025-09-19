package com.lnjoying.justice.ecrm.common;

import com.micro.core.common.Utils;
import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GoogleCodeTool
{
	static byte [] googleKey = null;
	static byte[] private_prefix = {'p', 'v', 'm', 'p', 't', 'a','w'};

	public static byte[] unitByteArray(byte[] byte1, byte[] byte2)
	{
		byte[] unitByte = new byte[byte1.length + byte2.length];
		System.arraycopy(byte1, 0, unitByte, 0, byte1.length);
		System.arraycopy(byte2, 0, unitByte, byte1.length, byte2.length);
		return unitByte;
	}

	public static String generateSecretKey() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		long seed = Utils.getRandomByRangeL(0xFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL);
		sr.setSeed(seed);
		googleKey = sr.generateSeed(10);
		Base32 codec = new Base32();
		byte[] bEncodedKey = codec.encode(unitByteArray(private_prefix, googleKey));
		return new String(bEncodedKey);
	}

	public static int generateCode() throws InvalidKeyException, NoSuchAlgorithmException
	{
		long t = (System.currentTimeMillis() / 1000L) / 30L;
		return generateCode(t);
	}

	public static int generateCode(long t) throws NoSuchAlgorithmException, InvalidKeyException
	{
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}
		SecretKeySpec signKey = new SecretKeySpec(googleKey, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);
		int offset = hash[20 - 1] & 0xF;
		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i)
		{
			truncatedHash <<= 8;
			// We are dealing with signed bytes:
			// we just keep the first byte.
			truncatedHash |= (hash[offset + i] & 0xFF);
		}
		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;
		return (int) truncatedHash;
	}

	//permit 10min offset
	static int window_size = 200; //20*300=600s
	public static boolean check_code(long code, long timeMsec)
	{
		long t = (timeMsec / 1000L) / 30L; //30s
		for (int i = -window_size; i <= window_size; ++i) {
			long hash;
			try
			{
				hash = generateCode(t + i);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			if (hash == code)
			{
				return true;
			}
		}
		return false;
	}
}
