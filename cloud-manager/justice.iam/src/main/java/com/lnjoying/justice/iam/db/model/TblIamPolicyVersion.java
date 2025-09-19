package com.lnjoying.justice.iam.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_iam_policy_version")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamPolicyVersion
{
    @ApiModelProperty(value = "")
    private String policyId;

    @ApiModelProperty(value = "")
    @ResourceInstanceName
    private String versionId;

    @ApiModelProperty(value = "")
    private String document;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String description;
}