package com.lnjoying.justice.ecrm.domain.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@Data
public class GwInputReq
{
	List<String>    region_ids;
	@NotBlank(message = "name can not be empty")
	@Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9_@-]{0,63}$")
	String           node_name;
	String           rest_bind;
	int              rest_port;
	String           advertise;
	String        service_bind;
	int             local_port;
	Map<String,String>  labels;

}
