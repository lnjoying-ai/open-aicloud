package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSSecurityGroupUpdateReq
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("security_group")
    @SerializedName("security_group")
    private OSSecurityGroupUpdate securityGroup;

    @Data
    public static class OSSecurityGroupUpdate
    {
        private String description;
        private String name;
        private Boolean stateful;
    }
}
