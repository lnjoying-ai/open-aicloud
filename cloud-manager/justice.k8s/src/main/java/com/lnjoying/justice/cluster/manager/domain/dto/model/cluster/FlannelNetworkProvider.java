/**
 * desc flanel network provider
 */
package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FlannelNetworkProvider implements Serializable
{
    @ApiModelProperty(required = true, example = "if1")
    @SerializedName("iface")
    @JsonProperty("iface")
    private String                   iface;
}
