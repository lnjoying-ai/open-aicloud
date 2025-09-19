package com.lnjoying.justice.cmp.domain.dto.response.openstack.keystone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSProjectsWithDetailsRsp
{
    private List<OSDomainInfo> domains;
    private List<OSProjectInfo> projects;
    private Object links;

    @Data
    public static class OSDomainInfo
    {
        private String description;
        private Boolean enabled;
        private String id;
        private String name;
        private Object links;
    }

    @Data
    public static class OSProjectInfo
    {
        @JsonProperty("is_domain")
        @SerializedName("is_domain")
        private Boolean isDomain;
        private String description;
        @JsonProperty("domain_id")
        @SerializedName("domain_id")
        private String domainId;
        private Boolean enabled;
        private String id;
        private String name;
        @JsonProperty("parent_id")
        @SerializedName("parent_id")
        private String parentId;
        private List<String> tags;
        private Object links;
    }
}
