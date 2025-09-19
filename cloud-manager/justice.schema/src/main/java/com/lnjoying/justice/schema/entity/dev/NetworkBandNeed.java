package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NetworkBandNeed implements Serializable
{
    @ApiModelProperty(example = "0")
    @SerializedName("transmit_band")
    @JsonProperty("transmit_band")
    private Integer transmitBand;
    
    @ApiModelProperty(example = "0")
    @SerializedName("recv_band")
    @JsonProperty("recv_band")
    private Integer recvBand;
}
