package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.cluster.manager.common.InstanceState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CoreService
{
    @JsonProperty("service_name")
    private String serviceName;
    
    @JsonProperty("status")
    private InstanceState.StatusCode status;
}
