package com.lnjoying.justice.iam.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.domain.dto.response.policy.TblIamPolicyDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/10 19:20
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamPolicy
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private String displayName;

    /**
     * policy type(1:system;2:custom;)
     */
    @ApiModelProperty(value = "policy type(1:system;2:custom;)")
    private Integer policyType;

    @JsonIgnore
    @ApiModelProperty(value = "")
    private Boolean autogen;

    @ApiModelProperty(value = "")
    private String defaultVersion;

    @ApiModelProperty(value = "")
    private Integer attachmentCount;

    @ApiModelProperty(value = "")
    private Boolean attachable;

    @ApiModelProperty(value = "")
    private Object tags;

    @ApiModelProperty(value = "")
    private String arn;

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

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

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

    @ApiModelProperty(value = "")
    private String document;

    public static IamPolicy of (TblIamPolicyDetail tblIamPolicyDetail)
    {
        IamPolicy iamPolicy = new IamPolicy();
        BeanUtils.copyProperties(tblIamPolicyDetail, iamPolicy);
        return iamPolicy;
    }
}
