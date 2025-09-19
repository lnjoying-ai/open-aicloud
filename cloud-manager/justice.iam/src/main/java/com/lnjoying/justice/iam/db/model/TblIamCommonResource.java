package com.lnjoying.justice.iam.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_iam_common_resource")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamCommonResource {
    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String serviceId;

    @ApiModelProperty(value="")
    private String projectId;

    @ApiModelProperty(value="")
    private String name;

    @ApiModelProperty(value="")
    private String description;

    @ApiModelProperty(value="")
    private String lrn;

    /**
    * create time
    */
    @ApiModelProperty(value="create time")
    private Date createTime;

    /**
    * update time
    */
    @ApiModelProperty(value="update time")
    private Date updateTime;
}