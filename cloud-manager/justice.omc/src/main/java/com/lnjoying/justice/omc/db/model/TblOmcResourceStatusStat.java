package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_resource_status_stat")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcResourceStatusStat
{
    @ApiModelProperty(value = "")
    private String id;

    /**
     * resource type(1:vm;2:node;3:cluster;4:container)
     */
    @ApiModelProperty(value = "resource type(1:vm;2:node;3:cluster;4:container)")
    private Integer resourceType;

    /**
     * )
     */
    @ApiModelProperty(value = ")")
    private String status;

    @ApiModelProperty(value = "")
    private Integer statusCounts;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    private Date createTime;

    /**
     * day
     */
    @ApiModelProperty(value = "day")
    private Date day;
}