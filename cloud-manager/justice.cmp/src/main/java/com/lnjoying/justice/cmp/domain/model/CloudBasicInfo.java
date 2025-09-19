package com.lnjoying.justice.cmp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.common.CloudProduct;
import com.lnjoying.justice.cmp.common.CloudStatus;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.StatusCode;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.List;

@Data
public class CloudBasicInfo
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

    @SerializedName("create_time")
    @JsonProperty("create_time")
    private String createTime;

    @SerializedName("update_time")
    @JsonProperty("update_time")
    private String updateTime;

    @SerializedName("labels")
    @JsonProperty("labels")
    private List<String> labels;

    @SerializedName("usage")
    @JsonProperty("usage")
    private Double usage;

    @SerializedName("user_resource_metrics")
    @JsonProperty("user_resource_metrics")
    private UserResourceMetrics userResourceMetrics;

    public static CloudBasicInfo of(TblCloudInfo tblCloudInfo)
    {
        CloudBasicInfo cloudBasicInfo = new CloudBasicInfo();
        cloudBasicInfo.setCloudId(tblCloudInfo.getCloudId());
        cloudBasicInfo.setName(tblCloudInfo.getName());
        cloudBasicInfo.setProduct(tblCloudInfo.getProduct());
        cloudBasicInfo.setProductShort(CloudProduct.fromName(cloudBasicInfo.getProduct()).getShortName());
        cloudBasicInfo.setStatus(CloudStatus.fromCode(tblCloudInfo.getStatus()).toStatusCode());
        cloudBasicInfo.setCreateTime(Utils.formatDate(tblCloudInfo.getCreateTime()));
        cloudBasicInfo.setUpdateTime(Utils.formatDate(tblCloudInfo.getUpdateTime()));
        cloudBasicInfo.setLabels(JsonUtils.fromJson(tblCloudInfo.getLabels(), new com.google.gson.reflect.TypeToken<List<String>>(){}.getType()));
        return cloudBasicInfo;
    }

}
