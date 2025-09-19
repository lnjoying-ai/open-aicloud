package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RollingUpdateDeployment implements Serializable
{
    @ApiModelProperty(required = true, example = "10")
    @SerializedName("max_surge")
    @JsonProperty("max_surge")
    private Integer                 maxSurge;
    
    @ApiModelProperty(required = true, example = "3")
    @SerializedName("max_unavailable")
    @JsonProperty("max_unavailable")
    private Integer           maxUnavailable;
}
