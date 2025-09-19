package com.lnjoying.justice.iam.domain.dto.request.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/16 19:10
 */

@Data
@ApiModel(value = "AddProjectReq")
@JsonIgnoreProperties({"id", "bp_id", "user_id", "bp_name", "user_name", "create_time", "update_time"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateCommonResourceReq extends BaseReq
{
    @ApiModelProperty(value="")
    private String serviceId;

    @ApiModelProperty(value="")
    private String projectId;

    @ApiModelProperty(value="")
    private String name;

    @ApiModelProperty(value="")
    private String description;
}
