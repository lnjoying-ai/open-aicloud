package com.lnjoying.justice.cis.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.cis.domain.model.ContainerConfig;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/11/2 20:26
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DockerContainerInfoDetail extends DockerContainerInfo
{
    List<ContainerConfig> containerConfigs;

    List<String> containerVolumes;

}
