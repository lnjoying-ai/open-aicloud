package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * update Registry project domain for requestBody
 *
 * @author merak
 **/

@Data
@ApiModel(value = "UpdateRegistryProjectReq")
@JsonIgnoreProperties({"projectId", "bpId", "userId", "userName", "bpName"})
public class UpdateRegistryProjectReq extends BaseReq
{
    /**
     * project id
     */
    @ApiModelProperty(value = "project id")
    private String projectId;

    /**
     * description
     */
    @ApiModelProperty(value = "description")
    @JsonProperty("description")
    private String projectDesc;

}
