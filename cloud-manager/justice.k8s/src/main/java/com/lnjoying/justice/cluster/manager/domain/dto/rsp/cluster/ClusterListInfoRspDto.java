package com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@ApiModel(value = "GetClusterListRsp")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterListInfoRspDto
{
    @ApiModelProperty(required = true, example = "10")
    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long                 totalNum;
    
    @ApiModelProperty(example = "[{\"id\":\"111222\",...}]")
    @SerializedName("clusters")
    @JsonProperty("clusters")
    private List<ClusterInfoRspDto> clusters;
}
