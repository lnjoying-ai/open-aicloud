package com.lnjoying.justice.omc.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.domain.model.MonitorIntegration;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/30 16:58
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "StatsCountsRsp")
public class StatsCountsRsp
{

    private Object totalVms;

    private Object totalNodes;

    private Object totalClusters;

    private Object totalContainers;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class StatsCounts
    {
        private int running;

        private int abnormal;

        private Map<String, Integer> abnormalDetails;
    }

}
