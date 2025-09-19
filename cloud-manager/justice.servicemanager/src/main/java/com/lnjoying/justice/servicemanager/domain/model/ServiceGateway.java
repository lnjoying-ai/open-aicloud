package com.lnjoying.justice.servicemanager.domain.model;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.StatusCode;
import com.lnjoying.justice.servicemanager.common.ServiceGatewayStatus;
import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayInfo;
import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayPortInfo;
import com.lnjoying.justice.servicemanager.domain.model.servicecomb.Instance;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ServiceGateway
{
    @SerializedName("service_gateway_id")
    @JsonProperty("service_gateway_id")
    private String serviceGatewayId;

    @SerializedName("name")
    @JsonProperty("name")
    private String name;

    @SerializedName("description")
    @JsonProperty("description")
    private String description;

    @SerializedName("instance_id")
    @JsonProperty("instance_id")
    private String instanceId;

    @SerializedName("endpoint")
    @JsonProperty("endpoint")
    private String endpoint;

    @SerializedName("purpose")
    @JsonProperty("purpose")
    private String purpose;

    @SerializedName("status")
    @JsonProperty("status")
    private StatusCode status;

    @SerializedName("port_ranges")
    @JsonProperty("port_ranges")
    private List<ServiceGatewayPortRange> portRanges = new ArrayList<>();

    @SerializedName("create_time")
    @JsonProperty("create_time")
    private String createTime;

    @SerializedName("update_time")
    @JsonProperty("update_time")
    private String updateTime;

    public static ServiceGateway of(TblServiceGatewayInfo tblServiceGatewayInfo)
    {
        ServiceGateway serviceGateway = new ServiceGateway();
        serviceGateway.setServiceGatewayId(tblServiceGatewayInfo.getId());
        serviceGateway.setName(tblServiceGatewayInfo.getName());
        serviceGateway.setDescription(tblServiceGatewayInfo.getDescription());
        serviceGateway.setInstanceId(tblServiceGatewayInfo.getInstanceId());
        serviceGateway.setEndpoint(tblServiceGatewayInfo.getEndpoint());
        serviceGateway.setPurpose(tblServiceGatewayInfo.getPurpose());
        serviceGateway.setStatus(tblServiceGatewayInfo.getStatus() == null ? null : ServiceGatewayStatus.fromCode(tblServiceGatewayInfo.getStatus()).toStatusCode());
        serviceGateway.setCreateTime(Utils.formatDate(tblServiceGatewayInfo.getCreateTime()));
        serviceGateway.setUpdateTime(Utils.formatDate(tblServiceGatewayInfo.getUpdateTime()));
        return serviceGateway;
    }

    public void setPortRanges(List<TblServiceGatewayPortInfo> tblServiceGatewayPortInfos)
    {
        if (CollectionUtils.isNotEmpty(tblServiceGatewayPortInfos))
        {
            tblServiceGatewayPortInfos.forEach(tblServiceGatewayPortInfo -> {
                ServiceGatewayPortRange serviceGatewayPortRange = new ServiceGatewayPortRange();
                serviceGatewayPortRange.setPortRangeId(tblServiceGatewayPortInfo.getId());
                serviceGatewayPortRange.setExternalIp(tblServiceGatewayPortInfo.getExternalIp());
                serviceGatewayPortRange.setInternalIp(tblServiceGatewayPortInfo.getInternalIp());
                serviceGatewayPortRange.setPortRangeMin(tblServiceGatewayPortInfo.getPortRangeMin());
                serviceGatewayPortRange.setPortRangeMax(tblServiceGatewayPortInfo.getPortRangeMax());
                serviceGatewayPortRange.setListenPortRangeMin(tblServiceGatewayPortInfo.getListenPortRangeMin());
                serviceGatewayPortRange.setListenPortRangeMax(tblServiceGatewayPortInfo.getListenPortRangeMax());
                serviceGatewayPortRange.setTotal(tblServiceGatewayPortInfo.getTotal());
                serviceGatewayPortRange.setLeft(tblServiceGatewayPortInfo.getLeft());
                serviceGatewayPortRange.setDescription(tblServiceGatewayPortInfo.getDescription());
                this.portRanges.add(serviceGatewayPortRange);
            });
        }
    }

    public void setStatusByServiceComb(Map<String, Instance> serviceGatewayMap)
    {
        if (serviceGatewayMap != null && serviceGatewayMap.containsKey(this.endpoint))
        {
            Instance instance = serviceGatewayMap.get(this.endpoint);
            if (instance != null && instance.getStatus() != null && instance.getStatus().equals("UP"))
            {
                this.status = ServiceGatewayStatus.UP.toStatusCode();
            }
        }
        else
        {
            this.status = ServiceGatewayStatus.DOWN.toStatusCode();
        }
    }
}
