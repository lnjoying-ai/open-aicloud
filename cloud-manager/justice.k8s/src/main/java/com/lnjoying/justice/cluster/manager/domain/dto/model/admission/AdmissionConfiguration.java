package com.lnjoying.justice.cluster.manager.domain.dto.model.admission;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.TypeMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/15/21 10:52 AM
 * @Description:
 */
@Data
public class AdmissionConfiguration implements Serializable
{
    @ApiModelProperty(example = "{}")
    @SerializedName("type_meta")
    @JsonProperty("type_meta")
    private TypeMeta typeMeta;
    
    @ApiModelProperty(example = "[]")
    @SerializedName("plugins")
    @JsonProperty("plugins")
    private List<AdmissionPluginConfiguration > plugins;
}
