package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 11/19/21 8:14 PM
 * @Description:
 */
@Data
public class Configuration implements Serializable
{
    @ApiModelProperty(example = "{}")
    @SerializedName("type_meta")
    @JsonProperty("type_meta")
    private TypeMeta typeMeta;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("configuration")
    @JsonProperty("configuration")
    private  List<Limit> limits;
}
