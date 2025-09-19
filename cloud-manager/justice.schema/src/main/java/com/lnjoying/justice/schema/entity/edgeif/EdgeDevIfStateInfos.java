package com.lnjoying.justice.schema.entity.edgeif;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EdgeDevIfStateInfos implements Serializable
{
//	int      status;
	String   nodeId;
	String regionId;
	Map<String, EdgeDevIfState> edgeDevIfStateMap;
}
