/**
 * desc calico network provider
 */
package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CalicoNetworkProvider implements Serializable
{
    @ApiModelProperty(required = true, example = "aws")
    @SerializedName("cloud_provider")
    @JsonProperty("cloud_provider")
    private String         cloudProvider;
}
