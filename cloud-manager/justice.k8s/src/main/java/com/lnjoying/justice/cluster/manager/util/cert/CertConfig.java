package com.lnjoying.justice.cluster.manager.util.cert;

import lombok.Data;
import lombok.experimental.PackagePrivate;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 11/17/21 10:08 AM
 * @Description:
 */
@Data
@PackagePrivate
public class CertConfig
{
    X500Name         issuer;
    X500Name         subjectDn;
    Date             notBefore;
    Date             notAfter;
    BigInteger       serial;
    String           keyPassword;
    String           alias;
    KeyUsage         keyUsage;
    List<String>     dnsNames;
    List<String>     ipAddresses;
    ExtendedKeyUsage extendedKeyUsage;
    Boolean          isCa = false;
}
