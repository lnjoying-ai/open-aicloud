package com.lnjoying.justice.servicemanager.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.servicemanager.domain.model.ServicePort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetServicePortRsp
{
    @SerializedName("service_port")
    @JsonProperty("service_port")
    ServicePort servicePort;
}
