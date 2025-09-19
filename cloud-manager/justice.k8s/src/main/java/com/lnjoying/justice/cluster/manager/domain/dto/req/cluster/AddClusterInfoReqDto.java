/**
 * desc add cluster req info
 */
package com.lnjoying.justice.cluster.manager.domain.dto.req.cluster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.dev.K8sDevNeed;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.ClusterBaseInfo;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;


@Data
@ApiModel(value = "AddClusterReq")
@JsonIgnoreProperties({"id"})
public class AddClusterInfoReqDto extends ClusterBaseInfo
{
    @Valid
    @ApiModelProperty(required = true, example = "[{},{}]")
    @SerializedName("dev_needs")
    @JsonProperty("dev_needs")
    private List<K8sDevNeed>              devNeeds;
    
    @ApiModelProperty(required = true, example = "[{},{}]")
    @SerializedName("target_nodes")
    @JsonProperty("target_nodes")
    private List<TargetNode>              targetNodes;
}
