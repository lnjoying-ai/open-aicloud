package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSExtraDhcpOptCreate
{
    @JsonProperty("opt_value")
    @SerializedName("opt_value")
    public String optValue;
    @JsonProperty("opt_name")
    @SerializedName("opt_name")
    public String optName;
    @JsonProperty("ip_version")
    @SerializedName("ip_version")
    public Integer ipVersion;
}
