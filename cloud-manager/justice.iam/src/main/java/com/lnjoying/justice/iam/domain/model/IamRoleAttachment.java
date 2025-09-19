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
public class IamRoleAttachment
{
    private String id;

    @ApiModelProperty(value = "")
    private String projectId;

    @ApiModelProperty(value = "")
    private String projectName;

    private String projectDisplayName;

    private String roleId;

    private String rolePlatform;

    private String roleName;

    /**
     * policy type(1:system;2:custom;)
     */
    @ApiModelProperty(value = "role type(1:system;2:custom;)")
    private Integer roleType;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private String principalId;

    @ApiModelProperty(value = "")
    private String principalName;

    /**
     * principal type(1:user;2:group;3:role)
     */
    @ApiModelProperty(value = "principal type(1:user;2:group)")
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
