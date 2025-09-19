package com.lnjoying.justice.servicemanager.domain.model;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.StatusCode;
import com.lnjoying.justice.servicemanager.common.ServicePortStatus;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortInfo;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortTargetServiceInfo;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServicePort
{
    @SerializedName("service_port_id")
    @JsonProperty("service_port_id")
    private String servicePortId;

    @SerializedName("name")
    @JsonProperty("name")
    private String name;

    @SerializedName("purpose")
    @JsonProperty("purpose")
    private String purpose;

    @SerializedName("description")
    @JsonProperty("description")
    private String description;

    @SerializedName("target_type")
    @JsonProperty("target_type")
    private String targetType;

    @SerializedName("deployment")
    @JsonProperty("deployment")
    private String deployment;

    @SerializedName("deployment_name")
    @JsonProperty("deployment_name")
    private String deploymentName;

    @SerializedName("target_services")
    @JsonProperty("target_services")
    private List<TargetService> targetServices = new ArrayList<>();

    @SerializedName("bp_id")
    @JsonProperty("bp_id")
    private String bpId;

    @SerializedName("bp_name")
    @JsonProperty("bp_name")
    private String bpName;

    @SerializedName("user_id")
    @JsonProperty("user_id")
    private String userId;

    @SerializedName("user_name")
    @JsonProperty("user_name")
    private String userName;

    @SerializedName("create_time")
    @JsonProperty("create_time")
    private String createTime;

    @SerializedName("update_time")
    @JsonProperty("update_time")
    private String updateTime;

    @SerializedName("status")
    @JsonProperty("status")
    private StatusCode status;

    @SerializedName("tags")
    @JsonProperty("tags")
    private List<String> tags;

    public static ServicePort of(TblServicePortInfo tblServicePortInfo)
    {
        ServicePort servicePort = new ServicePort();
        servicePort.setServicePortId(tblServicePortInfo.getId());
        servicePort.setName(tblServicePortInfo.getName());
        servicePort.setPurpose(tblServicePortInfo.getPurpose());
        servicePort.setDescription(tblServicePortInfo.getDescription());
        servicePort.setTargetType(tblServicePortInfo.getTargetType());
        servicePort.setDeployment(tblServicePortInfo.getDeployment());
        servicePort.setCreateTime(Utils.formatDate(tblServicePortInfo.getCreateTime()));
        servicePort.setUpdateTime(Utils.formatDate(tblServicePortInfo.getUpdateTime()));
        servicePort.setBpId(tblServicePortInfo.getBpId());
        servicePort.setUserId(tblServicePortInfo.getUserId());
        servicePort.setTags(JsonUtils.fromJson(tblServicePortInfo.getTags(), new TypeToken<List<String>>(){}.getType()));
        ServicePortStatus status = ServicePortStatus.fromCode(tblServicePortInfo.getStatus());
        servicePort.setStatus(status.toStatusCode());
        return servicePort;
    }

    public void setPortMap(List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos)
    {
        if (CollectionUtils.isNotEmpty(tblServicePortTargetServiceInfos))
        {
            tblServicePortTargetServiceInfos.forEach(tblServicePortTargetServiceInfo -> {
                TargetService targetService = TargetService.of(tblServicePortTargetServiceInfo);
                this.targetServices.add(targetService);
            });
        }
    }
}
