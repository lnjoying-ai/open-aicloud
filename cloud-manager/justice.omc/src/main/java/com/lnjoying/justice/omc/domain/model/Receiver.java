package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.db.model.TblOmcReceiver;
import com.lnjoying.justice.omc.domain.dto.rsp.BaseRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/2 11:19
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Receiver
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private String iamUserId;

    @ApiModelProperty(value = "")
    private String iamUserName;

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

    @ApiModelProperty(value = "")
    private String bpName;

    @ApiModelProperty(value = "")
    private String userName;


    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String notifyType;

    public static Receiver of(TblOmcReceiver tblOmcReceiver)
    {
        Receiver receiver = new Receiver();
        BeanUtils.copyProperties(tblOmcReceiver, receiver);
        return receiver;
    }
}
