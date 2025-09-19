package com.lnjoying.justice.schema.msg.scheduler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.dev.K8sDevNeed;
import com.lnjoying.justice.schema.entity.dev.SchedulingStrategy;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignEdge2ClusterReq
{
    @ApiModelProperty(example = "controller")
    @SerializedName("dev_needs")
    @JsonProperty("dev_needs")
    private List<K8sDevNeed> devNeeds;

    @SerializedName("target_nodes")
    @JsonProperty("target_nodes")
    private List<TargetNode> targetNodes;

    @SerializedName("replica_num")
    @JsonProperty("replica_num")
    private Integer replicaNum;

    @SerializedName("bp_id")
    @JsonProperty("bp_id")
    private String bpId;

    @SerializedName("user_id")
    @JsonProperty("user_id")
    private String userId;

    @SerializedName("cluster_type")
    @JsonProperty("")
    private String clusterType;

    @SerializedName("scheduling_strategy")
    @JsonProperty("scheduling_strategy")
    private SchedulingStrategy schedulingStrategy;

    @SerializedName("wait_assign_id_list")
    @JsonProperty("wait_assign_id_list")
    private List<String> waitAssignIdList;

    @SerializedName("spec_id")
    @JsonProperty("spec_id")
    private String specId;
}
