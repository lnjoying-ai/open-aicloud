package com.lnjoying.justice.servicemanager.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.servicemanager.domain.model.ServiceGateway;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetServiceGatewaysRsp
{
    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;

    @SerializedName("service_gateways")
    @JsonProperty("service_gateways")
    List<ServiceGateway> serviceGateways;
}
