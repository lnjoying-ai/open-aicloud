package com.lnjoying.justice.aos.domain.model;

import com.lnjoying.justice.schema.entity.ConstraintParams;
import lombok.Data;

import java.util.List;

@Data
public class TemplateInputParams
{
	String variable;
	String description;
	String label;
	String type;
	boolean required = false;
	Object default_value;
	List<Object> options;
	boolean immutable;
	ConstraintParams constraints;
}
