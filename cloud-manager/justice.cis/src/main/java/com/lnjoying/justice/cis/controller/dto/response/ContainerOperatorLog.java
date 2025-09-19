package com.lnjoying.justice.cis.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.schema.entity.OperType;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContainerOperatorLog
{
    private String operId;

    private String operatorId;

    private String operatorName;

    private String operTime;

    private OperType operType;

    private String operResult;
}
