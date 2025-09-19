package com.lnjoying.justice.iam.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_iam_condition_func")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamConditionFunc {
    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String conditionType;

    @ApiModelProperty(value="")
    private String operation;

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