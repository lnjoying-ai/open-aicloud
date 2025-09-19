package com.lnjoying.justice.cluster.manager.domain.dto.model.admission;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 12/15/21 11:04 AM
 * @Description:
 */
@Data
public class RunTimeUnknow implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("raw")
    @JsonProperty("raw")
    private  byte []            raw;
    
    @ApiModelProperty(example = "")
    @SerializedName("content_type")
    @JsonProperty("content_type")
    private String     contentType;
    
    @ApiModelProperty(example = "")
    @SerializedName("content_encoding")
    @JsonProperty("content_encoding")
    private String contentEncoding;
}
