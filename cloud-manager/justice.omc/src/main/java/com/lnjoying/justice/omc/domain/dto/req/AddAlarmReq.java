package com.lnjoying.justice.omc.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.domain.model.AlertRule;
import com.lnjoying.justice.omc.domain.model.NotifyRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:16
 */

@Data
@ApiModel(value = "AddAlarmReq")
@JsonIgnoreProperties({ "id", "bpId", "userId", "bpName", "userName", "createTime", "updateTime"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddAlarmReq extends BaseReq
{
    private String id;

    private String name;

    private String description;

    @ApiModelProperty(value="")
    @JsonProperty(value = "alarm_group_id")
    private String alertGroupId;

    @ApiModelProperty(value="")
    @JsonProperty(value = "alarm_metric_id")
    private String alertMetricId;

    @ApiModelProperty(value="")
    private String dataSourceId;

    /**
     * rule_type(1:static threshold;2:custom promQL)
     */
    @ApiModelProperty(value="rule_type(1:static threshold;2:custom promQL)")
    private Integer ruleType;

    /**
     * promQL
     */
    @ApiModelProperty(value="promQL")
    private String expr;

    @ApiModelProperty(value="")
    private Integer durationTime;

    /**
     * level(1:info;2:warning;3:critical)
     */
    @ApiModelProperty(value="level(1:info;2:warning;3:critical)")
    private Integer level;

    @ApiModelProperty(value="")
    private String alertMessage;

    @ApiModelProperty(value="")
    private Map<String, String> labels;

    @ApiModelProperty(value="")
    private Map<String, String> annotations;

    // todo
    private int targetTYpe;

    private List<NotifyRule> notifyRules;

    private AlertRuleReq alertRule;

}
