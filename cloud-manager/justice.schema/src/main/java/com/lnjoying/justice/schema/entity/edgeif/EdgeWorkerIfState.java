package com.lnjoying.justice.schema.entity.edgeif;

import lombok.Data;

import java.io.Serializable;

@Data
public class EdgeWorkerIfState implements Serializable
{
	int      status;
	String gwNodeId;
	long  loginTime;
	long   lastTime;
}
