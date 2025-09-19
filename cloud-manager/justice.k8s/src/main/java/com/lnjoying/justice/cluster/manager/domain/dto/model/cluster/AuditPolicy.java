package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/9/21 4:26 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditPolicy implements Serializable
{
    @ApiModelProperty(example = "{}")
    @SerializedName("type_meta")
    @JsonProperty("type_meta")
    private TypeMeta           typeMeta;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("metadata")
    @JsonProperty("metadata")
    private ObjectMeta         metadata;
    
    @ApiModelProperty(example = "[]")
    @SerializedName("rules")
    @JsonProperty("rules")
    private List<AuditPolicyRule> rules;
    
    @ApiModelProperty(example = "[]")
    @SerializedName("omit_stages")
    @JsonProperty("omit_stages")
    private List<String>     omitStages;
}
