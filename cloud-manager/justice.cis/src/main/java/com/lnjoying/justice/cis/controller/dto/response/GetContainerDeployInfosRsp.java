package com.lnjoying.justice.cis.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description: ContainerDeployInfos for response
 * @Author: Merak
 * @Date: 2022/1/10 10:31
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "GetContainerDeployInfosRsp")
public class GetContainerDeployInfosRsp
{
    int totalNum;

    List<DockerContainerDeployInfo> deployments;
}
