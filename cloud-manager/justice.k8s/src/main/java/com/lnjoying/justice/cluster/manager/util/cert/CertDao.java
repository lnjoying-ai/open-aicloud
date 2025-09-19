package com.lnjoying.justice.cluster.manager.util.cert;

import org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public interface CertDao
{

	/**
	 * @description 
	 * @author Regulus
	 * @date 11/17/21 10:14 AM
	 * @param keyPair
 * @param certConfig
	 * @return java.security.cert.X509Certificate
	 */
	X509Certificate createCert(KeyPair keyPair, CertConfig certConfig) throws Exception;
	X509Certificate createCert(X509Certificate certificate, KeyPair keyPair, PrivateKey privateKey, CertConfig certConfig) throws Exception;
	
	X500Name buildName(String commonName, String organization);
	X500Name buildName(String commonName, String organization, String organizationUnit, String locality,
	                          String state, String country);
	/** 输出证书信息
	 * @param certPath 证书地址
	 * @param keyPassword 证书密码
	 */
	void printCert(String certPath, String keyPassword) throws Exception;

	/** 返回公钥
	 * @param certPath 证书路径
	 * @param keyPassword 证书密码
	 * @return
	 * @throws Exception
	 */
	PublicKey getPublicKey(String certPath, String keyPassword) throws Exception;

	/** 返回私钥
	 * @param certPath
	 * @param keyPassword
	 * @return
	 * @throws Exception
	 */
	PrivateKey getPrivateKey(String certPath, String keyPassword) throws Exception;

	/**
	 * @param endTime 延期时间
	 * @param certPath 证书地址
	 * @param password 密码
	 * @throws Exception 目前未实现，
	 */
	void certDelayTo(Date endTime, String certPath, String password) throws Exception;

	/**修改密码
	 * @param certPath 证书地址 密码
	 * @param oldPwd 原始密码
	 * @param newPwd 新密码
	 * @throws Exception
	 */
	void changePassword(String certPath, String oldPwd, String newPwd) throws Exception;

	/** 删除证书
	 * @param certPath 证书地址
	 * @param password 密码
	 * @param alias 别名
	 * @param entry 条目
	 * @throws Exception
	 */
	void deleteAlias(String certPath, String password, String alias, String entry) throws Exception;

	String convertX509ToPemStr(X509Certificate x509Cert) throws Exception;

	String convertPrivateKey2Str(PrivateKey priv) throws Exception;

	KeyPair generateKeyPair() throws Exception;
	
	X509Certificate getX509Cert(String certStr) throws CertificateException;
	PrivateKey getPrivatekey(String privatKeyStr) throws Exception;
}