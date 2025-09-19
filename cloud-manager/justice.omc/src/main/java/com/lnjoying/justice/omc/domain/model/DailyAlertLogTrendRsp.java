package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/19 15:23
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "DalyAlertLogTrendRsp")
public class DailyAlertLogTrendRsp
{

    private List<DailyTrend> dailyTrends;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyTrend
    {
        @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
        private Date alertDay;

        private AlertLogLevelCount alertLogLevelCount;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlertLogLevelCount
    {
        private long infoCount;

        private long warnCount;

        private long criticalCount;

        private long allCount = infoCount + warnCount + criticalCount;
    }
}
