package com.lnjoying.justice.omc.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 19:58
 */


@Data
@ApiModel(value = "AddReceiverReq")
@JsonIgnoreProperties({ "bp_id", "user_id", "user_name", "bp_name", "create_time", "update_time"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddReceiverReq extends BaseReq
{

    @ApiModelProperty(value="")
    private String name;

    @ApiModelProperty(value="")
    private String description;

    @ApiModelProperty(value="")
    @JsonProperty(value = "receiver_user_id")
    private String iamUserId;

    @ApiModelProperty(value="")
    private String email;

    @ApiModelProperty(value="")
    private String phone;

    @ApiModelProperty(value="")
    private Object configs;

    @ApiModelProperty(value = "")
    private String notifyType;
}
