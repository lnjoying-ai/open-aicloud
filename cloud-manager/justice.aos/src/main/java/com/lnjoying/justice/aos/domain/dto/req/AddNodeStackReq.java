package com.lnjoying.justice.aos.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.schema.entity.dev.DevNeedInfo;
import com.lnjoying.justice.schema.entity.dev.SchedulingStrategy;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "AddNodeStackReq")
public class AddNodeStackReq implements Serializable
{
	private static final long serialVersionUID = 519195555443924948L;

	/**
	 * node id
	 */
	@ApiModelProperty(value = "node id")
	@JsonProperty(value = "node_id")
	@NotBlank(message = "nodeId must not blank")
	private String nodeId;

	/**
	 * type
	 */
	@ApiModelProperty(value = "type")
	@JsonProperty(value = "type")
	@NotBlank(message = "type must not blank")
	private String type;
}
