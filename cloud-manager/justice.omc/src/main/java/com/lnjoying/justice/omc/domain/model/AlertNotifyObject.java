package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/14 14:56
 */


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public  class AlertNotifyObject
{
    @ApiModelProperty(value="")
    private String receiverId;

    @ApiModelProperty(value="")
    private String receiverName;


    @ApiModelProperty(value="")
    private String notifyChannels;

    private String optionalNotifyChannels;
}