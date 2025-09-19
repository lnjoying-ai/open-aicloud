package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_monitor_task")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcMonitorTask
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    private String description;

    /**
     * 任务类型(1:轻量级节点部署任务;2：nextstack云监控部署任务;3：openstack云监控部署任务;4：GPU监控任务
     * )
     */
    @ApiModelProperty(value = "任务类型(1:轻量级节点部署任务;2：nextstack云监控部署任务;3：openstack云监控部署任务;4：GPU监控任务,),,")
    private Integer taskType;

    @ApiModelProperty(value = "")
    private String dataSourceId;

    @ApiModelProperty(value = "")
    private Object configs;

    /**
     * 部署状态(1:pending;2：running;3:succeeded;4:failed;5:progressing)
     */
    @ApiModelProperty(value = "部署状态(1:pending;2：running;3:succeeded;4:failed;5:progressing),")
    private Integer deploymentStatus;

    /**
     * 目标类型(1:轻量级节点;2:nextstack;3:openstack)
     */
    @ApiModelProperty(value = "目标类型(1:轻量级节点;2:nextstack;3:openstack)")
    private Integer targetType;

    /**
     * 目标(节点的target保存节点信息，云target保存云id)
     */
    @ApiModelProperty(value = "目标(节点的target保存节点信息，云target保存云id)")
    private Object targets;

    /**
     * 部署开始时间
     */
    @ApiModelProperty(value = "部署开始时间")
    private Date startTime;

    /**
     * 部署完成事件
     */
    @ApiModelProperty(value = "部署完成事件")
    private Date completionTime;

    /**
     * 组件参数
     */
    @ApiModelProperty(value = "组件参数")
    private Object params;

    @ApiModelProperty(value = "")
    private Object stackIds;

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
}