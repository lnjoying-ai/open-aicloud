package com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.ClusterNodeInfoDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/14/21 10:27 AM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterNodesRspDto
{
    @ApiModelProperty(required = true, example = "10")
    @JsonProperty("total_num")
    private long totalNum  = 0;
    
    @ApiModelProperty(required = false, example = "{}")
    @JsonProperty("nodes")
    private List<ClusterNodeInfoDto> nodes;
}
