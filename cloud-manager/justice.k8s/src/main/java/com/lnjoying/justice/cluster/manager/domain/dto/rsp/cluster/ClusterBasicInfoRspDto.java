package com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.config.ServiceConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "GetClusterBasicInfoRsp")
@Slf4j
public class ClusterBasicInfoRspDto
{
    @ApiModelProperty(required = true, example = "cluster_id")
    @SerializedName("id")
    @JsonProperty("id")
    private String id;

    @ApiModelProperty(required = true, example = "cluster-name")
    @Pattern(regexp = ServiceConfig.PATTERN_NAME)
    @SerializedName("name")
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(example = "{}")
    @SerializedName("dashboard_url")
    @JsonProperty("dashboard_url")
    private String dashboardUrl;
}
