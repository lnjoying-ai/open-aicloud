package com.lnjoying.justice.omc.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import zipkin2.Call;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 11:23
 */

@Data
@ApiModel(value = "AlertLogProcessReq")
@JsonIgnoreProperties({ "bp_id", "user_id", "user_name", "bp_name", "create_time", "update_time"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AlertLogProcessReq extends BaseReq
{


    @ApiModelProperty(value="")
    @JsonIgnore
    private String alertLogId;

    @ApiModelProperty(value="")
    @JsonProperty("status")
    private String processStatus;

    @ApiModelProperty(value="")
    private Date processTime;

    @ApiModelProperty(value="")
    private String processor;

    @ApiModelProperty(value="")
    private String bpId;

    @ApiModelProperty(value="")
    private String userId;

    private String message;

}
