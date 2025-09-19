package com.lnjoying.justice.ims.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * registry node info
 *
 * @author merak
 **/

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "RegistryNodesRsp")
public class RegistryNodesRsp
{
    private String registryId;

    @ApiModelProperty(value = "regions")
    private List<RegionResourceService.RegionDto> regions;

}
