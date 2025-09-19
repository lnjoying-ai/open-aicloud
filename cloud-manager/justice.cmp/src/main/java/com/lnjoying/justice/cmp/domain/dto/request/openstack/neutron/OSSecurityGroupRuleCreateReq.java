package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSSecurityGroupRuleCreateReq
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("security_group_rule")
    @SerializedName("security_group_rule")
    private OSSecurityGroupRuleCreate securityGroupRule;

    @Data
    public static class OSSecurityGroupRuleCreate
    {
        @JsonProperty("remote_group_id")
        @SerializedName("remote_group_id")
        private String remote_group_id;
        private String direction;
        private String protocol;
        private String ethertype;
        @JsonProperty("port_range_max")
        @SerializedName("port_range_max")
        private Integer portRangeMax;
        @JsonProperty("security_group_id")
        @SerializedName("security_group_id")
        private String securityGroupId;
        @JsonProperty("port_range_min")
        @SerializedName("port_range_min")
        private Integer portRangeMin;
        @JsonProperty("remote_ip_prefix")
        @SerializedName("remote_ip_prefix")
        private String remoteIpPrefix;
        private String description;
    }
}
