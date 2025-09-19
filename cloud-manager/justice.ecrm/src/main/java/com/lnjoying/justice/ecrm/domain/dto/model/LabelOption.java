package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.ConstraintParams;
import lombok.Data;

import java.util.List;

@Data
public class LabelOption
{
	@SerializedName("key")
	@JsonProperty("key")
	private String key;

	@SerializedName("description")
	@JsonProperty("description")
	private String description;

	@SerializedName("show_name")
	@JsonProperty("show_name")
	private String showName;

	@SerializedName("type")
	@JsonProperty("type")
	private String type;

	@SerializedName("required")
	@JsonProperty("required")
	private Boolean	required;

	@SerializedName("default_value")
	@JsonProperty("default_value")
	private Object defaultValue;

	@SerializedName("options")
	@JsonProperty("options")
	private List<Object> options;

	@SerializedName("immutable")
	@JsonProperty("immutable")
	private Boolean immutable;

	@SerializedName("constraints")
	@JsonProperty("constraints")
	private ConstraintParams constraints;
}
