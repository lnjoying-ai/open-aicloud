package com.lnjoying.justice.ecrm.domain.dto.request;

import com.lnjoying.justice.ecrm.domain.dto.model.Location;
import lombok.Data;

import java.util.Map;

@Data
public class SiteInputReq
{
	String                   id;
	String                 name;
	String            region_id;
	Map<String, String>  labels;
	Map<String, String>  taints;
	Location           location;
	String          description;
}
