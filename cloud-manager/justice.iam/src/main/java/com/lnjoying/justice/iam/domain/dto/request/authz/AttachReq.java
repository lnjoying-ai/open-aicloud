package com.lnjoying.justice.iam.domain.dto.request.authz;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/14 20:15
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttachReq extends BaseReq
{

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
