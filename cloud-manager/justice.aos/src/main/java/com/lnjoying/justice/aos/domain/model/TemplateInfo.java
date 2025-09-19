package com.lnjoying.justice.aos.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class TemplateInfo
{
	String id;
	String name;
	String version;
	String description;
	String logo_url;
	String vendor;
	Boolean is_used;
	String aos_type;
	String content_type;
	String stack_compose;
	String justice_compose;
	List<TemplateInputParams> show_inputs;
	String create_time;
	String update_time;
	List<String> labels;
}
