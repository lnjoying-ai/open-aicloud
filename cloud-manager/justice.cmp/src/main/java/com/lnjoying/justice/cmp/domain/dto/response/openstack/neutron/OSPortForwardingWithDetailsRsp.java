package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSPortForwardingWithDetailsRsp
{
    @JsonProperty("port_forwarding")
    @SerializedName("port_forwarding")
    private OSPortForwardingInfo portForwarding;
}
