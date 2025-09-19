package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingips;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworks;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRouters;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OSExtFloatingIpInfo
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private String id;
    @JsonProperty("router_id")
    @SerializedName("router_id")
    private String routerId;
    private String status;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    @JsonProperty("project_id")
    @SerializedName("project_id")
    private String projectId;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("revision_number")
    @SerializedName("revision_number")
    private Integer revisionNumber;
    private String description;
    private Boolean distributed;
    @JsonProperty("dns_domain")
    @SerializedName("dns_domain")
    private String dnsDomain;
    @JsonProperty("dns_name")
    @SerializedName("dns_name")
    private String dnsName;
    @JsonProperty("port_details")
    @SerializedName("port_details")
    private String portDetails;
    @JsonProperty("qos_network_policy_id")
    @SerializedName("qos_network_policy_id")
    private String qosNetworkPolicyId;
    @JsonProperty("qos_policy_id")
    @SerializedName("qos_policy_id")
    private String qosPolicyId;
    @JsonProperty("floating_network_id")
    @SerializedName("floating_network_id")
    private String floatingNetworkId;
    @JsonProperty("fixed_ip_address")
    @SerializedName("fixed_ip_address")
    private String fixedIpAddress;
    @JsonProperty("floating_ip_address")
    @SerializedName("floating_ip_address")
    private String floatingIpAddress;
    @JsonProperty("port_id")
    @SerializedName("port_id")
    private String portId;
    private List<String> tags;
    @JsonProperty("port_forwardings")
    @SerializedName("port_forwardings")
    private List<Object> portForwardings;

    @JsonProperty("floating_network_name")
    @SerializedName("floating_network_name")
    private String floatingNetworkName;
    @JsonProperty("router_name")
    @SerializedName("router_name")
    private String routerName;

    public void setFloatingIpInfo(TblCmpOsFloatingips tblCmpOsFloatingips)
    {
        this.id = tblCmpOsFloatingips.getId();
        this.projectId = tblCmpOsFloatingips.getProjectId();
        this.tenantId = tblCmpOsFloatingips.getProjectId();
        this.floatingIpAddress = tblCmpOsFloatingips.getFloatingIpAddress();
        this.floatingNetworkId = tblCmpOsFloatingips.getFloatingNetworkId();
        this.fixedIpAddress = tblCmpOsFloatingips.getFixedIpAddress();
        this.routerId = tblCmpOsFloatingips.getRouterId();
        this.status = tblCmpOsFloatingips.getStatus();

        this.dnsName = tblCmpOsFloatingips.getDnsName();
        this.dnsDomain = tblCmpOsFloatingips.getDnsDomain();

        this.createdAt = tblCmpOsFloatingips.getCreatedAt();
        this.updatedAt = tblCmpOsFloatingips.getCreatedAt();
        this.description = tblCmpOsFloatingips.getDescription();
        this.revisionNumber = tblCmpOsFloatingips.getRevisionNumber() == null ? null : tblCmpOsFloatingips.getRevisionNumber().intValue();

        this.qosPolicyId = tblCmpOsFloatingips.getPolicyId();
    }

    public void setNetwork(TblCmpOsNetworks tblCmpOsNetwork)
    {
        if (tblCmpOsNetwork != null)
        {
            this.qosNetworkPolicyId = tblCmpOsNetwork.getPolicyId();
            this.floatingNetworkName = tblCmpOsNetwork.getName();
        }
    }

    public void setRouter(TblCmpOsRouters tblCmpOsRouter)
    {
        if (tblCmpOsRouter != null)
        {
            this.routerName = tblCmpOsRouter.getName();
        }
    }
}
