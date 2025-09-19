package com.lnjoying.justice.iam.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_iam_resource_attr")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamResourceAttr
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String resourceId;

    @ApiModelProperty(value = "")
    private String attrType;

    @ApiModelProperty(value = "")
    private String attrName;

    @ApiModelProperty(value = "")
    private String description;

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
    private String model;
}