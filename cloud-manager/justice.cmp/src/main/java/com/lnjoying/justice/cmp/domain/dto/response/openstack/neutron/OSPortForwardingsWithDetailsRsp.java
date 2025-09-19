package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSPortForwardingsWithDetailsRsp
{
    @JsonProperty("port_forwardings")
    @SerializedName("port_forwardings")
    private List<OSPortForwardingInfo> portForwardings;
}
