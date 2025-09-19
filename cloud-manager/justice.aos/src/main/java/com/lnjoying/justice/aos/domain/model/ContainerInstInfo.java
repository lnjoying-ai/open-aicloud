package com.lnjoying.justice.aos.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ContainerInstInfo
{
	String name;
	String container_id;
	@JsonProperty("id")
	String inst_id;
	String command;
	String image_name;
	String ports;         //dict convert to string
	String node_id;
	String node_name;
	StatusCode status;

	@Data
	@ApiModel(value = "ContainerInstStatusCode")
	public static final class StatusCode{
		private int code;
		private Map<String, String> desc;
	}
}
