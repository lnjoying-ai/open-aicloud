package com.lnjoying.justice.ecrm.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import lombok.Data;

import java.util.List;

@Data
public class AddServiceAgentReq
{
    private String image;

    private String description;

    @SerializedName("target_nodes")
    @JsonProperty("target_nodes")
    private List<TargetNode> targetNodes;
}
