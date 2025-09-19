package com.lnjoying.justice.cis.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContainerRunLog
{
    private String startTime;

    private String stopTime;

    private InstanceState.StatusCode state;
}
