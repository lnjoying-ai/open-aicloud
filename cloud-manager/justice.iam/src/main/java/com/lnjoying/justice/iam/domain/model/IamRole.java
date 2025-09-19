package com.lnjoying.justice.iam.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lnjoying.justice.iam.domain.dto.response.policy.TblIamPolicyDetail;
import com.lnjoying.justice.iam.domain.dto.response.role.TblIamRoleDetail;
import com.lnjoying.justice.iam.handler.StringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/10 13:59
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamRole
{
    @ApiModelProperty(value = "")
    private Long roleId;

    @ApiModelProperty(value = "")
    @JsonProperty("iam_code")
    private String platform;

    @ApiModelProperty(value = "")
    @JsonProperty("name")
    private String role;

    @ApiModelProperty(value = "")
    private String projectId;

    @ApiModelProperty(value = "")
    @JsonSerialize(using = StringSerializer.class)
    private String description = "";

    /**
     * role type(1:system;2:custom;)
     */
    @ApiModelProperty(value = "role type(1:system;2:custom;)")
    private Integer roleType;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * username
     */
    @ApiModelProperty(value = "user name")
    private String userName;

    /**
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;

    public static IamRole of (TblIamRoleDetail tblIamRoleDetail)
    {
        IamRole iamRole = new IamRole();
        BeanUtils.copyProperties(tblIamRoleDetail, iamRole);
        return iamRole;
    }
}
