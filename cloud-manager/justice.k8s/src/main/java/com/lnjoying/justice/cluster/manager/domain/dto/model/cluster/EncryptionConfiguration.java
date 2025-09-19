package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/14/21 3:31 PM
 * @Description:
 */
@Data
public class EncryptionConfiguration implements Serializable
{
    @ApiModelProperty(example = "{}")
    @SerializedName("type_meta")
    @JsonProperty("type_meta")
    private  TypeMeta typeMeta;
    
    @ApiModelProperty(example = "[]")
    @SerializedName("resources")
    @JsonProperty("resources")
    private List<ResourceConfiguration> resources;
}
