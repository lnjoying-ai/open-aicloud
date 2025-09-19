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
public class NodeUpgradeStrategy implements Serializable
{
    @ApiModelProperty(example = "true")
    @SerializedName("drain")
    @JsonProperty("drain")
    private Boolean                       drain;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("node_drain_input")
    @JsonProperty("node_drain_input")
    private NodeDrainInput nodeDrainInput;
    
    @ApiModelProperty(example = "10")
    @SerializedName("max_unavailable_controller")
    @JsonProperty("max_unavailable_controller")
    private String             maxUnavailableController;
    
    @ApiModelProperty(example = "20")
    @SerializedName("max_unavailable_worker")
    @JsonProperty("max_unavailable_worker")
    private String         maxUnavailableWorker;
}
