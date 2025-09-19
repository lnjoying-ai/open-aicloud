package com.lnjoying.justice.iam.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.*;
import com.lnjoying.justice.iam.domain.dto.response.resource.TblIamCommonResourceDetail;
import com.lnjoying.justice.iam.domain.dto.response.resource.TblIamCommonResourceSummary;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/16 16:35
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamResource
{
    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String serviceId;

    @ApiModelProperty(value="")
    private String serviceName;

    @ApiModelProperty(value="")
    private String serviceDisplayName;

    @ApiModelProperty(value="")
    private String projectId;

    @ApiModelProperty(value="")
    private String projectName;

    @ApiModelProperty(value="")
    private String projectDisplayName;

    @ApiModelProperty(value="")
    private String name;

    @ApiModelProperty(value="")
    private String resourceType;

    @ApiModelProperty(value="")
    private String description;

    @ApiModelProperty(value="")
    private String lrn;

    /**
     * create time
     */
    @ApiModelProperty(value="create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value="update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;


    List<TblIamAction> actions;

    List<TblIamResourceAttr> attrs;

    List<TblIamConditionKey> conditions;

    private int actionTotal;

    private int  attrTotal;

    private int conditionTotal;

    public static IamResource of (TblIamCommonResourceDetail tblIamCommonResourceDetail)
    {
        IamResource iamResource = new IamResource();
        BeanUtils.copyProperties(tblIamCommonResourceDetail, iamResource);
        iamResource.setResourceType(tblIamCommonResourceDetail.getServiceIamCode() + ":" + tblIamCommonResourceDetail.getName());
        return iamResource;
    }

    public static IamResource of (TblIamCommonResourceSummary tblIamCommonResourceSummary)
    {
        IamResource iamResource = new IamResource();
        BeanUtils.copyProperties(tblIamCommonResourceSummary, iamResource);
        iamResource.setResourceType(tblIamCommonResourceSummary.getServiceIamCode() + ":" + tblIamCommonResourceSummary.getName());
        return iamResource;
    }
}
