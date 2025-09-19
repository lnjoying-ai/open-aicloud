package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_omc_alarm_receiver")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcAlarmReceiver {
    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String ruleId;

    @ApiModelProperty(value="")
    private String receiverId;
}