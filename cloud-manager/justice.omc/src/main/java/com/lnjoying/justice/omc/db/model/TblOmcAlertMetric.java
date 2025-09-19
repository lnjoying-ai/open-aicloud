package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_alert_metric")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcAlertMetric
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String groupId;

    @ApiModelProperty(value = "")
    private String metricName;

    @ApiModelProperty(value = "")
    private Object label;

    @ApiModelProperty(value = "")
    private Integer durationTime;

    @ApiModelProperty(value = "")
    private Integer level;

    @ApiModelProperty(value = "")
    private String message;

    @ApiModelProperty(value = "")
    private String metricKey;

    @ApiModelProperty(value = "")
    private String alertConditionDisplayStatement;

    @ApiModelProperty(value = "")
    private String alertConditionParam;

    @ApiModelProperty(value = "")
    private String dataConditionLabel;

    @ApiModelProperty(value = "")
    private String dataFilters;

    @ApiModelProperty(value = "")
    private String promContent;

    /**
     * status(0:启用;1:停用)
     */
    @ApiModelProperty(value = "status(0:启用;1:停用)")
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
}