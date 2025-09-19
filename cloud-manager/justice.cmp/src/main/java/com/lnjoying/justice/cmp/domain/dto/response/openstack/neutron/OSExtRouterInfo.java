package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.db.model.TblCmpOsIpallocations;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworks;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRouterroutes;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRouters;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OSExtRouterInfo
{

    @JsonProperty("id")
    private String id;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    @JsonProperty("project_id")
    @SerializedName("project_id")
    private String projectId;
    @JsonProperty("name")
    private String name;
    private String description;
    @JsonProperty("admin_state_up")
    @SerializedName("admin_state_up")
    private Boolean adminStateUp;
    private String status;
    @JsonProperty("external_gateway_info")
    @SerializedName("external_gateway_info")
    private OSExtExternalGateway externalGatewayInfo;
    @JsonProperty("external_gateways")
    @SerializedName("external_gateways")
    private List<String> externalGateways;
    @JsonProperty("revision_number")
    @SerializedName("revision_number")
    private Integer revisionNumber;
    @JsonProperty("routes")
    private List<OSHostRoute> routes;
    @JsonProperty("destination")
    private String destination;
    @JsonProperty("nexthop")
    private String nexthop;
    @JsonProperty("distributed")
    private Boolean distributed;
    @JsonProperty("ha")
    private Boolean ha;
    @JsonProperty("availability_zone_hints")
    @SerializedName("availability_zone_hints")
    private List<String> availabilityZoneHints;
    @JsonProperty("availability_zones")
    @SerializedName("availability_zones")
    private List<String> availabilityZones;
    @JsonProperty("service_type_id")
    @SerializedName("service_type_id")
    private String serviceTypeId;
    @JsonProperty("flavor_id")
    @SerializedName("flavor_id")
    private String flavorId;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("tags")
    private List<String> tags;
    @JsonProperty("conntrack_helpers")
    @SerializedName("conntrack_helpers")
    private List<String> conntrackHelpers;
    @JsonProperty("enable_ndp_proxy")
    @SerializedName("enable_ndp_proxy")
    private Boolean enableNdpProxy;
    @JsonProperty("enable_default_route_bfd")
    @SerializedName("enable_default_route_bfd")
    private Boolean enableDefaultRouteBfd;
    @JsonProperty("enable_default_route_ecmp")
    @SerializedName("enable_default_route_ecmp")
    private Boolean enableDefaultRouteEcmp;

    @Data
    public static class OSExtExternalGateway
    {
        private static final long serialVersionUID = 1L;
        @JsonProperty("enable_snat")
        @SerializedName("enable_snat")
        private Boolean enableSnat;
        @JsonProperty("external_fixed_ips")
        @SerializedName("external_fixed_ips")
        private List<OSIP> externalFixedIps;
        @JsonProperty("network_id")
        @SerializedName("network_id")
        private String networkId;

        @JsonProperty("network_name")
        @SerializedName("network_name")
        private String networkName;
    }

    public void setRouterInfo(TblCmpOsRouters tblCmpOsRouter)
    {
        this.id = tblCmpOsRouter.getId();
        this.tenantId = tblCmpOsRouter.getProjectId();
        this.projectId = tblCmpOsRouter.getProjectId();
        this.name = tblCmpOsRouter.getName();
        this.adminStateUp = BoolUtil.short2Bool(tblCmpOsRouter.getAdminStateUp());
        this.status = tblCmpOsRouter.getStatus();

        this.distributed = BoolUtil.short2Bool(tblCmpOsRouter.getDistributed());
        this.ha = BoolUtil.short2Bool(tblCmpOsRouter.getHa());
        this.availabilityZoneHints = JsonUtils.fromJson(tblCmpOsRouter.getAvailabilityZoneHints(), new TypeToken<List<String>>(){}.getType());

        this.createdAt = tblCmpOsRouter.getCreatedAt();
        this.updatedAt = tblCmpOsRouter.getCreatedAt();
        this.description = tblCmpOsRouter.getDescription();
        this.revisionNumber = tblCmpOsRouter.getRevisionNumber() == null ? null : tblCmpOsRouter.getRevisionNumber().intValue();
        if (tblCmpOsRouter.getEnableSnat() != null)
        {
            this.externalGatewayInfo = new OSExtExternalGateway();
            this.externalGatewayInfo.setEnableSnat(BoolUtil.short2Bool(tblCmpOsRouter.getEnableSnat()));
        }
    }

    public void setFixedIp(List<TblCmpOsIpallocations> ipallocations)
    {
        if (! CollectionUtils.isEmpty(ipallocations))
        {
            if (this.externalGatewayInfo == null) this.externalGatewayInfo = new OSExtExternalGateway();
            this.externalGatewayInfo.setExternalFixedIps(new ArrayList<>());
            for (TblCmpOsIpallocations ipallocation : ipallocations)
            {
                this.externalGatewayInfo.getExternalFixedIps().add(new OSIP(ipallocation.getIpAddress(), ipallocation.getSubnetId()));
            }
            this.externalGatewayInfo.setNetworkId(ipallocations.get(0).getNetworkId());
        }
    }

    public void setRouterroute(List<TblCmpOsRouterroutes> routerroutes)
    {
        if (! CollectionUtils.isEmpty(routerroutes))
        {
            this.routes = new ArrayList<>();
            for (TblCmpOsRouterroutes routerroute : routerroutes)
            {
                this.routes.add(new OSHostRoute(routerroute.getDestination(), routerroute.getNexthop()));
            }
        }
    }

    public void setNetwork(TblCmpOsNetworks network)
    {
        if (network != null)
        {
            this.externalGatewayInfo.setNetworkName(network.getName());
        }
    }
}
