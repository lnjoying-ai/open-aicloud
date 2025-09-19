package com.lnjoying.justice.iam.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamPolicyVersion;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/10 20:30
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamPolicyVersion
{
    @ApiModelProperty(value = "")
    private String policyId;

    @ApiModelProperty(value = "")
    private String versionId;

    @ApiModelProperty(value = "")
    private String document;

    // todo add data
    @ApiModelProperty(value = "")
    private boolean defaultVersion;

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
    private String description;

    public static IamPolicyVersion of(TblIamPolicyVersion tblIamPolicyVersion, String defaultVersion)
    {
        IamPolicyVersion iamPolicyVersion = new IamPolicyVersion();
        BeanUtils.copyProperties(tblIamPolicyVersion, iamPolicyVersion);
        if (tblIamPolicyVersion.getVersionId().equalsIgnoreCase(defaultVersion))
        {
            iamPolicyVersion.setDefaultVersion(true);
        }
        return iamPolicyVersion;
    }


}
