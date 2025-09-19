package com.lnjoying.justice.cmp.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.OSServiceEndpoints;
import com.lnjoying.justice.cmp.domain.model.Authorization;
import com.lnjoying.justice.cmp.domain.model.HealthCheck;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import lombok.Data;

import java.util.List;

@Data
public class AddCloudReq
{
    @SerializedName("cloud_id")
    @JsonProperty("cloud_id")
    private String cloudId;

    @SerializedName("name")
    @JsonProperty("name")
    private String name;

    @SerializedName("description")
    @JsonProperty("description")
    private String description;

    @SerializedName("product")
    @JsonProperty("product")
    private String product;

    @SerializedName("version")
    @JsonProperty("version")
    private String version;

    @SerializedName("target_nodes")
    @JsonProperty("target_nodes")
    private List<TargetNode> targetNodes;

    @SerializedName("mode")
    @JsonProperty("mode")
    private String mode;

    @SerializedName("url")
    @JsonProperty("url")
    private String url;

    @SerializedName("certificate")
    @JsonProperty("certificate")
    private String certificate;

    @SerializedName("health_check")
    @JsonProperty("health_check")
    private HealthCheck healthCheck;

    @SerializedName("authorization")
    @JsonProperty("authorization")
    private Authorization authorization;

    @SerializedName("bp_id")
    @JsonProperty("bp_id")
    private String bpId;

    @SerializedName("user_id")
    @JsonProperty("user_id")
    private String userId;

    @SerializedName("os_service_endpoints")
    @JsonProperty("os_service_endpoints")
    private OSServiceEndpoints osServiceEndpoints;

    @SerializedName("labels")
    @JsonProperty("labels")
    private List<String> labels;
}
