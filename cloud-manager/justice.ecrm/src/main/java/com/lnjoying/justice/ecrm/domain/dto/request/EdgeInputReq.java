package com.lnjoying.justice.ecrm.domain.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Map;

@Data
public class EdgeInputReq
{
	String           region_id;
	String             site_id;
	@NotBlank(message = "name can not be empty")
	@Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9_@-]{0,63}$")
	String           node_name;
	String           rest_bind;
	int              rest_port;
	Map<String, String> labels;
	Map<String, String> taints;
	String              vendor;
}
