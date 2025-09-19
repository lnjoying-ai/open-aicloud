package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ESExtFirewallsRsp
{
    private List<ESExtFirewallInfo> firewalls;

    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
