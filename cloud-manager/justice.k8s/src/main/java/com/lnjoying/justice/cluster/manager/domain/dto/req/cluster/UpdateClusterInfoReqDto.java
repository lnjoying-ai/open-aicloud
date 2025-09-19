/**
 * desc add cluster req info
 */
package com.lnjoying.justice.cluster.manager.domain.dto.req.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.dev.K8sDevNeed;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "UpdateClusterReq")
public class UpdateClusterInfoReqDto
{
    @ApiModelProperty(example = "")
    @SerializedName("description")
    @JsonProperty("description")
    private  String                description;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("dev_need")
    @JsonProperty("dev_need")
    private K8sDevNeed             devNeed;
}
