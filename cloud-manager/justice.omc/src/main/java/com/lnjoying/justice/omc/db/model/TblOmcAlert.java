package com.lnjoying.justice.omc.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_omc_alert")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcAlert {
    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String alarmId;

    /**
    * way(1:sms;2:email)
    */
    @ApiModelProperty(value="way(1:sms;2:email)")
    private Integer way;

    @ApiModelProperty(value="")
    private Object waySettings;

    @ApiModelProperty(value="")
    private Integer silence;

    @ApiModelProperty(value="")
    private String selienceExpression;

    @ApiModelProperty(value="")
    private String alertTemplate;

    /**
    * alert_template_type(1:text;2:markdown)
    */
    @ApiModelProperty(value="alert_template_type(1:text;2:markdown)")
    private Integer alertTemplateType;

    /**
    * create time
    */
    @ApiModelProperty(value="create time")
    private Date createTime;

    /**
    * update time
    */
    @ApiModelProperty(value="update time")
    private Date updateTime;
}