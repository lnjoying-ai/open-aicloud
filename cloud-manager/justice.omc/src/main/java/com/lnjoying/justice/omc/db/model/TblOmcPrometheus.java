package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_prometheus")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcPrometheus
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    /**
     * 类型(0:server;1:agent)
     */
    @ApiModelProperty(value = "类型(0:server;1:agent)")
    private Integer type;

    /**
     * 状态(1:running;2:starting;3:error;4:crashed;5:updating configguration;6:paused;7:unknown)
     */
    @ApiModelProperty(value = "状态(1:running;2:starting;3:error;4:crashed;5:updating configguration;6:paused;7:unknown)")
    private Integer status;

    @ApiModelProperty(value = "")
    private Object lables;

    @ApiModelProperty(value = "")
    private Object auth;

    @ApiModelProperty(value = "")
    private String regionId;

    @ApiModelProperty(value = "")
    private String siteId;

    @ApiModelProperty(value = "")
    private String nodeId;

    @ApiModelProperty(value = "")
    private Boolean global;

    @ApiModelProperty(value = "")
    private Boolean systemDefault;

    @ApiModelProperty(value = "")
    private Object extra;

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

    @ApiModelProperty(value = "")
    private String internalEndpointUrl;

    @ApiModelProperty(value = "")
    private String externalEndpointUrl;
}