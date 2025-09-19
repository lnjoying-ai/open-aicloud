package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExternalGateway;
import lombok.Data;

import java.util.List;

@Data
public class OSExtRouterUpdateReq
{
    private static final long serialVersionUID = 1L;
    private OSExtRouterUpdate router;

    @Data
    public static class OSExtRouterUpdate
    {
        @JsonProperty("external_gateway_info")
        @SerializedName("external_gateway_info")
        private OSExternalGateway externalGatewayInfo;
        private Boolean ha;
        @JsonProperty("name")
        @SerializedName("name")
        private String name;
        @JsonProperty("admin_state_up")
        @SerializedName("admin_state_up")
        private Boolean adminStateUp;
        private String description;
        private List<String> routes;
        @JsonProperty("distributed")
        @SerializedName("distributed")
        private Boolean distributed;
        @JsonProperty("enable_ndp_proxy")
        @SerializedName("enable_ndp_proxy")
        private Boolean enableNdpProxy;
        @JsonProperty("enable_default_route_bfd")
        @SerializedName("enable_default_route_bfd")
        private Boolean enableDefaultRouteBfd;
        @JsonProperty("enable_default_route_ecmp")
        @SerializedName("enable_default_route_ecmp")
        private Boolean enableDefaultRouteEcmp;

        @JsonProperty("close_external_gateway")
        @SerializedName("close_external_gateway")
        private Boolean closeExternalGateway;
    }
}
