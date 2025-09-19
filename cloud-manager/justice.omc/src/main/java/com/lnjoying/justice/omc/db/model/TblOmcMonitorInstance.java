package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_monitor_instance")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcMonitorInstance
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    private String taskId;

    /**
     * 任务类型(1:轻量级节点部署任务;2：nextstack云监控部署任务;3：openstack云监控部署任务;4：GPU监控任务
     * )
     */
    @ApiModelProperty(value = "任务类型(1:轻量级节点部署任务;2：nextstack云监控部署任务;3：openstack云监控部署任务;4：GPU监控任务,),,")
    private Integer taskType;

    @ApiModelProperty(value = "")
    private String exporterType;

    @ApiModelProperty(value = "")
    private Object configs;

    @ApiModelProperty(value = "")
    private String stackId;

    @ApiModelProperty(value = "")
    private Object labels;

    @ApiModelProperty(value = "")
    private String dataSourceId;

    @ApiModelProperty(value = "")
    private String endpoint;

    @ApiModelProperty(value = "")
    private String regionId;

    @ApiModelProperty(value = "")
    private String siteId;

    @ApiModelProperty(value = "")
    private String nodeId;

    /**
     * 目标类型(1:轻量级节点;2:nextstack;3:openstack)
     */
    @ApiModelProperty(value = "目标类型(1:轻量级节点;2:nextstack;3:openstack)")
    private Integer targetType;

    /**
     * 目标(节点的target保存节点信息，云target保存云id)
     */
    @ApiModelProperty(value = "目标(节点的target保存节点信息，云target保存云id)")
    private String targetId;

    /**
     * 部署状态(1:pending;2：running;3:succeeded;4:failed;5:progressing)
     */
    @ApiModelProperty(value = "部署状态(1:pending;2：running;3:succeeded;4:failed;5:progressing),")
    private Integer status;

    @ApiModelProperty(value = "")
    private String detail;

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
    private Object stackParams;

    @ApiModelProperty(value = "")
    private Object customParams;

    @ApiModelProperty(value = "")
    private String stackTemplateVersionId;

    @ApiModelProperty(value = "")
    private String specId;

    /**
     * 目标(节点的target保存节点信息，云target保存云id)
     */
    @ApiModelProperty(value = "目标(节点的target保存节点信息，云target保存云id)")
    private Object targets;
}