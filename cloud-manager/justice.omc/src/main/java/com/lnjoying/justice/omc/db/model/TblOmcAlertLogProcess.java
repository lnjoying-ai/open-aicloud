package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_alert_log_process")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcAlertLogProcess
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String alertLogId;

    @ApiModelProperty(value = "")
    private String processStatus;

    @ApiModelProperty(value = "")
    private Date processTime;

    @ApiModelProperty(value = "")
    private String processor;

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
    private String message;
}