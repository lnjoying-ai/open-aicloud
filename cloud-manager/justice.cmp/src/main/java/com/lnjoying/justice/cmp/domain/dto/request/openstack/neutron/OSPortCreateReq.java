package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSAllowedAddressPair;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtraDhcpOptCreate;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSIP;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class OSPortCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSPortCreate port;

    @Data
    public static class OSPortCreate
    {
        @JsonProperty("admin_state_up")
        @SerializedName("admin_state_up")
        private boolean adminStateUp = true;
        @JsonProperty("allowed_address_pairs")
        @SerializedName("allowed_address_pairs")
        private Set<OSAllowedAddressPair> allowedAddressPairs;
        @JsonProperty("binding:host_id")
        @SerializedName("binding:host_id")
        private String hostId;
        @JsonProperty("binding:profile")
        @SerializedName("binding:profile")
        private Map<String, Object> profile;
        @JsonProperty("binding:vnic_type")
        @SerializedName("binding:vnic_type")
        private String vNicType;
        private String description;
        @JsonProperty("device_id")
        @SerializedName("device_id")
        private String deviceId;
        @JsonProperty("device_owner")
        @SerializedName("device_owner")
        private String deviceOwner;
        @JsonProperty("dns_domain")
        @SerializedName("dns_domain")
        private String dnsDomain;
        @JsonProperty("dns_name")
        @SerializedName("dns_name")
        private String dnsName;
        @JsonProperty("extra_dhcp_opts")
        @SerializedName("extra_dhcp_opts")
        private List<OSExtraDhcpOptCreate> extraDhcpOptCreates;
        @JsonProperty("fixed_ips")
        @SerializedName("fixed_ips")
        private Set<OSIP> fixedIps;
        private Object hints;
        @JsonProperty("mac_address")
        @SerializedName("mac_address")
        private String macAddress;
        private String name;
        @JsonProperty("network_id")
        @SerializedName("network_id")
        private String networkId;
        @JsonProperty("numa_affinity_policy")
        @SerializedName("numa_affinity_policy")
        private String numaAffinityPolicy;
        @JsonProperty("port_security_enabled")
        @SerializedName("port_security_enabled")
        private Boolean portSecurityEnabled;
        @JsonProperty("project_id")
        @SerializedName("project_id")
        private String projectId;
        @JsonProperty("qos_policy_id")
        @SerializedName("qos_policy_id")
        private String qosPolicyId;
        @JsonProperty("security_groups")
        @SerializedName("security_groups")
        private List<String> securityGroups;
        @JsonProperty("tenant_id")
        @SerializedName("tenant_id")
        private String tenantId;
        @JsonProperty("propagate_uplink_status")
        @SerializedName("propagate_uplink_status")
        private Boolean propagateUplinkStatus;
        @JsonProperty("mac_learning_enabled")
        @SerializedName("mac_learning_enabled")
        private Boolean macLearningEnabled;
    }
}
