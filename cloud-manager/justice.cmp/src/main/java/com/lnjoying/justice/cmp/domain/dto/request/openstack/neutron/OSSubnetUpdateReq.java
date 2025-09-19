package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSHostRoute;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSPool;
import lombok.Data;

import java.util.List;

@Data
public class OSSubnetUpdateReq
{
    private static final long serialVersionUID = 1L;
    private OSSubnetUpdate subnet;

    @Data
    public static class OSSubnetUpdate
    {
        @JsonProperty("name")
        @SerializedName("name")
        private String name;
        @JsonProperty("enable_dhcp")
        @SerializedName("enable_dhcp")
        private boolean enableDHCP = true;
        @JsonProperty("dns_nameservers")
        @SerializedName("dns_nameservers")
        private List<String> dnsNames;
        @JsonProperty("allocation_pools")
        @SerializedName("allocation_pools")
        private List<OSPool> pools;
        @JsonProperty("host_routes")
        @SerializedName("host_routes")
        private List<OSHostRoute> hostRoutes;
        @JsonProperty("gateway_ip")
        @SerializedName("gateway_ip")
        private String gateway;
        private String description;
        @JsonProperty("service_types")
        @SerializedName("service_types")
        private List<String> serviceTypes;
        @JsonProperty("segment_id")
        @SerializedName("segment_id")
        private String segmentId;
        @JsonProperty("dns_publish_fixed_ip")
        @SerializedName("dns_publish_fixed_ip")
        private Boolean dnsPublishFixedIp;
    }
}
