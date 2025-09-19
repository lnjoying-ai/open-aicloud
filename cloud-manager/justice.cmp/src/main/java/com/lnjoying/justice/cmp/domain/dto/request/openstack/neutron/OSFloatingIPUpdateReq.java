package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSFloatingIPUpdateReq
{
    private static final long serialVersionUID = 1L;
    private OSFloatingIPUpdate floatingip;

    @Data
    public static class OSFloatingIPUpdate
    {
        @JsonProperty("port_id")
        @SerializedName("port_id")
        private String portId;
        @JsonProperty("fixed_ip_address")
        @SerializedName("fixed_ip_address")
        private String fixedIpAddress;
        private String description;
        private Boolean distributed;
    }
}
