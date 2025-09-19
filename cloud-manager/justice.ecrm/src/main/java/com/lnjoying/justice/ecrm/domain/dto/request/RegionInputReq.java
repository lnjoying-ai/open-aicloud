package com.lnjoying.justice.ecrm.domain.dto.request;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class RegionInputReq
{
	String                  id;
	String                name;
	Map<String, String> labels;
	Map<String, String> taints;
	String         description;
	List<String>   gw_node_ids;
}
