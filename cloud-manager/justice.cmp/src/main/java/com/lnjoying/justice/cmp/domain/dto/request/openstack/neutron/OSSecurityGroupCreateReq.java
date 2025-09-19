package com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSSecurityGroupCreateReq
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("security_group")
    @SerializedName("security_group")
    private OSSecurityGroupCreate securityGroup;

    @Data
    public static class OSSecurityGroupCreate
    {
        @JsonProperty("tenant_id")
        @SerializedName("tenant_id")
        private String tenantId;
        @JsonProperty("project_id")
        @SerializedName("project_id")
        private String projectId;
        private String description;
        private String name;
        private Boolean stateful;
    }
}
