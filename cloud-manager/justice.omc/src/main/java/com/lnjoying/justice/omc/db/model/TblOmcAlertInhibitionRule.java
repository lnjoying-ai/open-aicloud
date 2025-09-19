package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

import lombok.*;

@ApiModel(value = "tbl_omc_alert_inhibition_rule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblOmcAlertInhibitionRule
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
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    @ApiModelProperty(value = "")
    private Date createTime;

    @ApiModelProperty(value = "")
    private Date updateTime;
}