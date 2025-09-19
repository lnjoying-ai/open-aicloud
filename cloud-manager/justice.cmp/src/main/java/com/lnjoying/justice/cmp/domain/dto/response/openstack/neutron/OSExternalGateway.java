package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSExternalGateway
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("enable_snat")
    @SerializedName("enable_snat")
    private Boolean enableSnat;
    @JsonProperty("external_fixed_ips")
    @SerializedName("external_fixed_ips")
    private List<OSIP> externalFixedIps;
    @JsonProperty("network_id")
    @SerializedName("network_id")
    private String networkId;
}
