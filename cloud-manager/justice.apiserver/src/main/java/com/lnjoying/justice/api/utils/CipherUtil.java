package com.lnjoying.justice.api.utils;

import com.lnjoying.justice.schema.entity.sys.NodeConfig;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
public class CipherUtil
{
	static byte [] private_key_byte;
	static byte[] private_prefix = {'c', 'r', 'y', 'p', 't', '.'};
	static byte[] pub_prefix = {'i', 'd', '.'};
	
	static ECKey ecKey = null;
	
	public static byte[] unitByteArray(byte[] byte1, byte[] byte2)
	{
		byte[] unitByte = new byte[byte1.length + byte2.length];
		System.arraycopy(byte1, 0, unitByte, 0, byte1.length);
		System.arraycopy(byte2, 0, unitByte, byte1.length, byte2.length);
		return unitByte;
	}

	public static String encodeChecked(byte[] payload)
	{
		byte[] addressBytes = new byte[payload.length + 4];
		System.arraycopy(payload, 0, addressBytes, 0, payload.length);
		byte[] checksum = Sha256Hash.hashTwice(payload, 0, payload.length);
		System.arraycopy(checksum, 0, addressBytes, payload.length, 4);
		return Base58.encode(addressBytes);
	}

	public static boolean checkSign(Map<String, String> exten_info, String nonce)
	{
		if (exten_info.get("sign") == null ||exten_info.get("sign_algo") == null
				|| exten_info.get("sign_at") == null || exten_info.get("origin_id") == null
				|| exten_info.get("sign").isEmpty() ||exten_info.get("sign_algo").isEmpty()
				|| exten_info.get("sign_at").isEmpty() || exten_info.get("origin_id").isEmpty())
		{
			return false;
		}

		String sign_msg =  nonce + exten_info.get("origin_id") +  exten_info.get("sign_at");
		return checkSign(sign_msg, exten_info.get("sign"), exten_info.get("origin_id"));
	}

	public static boolean checkSign(String message, String sign, String src_node_id)
	{
		try
		{
			byte[] msg_byte = message.getBytes();
			byte hash_message[] = Sha256Hash.hashTwice(msg_byte, 0, msg_byte.length);
			Sha256Hash _hash = Sha256Hash.wrap(hash_message);

			String r_sign_s = sign.substring(2, 66);
			String l_sign_s = sign.substring(66, 130);

			byte[] _sign = Utils.HEX.decode(sign.substring(0, 2));
			int recId = (_sign[0] - 27) & 3;
			BigInteger r_sign = new BigInteger(r_sign_s, 16);
			BigInteger l_sign = new BigInteger(l_sign_s, 16);
			ECKey.ECDSASignature _sig = new ECKey.ECDSASignature(r_sign, l_sign);
			ECKey recoveredKey = ECKey.recoverFromSignature(recId, _sig, _hash, true);

			byte[] pub = recoveredKey.getPubKey();
			byte[] key_id = Utils.sha256hash160(pub);
			byte[] node_info = unitByteArray(pub_prefix, key_id);
			String derived = encodeChecked(node_info);

			return derived.equals(src_node_id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static NodeConfig gen_node()
	{
		if (ecKey == null)
		{
			ecKey = new ECKey();
		}
		
		byte[] privateKeyBytes = ecKey.getPrivKeyBytes();
		;
		byte[] pub = ecKey.getPubKey();
		byte[] key_id = Utils.sha256hash160(pub);
		byte[] node_info = unitByteArray(pub_prefix, key_id);
		String node_id = encodeChecked(node_info);

		NodeConfig nodeConfig = new NodeConfig();
		nodeConfig.setRegion_id("admin");
		nodeConfig.setNode_id(node_id);
		nodeConfig.setCore_version("0.0.1");
		nodeConfig.setProtocol_version("0.0.1");
		nodeConfig.setNode_name("admin");
		nodeConfig.setPrivate_key(private_key_str(privateKeyBytes));
		private_key_byte = privateKeyBytes;
		return nodeConfig;
	}

	private static String private_key_str(byte [] srcKey)
	{
		byte[] key_byte = unitByteArray(private_prefix, srcKey);
		return encodeChecked(key_byte);
	}

	public static String sign(String message)
	{
		if (ecKey == null)
		{
			ecKey = ECKey.fromPrivate(private_key_byte);
		}
		
		byte[] msg_byte = message.getBytes();
		byte hash_message[] = Sha256Hash.hashTwice(msg_byte, 0, msg_byte.length);
		Sha256Hash _hash = Sha256Hash.wrap(hash_message);

		ECKey.ECDSASignature sig = ecKey.sign(_hash);
		byte recId = ecKey.findRecoveryId(_hash, sig);
		int headerByte = recId + 27 + (ecKey.isCompressed() ? 4 : 0);
		byte[] sigData = new byte[65];
		sigData[0] = (byte)headerByte;
		System.arraycopy(Utils.bigIntegerToBytes(sig.r, 32), 0, sigData, 1, 32);
		System.arraycopy(Utils.bigIntegerToBytes(sig.s, 32), 0, sigData, 33, 32);
		return Utils.HEX.encode(sigData);
	}

	public static Map<String, String> sign_exten(String message, String node_id)
	{
		Map<String, String> exten_map = new HashMap<>();
		String sigtAt = String.valueOf(System.currentTimeMillis());
		String sig = sign(message+sigtAt);
		exten_map.put("origin_id", node_id);
		exten_map.put("sign_at", sigtAt);
		exten_map.put("sign",sig);
		exten_map.put("sign_algo","ecdsa");
		return exten_map;
	}

	public static void import_private_key(String privatekey_str)
	{
		byte [] pri_byte = Base58.decodeChecked(privatekey_str);
		private_key_byte = new byte[pri_byte.length - private_prefix.length];
		System.arraycopy(pri_byte, private_prefix.length, private_key_byte, 0, pri_byte.length - private_prefix.length);
		if (ecKey == null)
		{
			ecKey = ECKey.fromPrivate(private_key_byte);
		}
	}
}
