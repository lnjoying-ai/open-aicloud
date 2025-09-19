package com.lnjoying.justice.cluster.manager.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 12/14/21 10:30 AM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class K8sStatusCode
{
    @ApiModelProperty(example = "100")
    @SerializedName("code")
    @JsonProperty("code")
    private  int                 code;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("desc")
    @JsonProperty("desc")
    private Map<String, String> desc;
}
