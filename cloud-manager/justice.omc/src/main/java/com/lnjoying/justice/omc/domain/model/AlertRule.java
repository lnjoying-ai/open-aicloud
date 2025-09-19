package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/8 11:30
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//@JsonIgnoreProperties({ "id", "bpId", "userId", "bpName", "userName", "createTime", "updateTime"})
public class AlertRule
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

    private String alertMessage;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;
}
