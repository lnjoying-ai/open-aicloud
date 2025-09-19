package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworks;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworksegments;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnets;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OSNetworkInfo
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("admin_state_up")
    @SerializedName("admin_state_up")
    private Boolean adminStateUp;
    @JsonProperty("availability_zone_hints")
    @SerializedName("availability_zone_hints")
    private List<String> availabilityZoneHints;
    @JsonProperty("availability_zones")
    @SerializedName("availability_zones")
    private List<String> availabilityZones;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("dns_domain")
    @SerializedName("dns_domain")
    private String dnsDomain;
    private String id;
    @JsonProperty("ipv4_address_scope")
    @SerializedName("ipv4_address_scope")
    private String ipv4AddressScope;
    @JsonProperty("ipv6_address_scope")
    @SerializedName("ipv6_address_scope")
    private String ipv6AddressScope;
    @JsonProperty("l2_adjacency")
    @SerializedName("l2_adjacency")
    private Boolean l2Adjacency;
    @JsonProperty("mtu")
    private Integer mtu;
    private String name;
    @JsonProperty("port_security_enabled")
    @SerializedName("port_security_enabled")
    private Boolean portSecurityEnabled;
    @JsonProperty("project_id")
    @SerializedName("project_id")
    private String projectId;
    @JsonProperty("provider:network_type")
    @SerializedName("provider:network_type")
    private String networkType;
    @JsonProperty("provider:physical_network")
    @SerializedName("provider:physical_network")
    private String providerPhyNet;
    @JsonProperty("provider:segmentation_id")
    @SerializedName("provider:segmentation_id")
    private String providerSegID;
    @JsonProperty("qos_policy_id")
    @SerializedName("qos_policy_id")
    private String qosPolicyId;
    @JsonProperty("revision_number")
    @SerializedName("revision_number")
    private Integer revisionNumber;
    @JsonProperty("router:external")
    @SerializedName("router:external")
    private Boolean routerExternal;
    private List<OSNetworkSegmentInfo> segments;
    private Boolean shared;
    private String status;
    private List<String> subnets;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("vlan_transparent")
    @SerializedName("vlan_transparent")
    private Boolean vlanTransparent;
    private String description;
    @JsonProperty("is_default")
    @SerializedName("is_default")
    private Boolean isDefault;
    private List<String> tags;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class OSNetworkSegmentInfo
    {
        @JsonProperty("provider:network_type")
        @SerializedName("provider:network_type")
        private String networkType;
        @JsonProperty("provider:physical_network")
        @SerializedName("provider:physical_network")
        private String providerPhyNet;
        @JsonProperty("provider:segmentation_id")
        @SerializedName("provider:segmentation_id")
        private String providerSegID;
    }

    public void setNetworkInfo(TblCmpOsNetworks tblCmpOsNetwork)
    {
        this.id = tblCmpOsNetwork.getId();
        this.name = tblCmpOsNetwork.getName();
        this.tenantId = tblCmpOsNetwork.getProjectId();
        this.adminStateUp = BoolUtil.short2Bool(tblCmpOsNetwork.getAdminStateUp());
        this.mtu = tblCmpOsNetwork.getMtu();
        this.status = tblCmpOsNetwork.getStatus();
        this.availabilityZoneHints = JsonUtils.fromJson(tblCmpOsNetwork.getAvailabilityZoneHints(), new TypeToken<List<String>>(){}.getType());
        this.projectId = tblCmpOsNetwork.getProjectId();
        this.vlanTransparent = BoolUtil.short2Bool(tblCmpOsNetwork.getVlanTransparent());

        this.shared =  BoolUtil.short2Bool(tblCmpOsNetwork.getShared());

        this.createdAt = tblCmpOsNetwork.getCreatedAt();
        this.updatedAt = tblCmpOsNetwork.getUpdatedAt();
        this.description = tblCmpOsNetwork.getDescription();
        this.revisionNumber = tblCmpOsNetwork.getRevisionNumber() == null ? null : tblCmpOsNetwork.getRevisionNumber().intValue();

        this.isDefault = BoolUtil.short2Bool(tblCmpOsNetwork.getIsDefault());

        this.dnsDomain = tblCmpOsNetwork.getDnsDomain();

        this.portSecurityEnabled = BoolUtil.short2Bool(tblCmpOsNetwork.getPortSecurityEnabled());

        this.qosPolicyId = tblCmpOsNetwork.getPolicyId();

        if (this.isDefault != null)
        {
            this.routerExternal = true;
        }
    }

    public void setNetworkSegment(List<TblCmpOsNetworksegments> networksegments)
    {
        if (! CollectionUtils.isEmpty(networksegments))
        {
            if (networksegments.size() == 1)
            {
                this.networkType = networksegments.get(0).getNetworkType();
                this.providerPhyNet = networksegments.get(0).getPhysicalNetwork();
                this.providerSegID = networksegments.get(0).getSegmentationId() == null ? null : networksegments.get(0).getSegmentationId().toString();
            }
            else
            {
                this.segments = new ArrayList<>();
                for (TblCmpOsNetworksegments networksegment : networksegments)
                {
                    this.segments.add(new OSNetworkSegmentInfo(networksegment.getNetworkType(),
                            networksegment.getPhysicalNetwork(), networksegment.getSegmentationId().toString()));
                }
            }
        }
    }

    public void setSubnet(List<TblCmpOsSubnets> subnets)
    {
        if (! CollectionUtils.isEmpty(subnets))
        {
            this.subnets = new ArrayList<>();
            for (TblCmpOsSubnets subnet : subnets)
            {
                this.subnets.add(subnet.getId());
            }
        }
    }
}
