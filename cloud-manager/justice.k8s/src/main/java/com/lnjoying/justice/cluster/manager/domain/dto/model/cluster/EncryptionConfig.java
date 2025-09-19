package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 11/19/21 7:05 PM
 * @Description:
 */
@Data
public class EncryptionConfig implements Serializable
{
    @JsonProperty("rewrite_secrets")
    private  Boolean rewriteSecrets = false;
    
    @JsonProperty("rotate_key")
    private Boolean rotateKey = false;
    
    @JsonProperty("encryption_provider_file")
    private  String  encryptionProviderFile;
}
