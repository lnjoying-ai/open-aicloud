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
 * @Date: 11/19/21 8:17 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog implements Serializable
{
    @ApiModelProperty(example = "false")
    @SerializedName("enable")
    @JsonProperty("enable")
    private  Boolean enable;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("configuration")
    @JsonProperty("configuration")
    private  AuditLogConfig configuration;
}
