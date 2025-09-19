package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_alert_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcAlertLog
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String alarmId;

    @ApiModelProperty(value = "")
    private String ruleId;

    @ApiModelProperty(value = "")
    private String alertGroupId;

    /**
     * alert type(1:event;2:monitor)
     */
    @ApiModelProperty(value = "alert type(1:event;2:monitor)")
    private Integer alertType;

    /**
     * alert_status(1:firing;2:resolved;3pending;4:inhibited;5:silenced;6:expired;7:unknown)
     */
    @ApiModelProperty(value = "alert_status(1:firing;2:resolved;3pending;4:inhibited;5:silenced;6:expired;7:unknown)")
    private Integer alertStatus;

    /**
     * level(1:info;2:warning;3:critical)
     */
    @ApiModelProperty(value = "level(1:info;2:warning;3:critical)")
    private Integer level;

    /**
     * way(1:sms;2:email)
     */
    @ApiModelProperty(value = "way(1:sms;2:email)")
    private Integer way;

    @ApiModelProperty(value = "")
    private Object labels;

    @ApiModelProperty(value = "")
    private String summary;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private Boolean inSilence;

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
    private String resourceTypeId;

    @ApiModelProperty(value = "")
    private String resourceId;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    @ApiModelProperty(value = "")
    private Integer uniqueHash;

    /**
     * first alert time
     */
    @ApiModelProperty(value = "first alert time")
    private Date firstAlertTime;

    /**
     * last alert time
     */
    @ApiModelProperty(value = "last alert time")
    private Date lastAlertTime;

    @ApiModelProperty(value = "")
    private Integer times;

    @ApiModelProperty(value = "")
    private Integer triggerTimes;
}