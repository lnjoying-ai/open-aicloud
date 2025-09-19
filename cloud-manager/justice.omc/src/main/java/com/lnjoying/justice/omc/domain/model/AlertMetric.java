package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.db.model.TblOmcAlertMetric;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 16:29
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AlertMetric
{

    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String groupId;

    @ApiModelProperty(value="")
    private String metricName;

    @ApiModelProperty(value="")
    private Object label;

    @ApiModelProperty(value="")
    private Integer durationTime;

    @ApiModelProperty(value="")
    private Integer level;

    @ApiModelProperty(value="")
    private String message;

    @ApiModelProperty(value="")
    private String metricKey;

    @ApiModelProperty(value="")
    private String alertConditionDisplayStatement;

    @ApiModelProperty(value="")
    private String alertConditionParam;

    @ApiModelProperty(value="")
    private String dataConditionLabel;

    @ApiModelProperty(value="")
    private String dataFilters;

    @ApiModelProperty(value="")
    private String promContent;

    /**
     * status(0:启用;1:停用)
     */
    @ApiModelProperty(value="status(0:启用;1:停用)")
    private Integer status;

    @ApiModelProperty(value="")
    private String bpId;

    @ApiModelProperty(value="")
    private String userId;

    /**
     * create time
     */
    @ApiModelProperty(value="create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value="update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public static AlertMetric of(TblOmcAlertMetric tblOmcAlertMetric)
    {
        AlertMetric alarmAlert = new AlertMetric();
        BeanUtils.copyProperties(tblOmcAlertMetric, alarmAlert);
        return alarmAlert;
    }
}
