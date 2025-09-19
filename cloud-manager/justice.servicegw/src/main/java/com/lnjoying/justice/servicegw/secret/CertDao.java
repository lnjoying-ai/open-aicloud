package com.lnjoying.justice.servicegw.secret;

import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface CertDao
{
	X509Certificate getX509Cert(String certStr) throws CertificateException;
	PrivateKey getPrivatekey(String privatKeyStr) throws Exception;
}