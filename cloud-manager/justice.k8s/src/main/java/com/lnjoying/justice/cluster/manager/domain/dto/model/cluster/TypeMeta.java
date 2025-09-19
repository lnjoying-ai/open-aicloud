package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 12/9/21 3:04 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeMeta implements Serializable
{
    @ApiModelProperty(example = "")
    @JsonProperty("kind")
    @SerializedName("kind")
    private String kind;
    
    @ApiModelProperty(example = "")
    @JsonProperty("api_version")
    @SerializedName("apiVersion")
    private String apiVersion;
}
