package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_alert_silence_rule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcAlertSilenceRule
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private Boolean enable;

    @ApiModelProperty(value = "")
    private Boolean matchAll;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    private Integer evalInterval;

    @ApiModelProperty(value = "")
    private Object priorities;

    @ApiModelProperty(value = "")
    private Object tags;

    @ApiModelProperty(value = "")
    private Object days;

    @ApiModelProperty(value = "")
    private Object weeks;

    /**
     * type(0:once;1:day;2:week)
     */
    @ApiModelProperty(value = "type(0:once;1:day;2:week)")
    private Integer type;

    @ApiModelProperty(value = "")
    private Integer times;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    private Date startTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    private Date endTime;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    @ApiModelProperty(value = "")
    private Date createTime;

    @ApiModelProperty(value = "")
    private Date updateTime;
}