package com.lnjoying.justice.iam.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/15 11:03
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamPolicyAttachment
{
    private String id;

    @ApiModelProperty(value = "")
    private String projectId;

    @ApiModelProperty(value = "")
    private String projectName;

    private String projectDisplayName;

    private String policyId;

    private String policyName;

    private String policyDisplayName;

    /**
     * policy type(1:system;2:custom;)
     */
    @ApiModelProperty(value = "policy type(1:system;2:custom;)")
    private Integer policyType;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private String principalId;

    @ApiModelProperty(value = "")
    private String principalName;

    /**
     * principal type(1:user;2:group;3:role)
     */
    @ApiModelProperty(value = "principal type(1:user;2:group;3:role)")
    private Integer principalType;

    /**
     * attach time
     */
    @ApiModelProperty(value = "attach time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date attachTime;


    @ApiModelProperty(value = "")
    private String bpId;
    @ApiModelProperty(value = "")
    private String userId;
}
