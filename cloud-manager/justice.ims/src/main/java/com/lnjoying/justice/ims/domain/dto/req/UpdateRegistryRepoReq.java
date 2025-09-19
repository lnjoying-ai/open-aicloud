package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * update Registry repo domain for requestBody
 *
 * @author merak
 **/

@Data
@ApiModel(value = "UpdateRegistryRepoReq")
@JsonIgnoreProperties({"registryId", "projectId", "repoName", "bpId", "userId", "userName", "bpName"})
public class UpdateRegistryRepoReq extends BaseReq
{
    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    /**
     * project id
     */
    @ApiModelProperty(value = "project id")
    private String projectId;

    /**
     * repo name
     */
    @JsonProperty(value = "repo_name")
    @ApiModelProperty(value = "repo name")
    private String repoName;

    @ApiModelProperty(value = "label list")
    @JsonProperty("label")
    private List<String> labels;

    @ApiModelProperty(value = "description")
    private String description;
}
