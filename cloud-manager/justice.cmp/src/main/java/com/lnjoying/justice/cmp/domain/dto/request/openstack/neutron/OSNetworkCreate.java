package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSNetworkCreate
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("uuid")
    @SerializedName("uuid")
    private String id;
    @JsonProperty("fixed_ip")
    @SerializedName("fixed_ip")
    private String fixedIp;
    private String port;
}
