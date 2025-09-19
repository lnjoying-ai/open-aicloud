package com.lnjoying.justice.servicemanager.domain.model;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayPortAllocation;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ServiceGatewayPortRange
{
    @SerializedName("port_range_id")
    @JsonProperty("port_range_id")
    private String portRangeId;

    @SerializedName("internal_ip")
    @JsonProperty("internal_ip")
    private String internalIp;

    @SerializedName("external_ip")
    @JsonProperty("external_ip")
    private String externalIp;

    @SerializedName("port_range_min")
    @JsonProperty("port_range_min")
    private Integer portRangeMin;

    @SerializedName("port_range_max")
    @JsonProperty("port_range_max")
    private Integer portRangeMax;

    @SerializedName("listen_port_range_min")
    @JsonProperty("listen_port_range_min")
    private Integer listenPortRangeMin;

    @SerializedName("listen_port_range_max")
    @JsonProperty("listen_port_range_max")
    private Integer listenPortRangeMax;

    @SerializedName("total")
    @JsonProperty("total")
    private Integer total;

    @SerializedName("left")
    @JsonProperty("left")
    private Integer left;

    @SerializedName("description")
    @JsonProperty("description")
    private String description;

    @SerializedName("available_ports")
    @JsonProperty("available_ports")
    private Set<Integer> availablePorts;

    public void setAvailablePorts(List<TblServiceGatewayPortAllocation> tblServiceGatewayPortAllocations)
    {
        this.availablePorts = new HashSet<>();

        for (int i = this.listenPortRangeMin; i <= this.listenPortRangeMax; i++)
        {
            this.availablePorts.add(i);
        }

        if (CollectionUtils.isNotEmpty(tblServiceGatewayPortAllocations))
        {
            tblServiceGatewayPortAllocations.forEach(tblServiceGatewayPortAllocation -> {
                this.availablePorts.remove(tblServiceGatewayPortAllocation.getPort());
            });
        }

        this.left = this.availablePorts.size();
    }
}
