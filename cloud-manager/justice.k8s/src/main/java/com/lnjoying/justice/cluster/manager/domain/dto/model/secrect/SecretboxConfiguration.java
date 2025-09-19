package com.lnjoying.justice.cluster.manager.domain.dto.model.secrect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/14/21 3:38 PM
 * @Description:
 */
@Data
public class SecretboxConfiguration implements Serializable
{
    @ApiModelProperty(example = "[]")
    @SerializedName("keys")
    @JsonProperty("keys")
    private List<EncryptionKey> keys;
}
