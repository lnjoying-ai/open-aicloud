package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.*;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OSSubnetInfo
{
    private static final long serialVersionUID = 1L;
    private String id;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    @JsonProperty("project_id")
    @SerializedName("project_id")
    private String projectId;
    private String name;
    @JsonProperty("enable_dhcp")
    @SerializedName("enable_dhcp")
    private boolean enableDHCP;
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
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    private String description;
    @JsonProperty("ipv6_address_mode")
    @SerializedName("ipv6_address_mode")
    private String ipv6AddressMode;
    @JsonProperty("ipv6_ra_mode")
    @SerializedName("ipv6_ra_mode")
    private String ipv6RaMode;
    @JsonProperty("revision_number")
    @SerializedName("revision_number")
    private Integer revisionNumber;
    @JsonProperty("segment_id")
    @SerializedName("segment_id")
    private String segmentId;
    @JsonProperty("service_types")
    @SerializedName("service_types")
    private List<String> serviceTypes;
    @JsonProperty("subnetpool_id")
    @SerializedName("subnetpool_id")
    private String subnetpoolId;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    private List<String> tags;
    @JsonProperty("dns_publish_fixed_ip")
    @SerializedName("dns_publish_fixed_ip")
    private Boolean dnsPublishFixedIp;

    public void setSubnetInfo(TblCmpOsSubnets tblCmpOsSubnet)
    {
        this.id = tblCmpOsSubnet.getId();
        this.name = tblCmpOsSubnet.getName();
        this.tenantId = tblCmpOsSubnet.getName();
        this.projectId = tblCmpOsSubnet.getName();
        this.networkId = tblCmpOsSubnet.getNetworkId();
        this.ipVersion = tblCmpOsSubnet.getIpVersion();
        this.subnetpoolId = tblCmpOsSubnet.getSubnetpoolId();
        this.enableDHCP = tblCmpOsSubnet.getEnableDhcp() != 0;
        this.ipv6RaMode = tblCmpOsSubnet.getIpv6RaMode();
        this.ipv6AddressMode = tblCmpOsSubnet.getIpv6AddressMode();
        this.gateway = tblCmpOsSubnet.getGatewayIp();
        this.cidr = tblCmpOsSubnet.getCidr();
        this.segmentId = tblCmpOsSubnet.getSegmentId();

        this.createdAt = tblCmpOsSubnet.getCreatedAt();
        this.updatedAt = tblCmpOsSubnet.getCreatedAt();
        this.description = tblCmpOsSubnet.getDescription();
        this.revisionNumber = tblCmpOsSubnet.getRevisionNumber() == null ? null : tblCmpOsSubnet.getRevisionNumber().intValue();
    }

    public void setIpallocationpools(List<TblCmpOsIpallocationpools> ipallocationpools)
    {
        if (! CollectionUtils.isEmpty(ipallocationpools))
        {
            this.pools = new ArrayList<>();
            for (TblCmpOsIpallocationpools ipallocationpool : ipallocationpools)
            {
                this.pools.add(new OSPool(ipallocationpool.getFirstIp(), ipallocationpool.getLastIp()));
            }
        }
    }

    public void setSubnetroutes(List<TblCmpOsSubnetroutes> subnetroutes)
    {
        if (! CollectionUtils.isEmpty(subnetroutes))
        {
            this.hostRoutes = new ArrayList<>();
            for (TblCmpOsSubnetroutes subnetroute : subnetroutes)
            {
                this.hostRoutes.add(new OSHostRoute(subnetroute.getDestination(), subnetroute.getNexthop()));
            }
        }
    }

    public void setDnsnameservers(List<TblCmpOsDnsnameservers> dnsnameservers)
    {
        if (! CollectionUtils.isEmpty(dnsnameservers))
        {
            this.dnsNames = new ArrayList<>();
            for (TblCmpOsDnsnameservers dnsnameserver : dnsnameservers)
            {
                this.dnsNames.add(dnsnameserver.getAddress());
            }
        }
    }

    public void setSubnetServiceTypes(List<TblCmpOsSubnetServiceTypes> subnetServiceTypes)
    {
        if (! CollectionUtils.isEmpty(subnetServiceTypes))
        {
            this.serviceTypes = new ArrayList<>();
            for (TblCmpOsSubnetServiceTypes tblCmpOsSubnetServiceType : subnetServiceTypes)
            {
                this.serviceTypes.add(tblCmpOsSubnetServiceType.getServiceType());
            }
        }
    }
}
