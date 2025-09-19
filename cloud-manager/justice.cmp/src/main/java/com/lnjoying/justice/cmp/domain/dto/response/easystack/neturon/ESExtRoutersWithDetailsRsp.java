package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ESExtRoutersWithDetailsRsp
{
    private List<ESExtRouterInfo> routers;

    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
