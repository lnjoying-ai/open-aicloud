package com.lnjoying.justice.cluster.manager.domain.dto.model.secrect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 12/14/21 3:35 PM
 * @Description:
 */
@Data
public class ProviderConfiguration implements Serializable
{
    @ApiModelProperty(example = "{}")
    @SerializedName("aesgcm")
    @JsonProperty("aesgcm")
    private AESConfiguration aesgcm;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("aescbc")
    @JsonProperty("aescbc")
    private AESConfiguration aescbc;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("secretbox")
    @JsonProperty("secretbox")
    private SecretboxConfiguration secretbox;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("identity")
    @JsonProperty("identity")
    private IdentityConfiguration identity;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("kms")
    @JsonProperty("kms")
    private KMSConfiguration kms;
}
