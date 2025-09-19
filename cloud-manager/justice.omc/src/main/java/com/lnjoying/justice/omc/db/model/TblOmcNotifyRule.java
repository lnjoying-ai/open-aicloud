package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_notify_rule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcNotifyRule
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private Boolean repeatNotify;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    private Date notifyStartTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    private Date notifyEndTime;

    @ApiModelProperty(value = "")
    private String alarmId;
}