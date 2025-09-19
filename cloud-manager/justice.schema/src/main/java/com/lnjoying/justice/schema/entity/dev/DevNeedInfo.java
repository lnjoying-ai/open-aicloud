package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevNeedInfo implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("cpu")
    @JsonProperty("cpu")
    private CpuNeed cpu;
    
    @ApiModelProperty(example = "")
    @SerializedName("mem")
    @JsonProperty("mem")
    private MemNeed mem;
    
    @ApiModelProperty(example = "")
    @SerializedName("disk")
    @JsonProperty("disk")
    private DiskNeed disk;
    
    @ApiModelProperty(example = "")
    @SerializedName("network_band_need")
    @JsonProperty("network_band_need")
    private NetworkBandNeed networkBandNeed;
    
    @ApiModelProperty(example = "")
    @SerializedName("gpu")
    @JsonProperty("gpu")
    private GpuNeed gpu;
}
