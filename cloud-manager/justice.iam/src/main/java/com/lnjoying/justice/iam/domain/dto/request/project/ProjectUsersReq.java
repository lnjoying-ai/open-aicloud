package com.lnjoying.justice.iam.domain.dto.request.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 20:38
 */

@Data
@ApiModel(value = "AddProjectUsersReq")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectUsersReq extends BaseReq
{

    @ApiModelProperty(value="userIds")
    private List<String> userIds = new ArrayList<>();
}
