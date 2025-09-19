package com.lnjoying.justice.ims.domain.dto.rsp;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * repo build history
 *
 * @author merak
 **/

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "RegistryRepoBuildHistory")
public class RegistryRepoBuildHistoryRsp
{
    @JsonRawValue
    private String buildHistory;
}
