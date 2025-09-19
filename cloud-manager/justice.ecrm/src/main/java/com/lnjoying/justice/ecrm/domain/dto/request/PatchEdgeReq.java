package com.lnjoying.justice.ecrm.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.ecrm.domain.dto.model.ResourceLimit;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchEdgeReq
{
	@ApiModelProperty(example = "")
	@SerializedName("labels")
	@JsonProperty("labels")
	Map<String, String>   labels;
}
