package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_omc_rule_resource")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcRuleResource {
    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String resourceType;

    @ApiModelProperty(value="")
    private String resourceTypeId;

    @ApiModelProperty(value="")
    private String resourceId;

    @ApiModelProperty(value="")
    private String alarmDataType;

    @ApiModelProperty(value="")
    private String bpId;

    @ApiModelProperty(value="")
    private String userId;

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