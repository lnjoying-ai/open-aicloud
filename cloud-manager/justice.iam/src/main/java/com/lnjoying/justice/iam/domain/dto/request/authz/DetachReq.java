package com.lnjoying.justice.iam.domain.dto.request.authz;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/14 15:19
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetachReq
{
    //@NotBlank(message = "project id can not be empty")
    @ApiModelProperty(value = "")
    private String projectId;

    @ApiModelProperty(value = "")
    private String projectName;

    @ApiModelProperty(value = "")
    private String principalId;

    /**
     * principal type(1:user;2:group;3:role)
     */
    @ApiModelProperty(value = "principal type(1:user;2:group;3:role)")
    private Integer principalType;


    @ApiModelProperty(value = "")
    private List<String> policyIds;

    @ApiModelProperty(value = "")
    private List<Long> roleIds;

    @ApiModelProperty(value = "")
    private String description;
}
