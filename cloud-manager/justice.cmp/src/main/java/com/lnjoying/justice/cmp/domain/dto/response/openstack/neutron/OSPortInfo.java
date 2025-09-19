package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Data
public class OSPortInfo
{
    private static final long serialVersionUID = 1L;
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
    @JsonProperty("binding:vif_details")
    @SerializedName("binding:vif_details")
    private Map<String, Object> vifDetails;
    @JsonProperty("binding:vif_type")
    @SerializedName("binding:vif_type")
    private String vifType;
    @JsonProperty("binding:vnic_type")
    @SerializedName("binding:vnic_type")
    private String vNicType;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("data_plane_status")
    @SerializedName("data_plane_status")
    private String dataPlaneStatus;
    private String description;
    @JsonProperty("device_id")
    @SerializedName("device_id")
    private String deviceId;
    @JsonProperty("device_owner")
    @SerializedName("device_owner")
    private String deviceOwner;
    @JsonProperty("dns_assignment")
    @SerializedName("dns_assignment")
    private Object dnsAssignment;
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
    private String id;
    @JsonProperty("ip_allocation")
    @SerializedName("ip_allocation")
    private String ipAllocation;
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
    @JsonProperty("qos_network_policy_id")
    @SerializedName("qos_network_policy_id")
    private String qosNetworkPolicyId;
    @JsonProperty("qos_policy_id")
    @SerializedName("qos_policy_id")
    private String qosPolicyId;
    @JsonProperty("revision_number")
    @SerializedName("revision_number")
    private Integer revisionNumber;
    @JsonProperty("resource_request")
    @SerializedName("resource_request")
    private String resourceRequest;
    @JsonProperty("security_groups")
    @SerializedName("security_groups")
    private List<String> securityGroups;
    @JsonProperty("status")
    @SerializedName("status")
    private String state;
    private List<String> tags;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("propagate_uplink_status")
    @SerializedName("propagate_uplink_status")
    private Boolean propagateUplinkStatus;
    @JsonProperty("mac_learning_enabled")
    @SerializedName("mac_learning_enabled")
    private Boolean macLearningEnabled;

    public void setPortInfo(TblCmpOsPorts tblCmpOsPort)
    {
        this.id = tblCmpOsPort.getId();
        this.projectId = tblCmpOsPort.getProjectId();
        this.tenantId = tblCmpOsPort.getProjectId();
        this.name = tblCmpOsPort.getName();
        this.networkId = tblCmpOsPort.getNetworkId();
        this.macAddress = tblCmpOsPort.getMacAddress();
        this.adminStateUp = BoolUtil.short2Bool(tblCmpOsPort.getAdminStateUp());
        this.deviceId = tblCmpOsPort.getDeviceId();
        this.deviceOwner = tblCmpOsPort.getDeviceOwner();
        this.state = tblCmpOsPort.getStatus();
        this.ipAllocation = tblCmpOsPort.getIpAllocation();

        this.dataPlaneStatus = tblCmpOsPort.getDataPlaneStatus();

        this.dnsDomain = tblCmpOsPort.getDnsDomain();
        this.dnsName = tblCmpOsPort.getDnsName();

        this.qosPolicyId = tblCmpOsPort.getPolicyId();

        this.createdAt = tblCmpOsPort.getCreatedAt();
        this.updatedAt = tblCmpOsPort.getCreatedAt();
        this.description = tblCmpOsPort.getDescription();
        this.revisionNumber = tblCmpOsPort.getRevisionNumber() == null ? null : tblCmpOsPort.getRevisionNumber().intValue();

        this.propagateUplinkStatus = BoolUtil.short2Bool(tblCmpOsPort.getPropagateUplinkStatus());
        this.macLearningEnabled = BoolUtil.short2Bool(tblCmpOsPort.getMacLearningEnabled());

        this.portSecurityEnabled = BoolUtil.short2Bool(tblCmpOsPort.getPortSecurityEnabled());
    }

    public void setAllowedaddresspair(List<TblCmpOsAllowedaddresspairs> tblCmpOsAllowedaddresspairs)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsAllowedaddresspairs))
        {
            this.allowedAddressPairs = new HashSet<>();
            for (TblCmpOsAllowedaddresspairs tblCmpOsAllowedaddresspair : tblCmpOsAllowedaddresspairs)
            {
                this.allowedAddressPairs.add(new OSAllowedAddressPair(tblCmpOsAllowedaddresspair.getIpAddress(), tblCmpOsAllowedaddresspair.getMacAddress()));
            }
        }
    }

    public void setMl2PortBinding(TblCmpOsMl2PortBindings tblCmpOsMl2PortBindings)
    {
        if (tblCmpOsMl2PortBindings != null)
        {
            this.profile = JsonUtils.fromJson(tblCmpOsMl2PortBindings.getProfile(), new TypeToken<Map<String, Object>>(){}.getType());
            this.vifDetails = JsonUtils.fromJson(tblCmpOsMl2PortBindings.getVifDetails(), new TypeToken<Map<String, Object>>(){}.getType());
            this.vifType = tblCmpOsMl2PortBindings.getVifType();
            this.vNicType = tblCmpOsMl2PortBindings.getVnicType();
            this.hostId = tblCmpOsMl2PortBindings.getHost();
        }
    }

    public void setMl2PortBindingLevel(TblCmpOsMl2PortBindingLevels tblCmpOsMl2PortBindingLevels)
    {

    }

    public void setMl2DistributedPortBinding(TblCmpOsMl2DistributedPortBindings tblCmpOsMl2DistributedPortBindings)
    {

    }

    public void setExtradhcpopt(List<TblCmpOsExtradhcpopts> tblCmpOsExtradhcpopts)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsExtradhcpopts))
        {
            this.extraDhcpOptCreates = new ArrayList<>();
            for (TblCmpOsExtradhcpopts tblCmpOsExtradhcpopt : tblCmpOsExtradhcpopts)
            {
                this.extraDhcpOptCreates.add(new OSExtraDhcpOptCreate(tblCmpOsExtradhcpopt.getOptValue(), tblCmpOsExtradhcpopt.getOptName(), tblCmpOsExtradhcpopt.getIpVersion()));
            }
        }
    }

    public void setSecuritygroupportbinding(List<TblCmpOsSecuritygroupportbindings> tblCmpOsSecuritygroupportbindings)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsSecuritygroupportbindings))
        {
            this.securityGroups = new ArrayList<>();
            for (TblCmpOsSecuritygroupportbindings tblCmpOsSecuritygroupportbinding : tblCmpOsSecuritygroupportbindings)
            {
                this.securityGroups.add(tblCmpOsSecuritygroupportbinding.getSecurityGroupId());
            }
        }
    }

    public void setIpallocation(List<TblCmpOsIpallocations> tblCmpOsIpallocations)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsIpallocations))
        {
            this.fixedIps = new HashSet<>();
            for (TblCmpOsIpallocations tblCmpOsIpallocation : tblCmpOsIpallocations)
            {
                this.fixedIps.add(new OSIP(tblCmpOsIpallocation.getIpAddress(), tblCmpOsIpallocation.getSubnetId()));
            }
        }
    }

    public void setNetwork(TblCmpOsNetworks tblCmpOsNetwork)
    {
        if (tblCmpOsNetwork != null)
        {
            this.qosNetworkPolicyId = tblCmpOsNetwork.getPolicyId();
        }
    }
}
