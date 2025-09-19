package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSAllowedAddressPair
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("ip_address")
    @SerializedName("ip_address")
    private String ipAddress;
    @JsonProperty("mac_address")
    @SerializedName("mac_address")
    private String macAddress;
}
