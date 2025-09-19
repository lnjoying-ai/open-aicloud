package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExternalGateway;
import lombok.Data;
import org.openstack4j.openstack.networking.domain.*;

import java.util.List;

@Data
public class OSRouterCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSRouterCreate router;

    @Data
    public static class OSRouterCreate
    {
        @JsonProperty("tenant_id")
        @SerializedName("tenant_id")
        private String tenantId;
        @JsonProperty("project_id")
        @SerializedName("project_id")
        private String projectId;
        @JsonProperty("name")
        @SerializedName("name")
        private String name;
        private String description;
        @JsonProperty("admin_state_up")
        @SerializedName("admin_state_up")
        private Boolean adminStateUp;
        @JsonProperty("external_gateway_info")
        @SerializedName("external_gateway_info")
        private OSExternalGateway externalGatewayInfo;
        @JsonProperty("distributed")
        @SerializedName("distributed")
        private Boolean distributed;
        private Boolean ha;
        @JsonProperty("availability_zone_hints")
        @SerializedName("availability_zone_hints")
        private List<String> availability_zone_hints;
        @JsonProperty("service_type_id")
        @SerializedName("service_type_id")
        private String serviceTypeId;
        @JsonProperty("flavor_id")
        @SerializedName("flavor_id")
        private String flavorId;
        @JsonProperty("enable_ndp_proxy")
        @SerializedName("enable_ndp_proxy")
        private Boolean enableNdpProxy;
        @JsonProperty("enable_default_route_bfd")
        @SerializedName("enable_default_route_bfd")
        private Boolean enableDefaultRouteBfd;
        @JsonProperty("enable_default_route_ecmp")
        @SerializedName("enable_default_route_ecmp")
        private Boolean enableDefaultRouteEcmp;
    }
}
