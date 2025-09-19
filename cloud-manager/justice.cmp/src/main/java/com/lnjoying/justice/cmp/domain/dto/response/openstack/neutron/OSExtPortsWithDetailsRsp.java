package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSExtPortsWithDetailsRsp
{
    private List<OSExtPortInfo> ports;

    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
