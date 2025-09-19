package com.lnjoying.justice.cluster.manager.domain.dto.model.secrect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 12/14/21 3:37 PM
 * @Description:
 */
@Data
public class EncryptionKey implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("name")
    @JsonProperty("name")
    private String name;
    
    @ApiModelProperty(example = "")
    @SerializedName("secret")
    @JsonProperty("secret")
    private String secret;
}
