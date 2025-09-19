package com.lnjoying.justice.iam.domain.dto.response.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.domain.model.IamResource;
import com.lnjoying.justice.iam.domain.model.IamService;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/3 10:31
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "ResourceRsp")
public class ResourceRsp
{
    private long totalNum;

    private List<IamResource> services;
}
