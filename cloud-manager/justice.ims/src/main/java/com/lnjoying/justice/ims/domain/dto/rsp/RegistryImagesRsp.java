package com.lnjoying.justice.ims.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author merak
 **/

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegistryImagesRsp
{
    private String registryId;

    @ApiModelProperty(value = "repo list", example = "192.168.50.16/lnjoying/nginx:1.0.0, 192.168.50.16/lnjoying/nginx:2.0.0")
    private List<String> repos;
}
