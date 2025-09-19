package com.lnjoying.justice.schema.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ConstraintParams
{
	@SerializedName("in_range")
	@JsonProperty("in_range")
	private String inRange;

	@SerializedName("regex")
	@JsonProperty("regex")
	private String regex;

	@SerializedName("valid_values")
	@JsonProperty("valid_values")
	private String validValues;

	@SerializedName("equal")
	@JsonProperty("equal")
	private String equal;

	@SerializedName("greater_or_equal")
	@JsonProperty("greater_or_equal")
	private String greaterOrEqual;

	@SerializedName("greater_than")
	@JsonProperty("greater_than")
	private String greaterThan;

	@SerializedName("less_or_equal")
	@JsonProperty("less_or_equal")
	private String lessOrEqual;

	@SerializedName("less_than")
	@JsonProperty("less_than")
	private String lessThan;
}
