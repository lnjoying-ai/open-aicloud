package com.lnjoying.justice.schema.entity.edgeif;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class EdgeWorkerIfStateInfos implements Serializable
{
	String   workerId;
	String regionId;
	Map<String, EdgeWorkerIfState> edgeWorkerIfStateMap;
}
