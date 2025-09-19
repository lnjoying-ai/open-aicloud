package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_omc_notify_object")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcNotifyObject {
    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String receiverId;

    @ApiModelProperty(value="")
    private String notifyRuleId;

    @ApiModelProperty(value="")
    private String notifyChannels;
}