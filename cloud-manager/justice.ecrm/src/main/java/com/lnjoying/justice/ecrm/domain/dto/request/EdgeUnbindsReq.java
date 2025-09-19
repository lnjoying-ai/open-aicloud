package com.lnjoying.justice.ecrm.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class EdgeUnbindsReq
{
    @SerializedName("node_ids")
    @JsonProperty("node_ids")
    List<String> nodeIds;
}
