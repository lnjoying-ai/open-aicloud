package com.lnjoying.justice.ims.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.ims.domain.model.ImsRegistry;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * contain totalNum and list
 *
 * @author merak
 **/

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "RegistriesRsp")
public class RegistriesRsp
{
    private long totalNum;

    private List<ImsRegistry> registries;
}
