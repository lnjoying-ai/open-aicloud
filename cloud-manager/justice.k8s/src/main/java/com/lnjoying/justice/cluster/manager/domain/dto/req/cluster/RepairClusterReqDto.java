package com.lnjoying.justice.cluster.manager.domain.dto.req.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel(value = "RepairClusterReq")
public class RepairClusterReqDto
{
    @SerializedName("node_ids")
    @JsonProperty("node_ids")
    private List<String> nodeIds;

    @SerializedName("type")
    @JsonProperty("type")
    @NotBlank
    private String type;
}
