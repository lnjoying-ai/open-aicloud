package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSHostRoute;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSPool;
import lombok.Data;

import java.util.List;

@Data
public class OSSubnetCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSSubnetCreate subnet;

    @Data
    public static class OSSubnetCreate
    {
        @JsonProperty("tenant_id")
        @SerializedName("tenant_id")
        private String tenantId;
        @JsonProperty("project_id")
        @SerializedName("project_id")
        private String projectId;
        private String name;
        @JsonProperty("enable_dhcp")
        @SerializedName("enable_dhcp")
        private boolean enableDHCP = true;
        @JsonProperty("network_id")
        @SerializedName("network_id")
        private String networkId;
        @JsonProperty("dns_nameservers")
        @SerializedName("dns_nameservers")
        private List<String> dnsNames;
        @JsonProperty("allocation_pools")
        @SerializedName("allocation_pools")
        private List<OSPool> pools;
        @JsonProperty("host_routes")
        @SerializedName("host_routes")
        private List<OSHostRoute> hostRoutes;
        @JsonProperty("ip_version")
        @SerializedName("ip_version")
        private Integer ipVersion;
        @JsonProperty("gateway_ip")
        @SerializedName("gateway_ip")
        private String gateway;
        private String cidr;
        private Integer prefixlen;
        private String description;
        @JsonProperty("ipv6_address_mode")
        @SerializedName("ipv6_address_mode")
        private String ipv6AddressMode;
        @JsonProperty("ipv6_ra_mode")
        @SerializedName("ipv6_ra_mode")
        private String ipv6RaMode;
        @JsonProperty("segment_id")
        @SerializedName("segment_id")
        private String segmentId;
        @JsonProperty("subnetpool_id")
        @SerializedName("subnetpool_id")
        private String subnetpoolId;
        @JsonProperty("use_default_subnetpool")
        @SerializedName("use_default_subnetpool")
        private Boolean useDefaultSubnetpool;
        @JsonProperty("service_types")
        @SerializedName("service_types")
        private String serviceTypes;
        @JsonProperty("dns_publish_fixed_ip")
        @SerializedName("dns_publish_fixed_ip")
        private Boolean dnsPublishFixedIp;
    }
}
