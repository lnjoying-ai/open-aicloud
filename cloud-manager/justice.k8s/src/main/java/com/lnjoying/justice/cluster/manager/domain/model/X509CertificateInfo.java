/**
 * cache certificate,privatekey, configfile. the value will be passed by container env to deploy on edge
 */
package com.lnjoying.justice.cluster.manager.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;


@Data
public class X509CertificateInfo implements Serializable
{
    private transient PrivateKey privateKey;
    
    private transient PublicKey publicKey;
    
    private transient X509Certificate certificate;
    
    @SerializedName("key_pem")
    @JsonProperty("key_pem")
    private String               keyPem;
    
    @SerializedName("certificate_pem")
    @JsonProperty("certificate_pem")
    private String       certificatePem;
    
    @SerializedName("config")
    @JsonProperty("config")
    private String               config;
    
    @SerializedName("name")
    @JsonProperty("name")
    private String                 name;
    
    @SerializedName("common_name")
    @JsonProperty("common_name")
    private String           commonName;
    
    @SerializedName("cert_env_name")
    @JsonProperty("cert_env_name")
    private String          certEnvName;
    
    //organization name
    private String               ouName;
    
    @SerializedName("cert_path")
    @JsonProperty("cert_path")
    private String             certPath;
    
    @SerializedName("key_env_name")
    @JsonProperty("key_env_name")
    private String           keyEnvName;
    
    @SerializedName("key_path")
    @JsonProperty("key_path")
    private String              keyPath;
    
    @SerializedName("config_env_name")
    @JsonProperty("config_env_name")
    private String        configEnvName;
    
    @SerializedName("config_path")
    @JsonProperty("config_path")
    private String           configPath;
    
    private List<String> dnsNames;
    private List<String> iPAddresses;
}
