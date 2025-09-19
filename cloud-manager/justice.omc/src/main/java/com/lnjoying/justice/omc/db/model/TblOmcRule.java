package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_rule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcRule
{
    @ApiModelProperty(value = "")
    private String id;

    /**
     * rule_type(1:static threshold;2:custom promQL)
     */
    @ApiModelProperty(value = "rule_type(1:static threshold;2:custom promQL)")
    private Integer ruleType;

    /**
     * promQL
     */
    @ApiModelProperty(value = "promQL")
    private String expr;

    @ApiModelProperty(value = "")
    private Integer durationTime;

    @ApiModelProperty(value = "")
    private String dataSourceId;

    @ApiModelProperty(value = "")
    private Object labels;

    @ApiModelProperty(value = "")
    private Object annotations;

    @ApiModelProperty(value = "")
    private String promContent;

    @ApiModelProperty(value = "")
    private String alertConditionContents;

    @ApiModelProperty(value = "")
    private String alertConditionParams;

    @ApiModelProperty(value = "")
    private String dataConditionContents;

    @ApiModelProperty(value = "")
    private String dataConditionParams;

    @ApiModelProperty(value = "")
    private String unit;

    @ApiModelProperty(value = "")
    private Boolean notice;

    @ApiModelProperty(value = "")
    private String alertTemplate;

    /**
     * alert_template_type(1:text;2:markdown)
     */
    @ApiModelProperty(value = "alert_template_type(1:text;2:markdown)")
    private Integer alertTemplateType;

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
    private String alarmId;

    @ApiModelProperty(value = "")
    private String alertMessage;
}