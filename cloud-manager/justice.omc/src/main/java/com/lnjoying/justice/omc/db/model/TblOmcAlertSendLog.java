package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_omc_alert_send_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcAlertSendLog {
    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String alertLogId;

    /**
    * send status(1:success;2:failed;3:unknown)
    */
    @ApiModelProperty(value="send status(1:success;2:failed;3:unknown)")
    private Integer sendStatus;

    @ApiModelProperty(value="")
    private String log;

    @ApiModelProperty(value="")
    private Date createTime;
}