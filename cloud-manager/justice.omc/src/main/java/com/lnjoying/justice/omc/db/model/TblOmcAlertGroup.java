package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_alert_group")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcAlertGroup
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private Integer status;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

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

    /**
     * scope(0:private;1:bp;2:public)
     */
    @ApiModelProperty(value = "scope(0:private;1:bp;2:public)")
    private Integer scope;

    @ApiModelProperty(value = "")
    private String serviceType;

    @ApiModelProperty(value = "")
    private String resourceType;
}