package com.lnjoying.justice.iam.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_iam_resource_condition")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamResourceCondition {
    @ApiModelProperty(value="")
    private String resourceId;

    @ApiModelProperty(value="")
    private String conditionId;
}