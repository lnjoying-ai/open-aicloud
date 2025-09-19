package com.lnjoying.justice.aos.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/9 15:40
 */

@Data
@ApiModel(value = "InstallStackReq")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InstallStackReq implements Serializable
{

    private static final long serialVersionUID = 4047951373364982808L;

    private String name;

    private String description;

    /**
     * app id
     */
    @ApiModelProperty(value="app id")
    private String appId;

    /**
     * chart version
     */
    @JsonProperty("version")
    @ApiModelProperty(value="chart version")
    private String chartVersion;

    private String aosType;

    private String contentType;

    /**
     * chart config
     */
    @ApiModelProperty(value="chart config")
    private String stackCompose;

    private Map<String, String> labels;

    private String userId;

    private String bpId;

    private String userName;

    private String bpName;

    private List<ClusterInfo> targetClusters;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public final static class ClusterInfo
    {
        private String clusterId;

        private String clusterName;

        private String namespace;
    }

}
