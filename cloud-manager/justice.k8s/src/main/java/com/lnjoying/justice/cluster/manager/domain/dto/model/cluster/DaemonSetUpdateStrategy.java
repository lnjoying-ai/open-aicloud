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
public class DaemonSetUpdateStrategy implements Serializable
{
    //RollingUpdate OnDelete
    @ApiModelProperty(required = true, example = "RollingUpdate")
    @SerializedName("strategy")
    @JsonProperty("strategy")
    private String                              strategy;
    
    @ApiModelProperty(required = true, example = "{}")
    @SerializedName("rolling_update")
    @JsonProperty("rolling_update")
    private  RollingUpdateDaemonSet rollingUpdate;
}
