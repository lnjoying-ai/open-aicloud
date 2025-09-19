package com.lnjoying.justice.aos.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class JustisBasic
{
	String name;
	String version;
	String description;
	String uuid;
	String minimum_justice_version;
	List<TemplateInputParams> input_params;
}
