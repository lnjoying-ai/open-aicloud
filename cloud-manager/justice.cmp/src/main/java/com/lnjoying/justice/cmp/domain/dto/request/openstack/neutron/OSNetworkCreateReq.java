package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.openstack4j.model.network.NetworkType;

import java.util.List;

@Data
public class OSNetworkCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSNetworkCreate network;

    @Data
    public static class OSNetworkCreate
    {
        @JsonProperty("admin_state_up")
        @SerializedName("admin_state_up")
        private Boolean adminStateUp;
        @JsonProperty("dns_domain")
        @SerializedName("dns_domain")
        private String dnsDomain;
        @JsonProperty("mtu")
        @SerializedName("mtu")
        private Integer mtu;
        private String name;
        @JsonProperty("port_security_enabled")
        @SerializedName("port_security_enabled")
        private String portSecurityEnabled;
        @JsonProperty("project_id")
        @SerializedName("project_id")
        private String projectId;
        @JsonProperty("provider:network_type")
        @SerializedName("provider:network_type")
        private NetworkType networkType;
        @JsonProperty("provider:physical_network")
        @SerializedName("provider:physical_network")
        private String providerPhyNet;
        @JsonProperty("provider:segmentation_id")
        @SerializedName("provider:segmentation_id")
        private Integer providerSegID;
        @JsonProperty("qos_policy_id")
        @SerializedName("qos_policy_id")
        private String qosPolicyId;
        @JsonProperty("router:external")
        @SerializedName("router:external")
        private Boolean routerExternal;
        private List<Object> segments;
        private Boolean shared;
        @JsonProperty("tenant_id")
        @SerializedName("tenant_id")
        private String tenantId;
        @JsonProperty("vlan_transparent")
        @SerializedName("vlan_transparent")
        private Boolean vlanTransparent;
        private String description;
        @JsonProperty("is_default")
        @SerializedName("is_default")
        private String isDefault;
        @JsonProperty("availability_zone_hints")
        @SerializedName("availability_zone_hints")
        private List<String> availabilityZoneHints;
    }
}
