package com.lnjoying.justice.omc.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_omc_receiver")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblOmcReceiver
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    @ResourceInstanceName
    private String name;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private String iamUserId;

    @ApiModelProperty(value = "")
    private String email;

    @ApiModelProperty(value = "")
    private String phone;

    @ApiModelProperty(value = "")
    private Object configs;

    @ApiModelProperty(value = "")
    private Integer status;

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
    private String notifyType;
}