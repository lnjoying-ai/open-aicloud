package com.lnjoying.justice.cmp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.common.CloudProduct;
import com.lnjoying.justice.cmp.common.CloudStatus;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.OSServiceEndpoints;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.StatusCode;
import com.lnjoying.justice.schema.entity.TipMessage;
import com.micro.core.common.Utils;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

@Data
public class CloudInfo
{
    @SerializedName("cloud_id")
    @JsonProperty("cloud_id")
    private String cloudId;

    @SerializedName("name")
    @JsonProperty("name")
    private String name;

    @SerializedName("product")
    @JsonProperty("product")
    private String product;

    @SerializedName("product_short")
    @JsonProperty("product_short")
    private String productShort;

    @SerializedName("status")
    @JsonProperty("status")
    private StatusCode status;

    @SerializedName("health_status")
    @JsonProperty("health_status")
    private StatusCode healthStatus;

    @SerializedName("region_id")
    @JsonProperty("region_id")
    private String regionId;

    @SerializedName("region_name")
    @JsonProperty("region_name")
    private String regionName;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    private String siteId;

    @SerializedName("site_name")
    @JsonProperty("site_name")
    private String siteName;

    @SerializedName("node_id")
    @JsonProperty("node_id")
    private String nodeId;

    @SerializedName("node_name")
    @JsonProperty("node_name")
    private String nodeName;

    @SerializedName("mode")
    @JsonProperty("mode")
    private String mode;

    @SerializedName("url")
    @JsonProperty("url")
    private String url;

    @SerializedName("certificate")
    @JsonProperty("certificate")
    private String certificate;

    @SerializedName("labels")
    @JsonProperty("labels")
    private List<String> labels;

    @SerializedName("user_id")
    @JsonProperty("user_id")
    private String userId;

    @SerializedName("user_name")
    @JsonProperty("user_name")
    private String userName;

    @SerializedName("bp_id")
    @JsonProperty("bp_id")
    private String bpId;

    @SerializedName("bp_name")
    @JsonProperty("bp_name")
    private String bpName;

    @SerializedName("tip_message")
    @JsonProperty("tip_message")
    private TipMessage tipMessage;

    @SerializedName("health_check")
    @JsonProperty("health_check")
    private HealthCheck healthCheck;

    @SerializedName("authorization")
    @JsonProperty("authorization")
    private Authorization authorization;

    @SerializedName("metrics")
    @JsonProperty("metrics")
    private CloudSystemMetrics metrics;

    @SerializedName("create_time")
    @JsonProperty("create_time")
    private String createTime;

    @SerializedName("update_time")
    @JsonProperty("update_time")
    private String updateTime;

    @SerializedName("os_service_endpoints")
    @JsonProperty("os_service_endpoints")
    private OSServiceEndpoints osServiceEndpoints;

    @SerializedName("usage")
    @JsonProperty("usage")
    private Double usage;

    public static CloudInfo of(TblCloudInfo tblCloudInfo)
    {
        CloudInfo cloudInfo = new CloudInfo();
        BeanUtils.copyProperties(tblCloudInfo, cloudInfo);
        cloudInfo.setProductShort(CloudProduct.fromName(cloudInfo.getProduct()).getShortName());
        cloudInfo.setHealthCheck(JsonUtils.fromJson(tblCloudInfo.getHealthCheck(), HealthCheck.class));
        cloudInfo.setAuthorization(JsonUtils.fromJson(tblCloudInfo.getAuthorization(), Authorization.class));
        cloudInfo.setLabels(JsonUtils.fromJson(tblCloudInfo.getLabels(), new com.google.gson.reflect.TypeToken<List<String>>(){}.getType()));
        cloudInfo.setStatus(CloudStatus.fromCode(tblCloudInfo.getStatus()).toStatusCode());
        cloudInfo.setBpId(tblCloudInfo.getBp());
        cloudInfo.setUserId(tblCloudInfo.getOwner());
        cloudInfo.setCreateTime(Utils.formatDate(tblCloudInfo.getCreateTime()));
        cloudInfo.setUpdateTime(Utils.formatDate(tblCloudInfo.getUpdateTime()));
        cloudInfo.setOsServiceEndpoints(JsonUtils.fromJson(tblCloudInfo.getOsServiceEndpoints(), OSServiceEndpoints.class));
        return cloudInfo;
    }

}
