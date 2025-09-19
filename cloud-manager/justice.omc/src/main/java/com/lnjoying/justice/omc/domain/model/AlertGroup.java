package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.db.model.TblOmcAlertGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 16:29
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AlertGroup
{

    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String name;

    @ApiModelProperty(value="")
    private String description;

    @ApiModelProperty(value="")
    private Integer status;

    public static AlertGroup of(TblOmcAlertGroup tblOmcAlertGroup)
    {
        AlertGroup alarmGroup = new AlertGroup();
        BeanUtils.copyProperties(tblOmcAlertGroup, alarmGroup);
        return alarmGroup;
    }
}
