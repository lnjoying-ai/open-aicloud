package com.lnjoying.justice.iam.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_iam_resource_action")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamResourceAction {
    @ApiModelProperty(value="")
    private String actionId;

    @ApiModelProperty(value="")
    private String resourceId;
}