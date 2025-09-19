package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.db.model.TblOmcAlarm;
import com.lnjoying.justice.omc.domain.dto.rsp.BaseRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/2 16:00
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Alarm extends BaseRsp
{
    private String id;

    private String name;


    private String description;

    @ApiModelProperty(value="")
    private String alertGroupId;

    @ApiModelProperty(value="")
    private String alertGroupName;


    @ApiModelProperty(value="")
    private String alertMetricId;

    private String alertMetricName;


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

    @ApiModelProperty(value = "")
    private Integer status;

    /**
     * level(1:info;2:warning;3:critical)
     */
    @ApiModelProperty(value="level(1:info;2:warning;3:critical)")
    private Integer level;

    @ApiModelProperty(value="")
    private String alertMessage;

    @ApiModelProperty(value="")
    private Object labels;

    @ApiModelProperty(value="")
    private Object annotations;

    // todo
    private String targetServiceType;

    private String targetResourceType;

   /* private List<NotifyRule> notifyRules;*/
   @ApiModelProperty(value = "")
   private String bpId;

    @ApiModelProperty(value = "")
    private String userId;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
