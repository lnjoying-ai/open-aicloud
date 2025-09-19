package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CoreServiceMetrics
{
    @JsonProperty("service_name")
    private String serviceName;
    
    @JsonProperty("running_num")
    private int runningNum;

    @JsonProperty("failed_num")
    private int failedNum;
}
