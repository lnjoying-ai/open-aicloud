package com.lnjoying.justice.schema.entity.edgeif;

import lombok.Data;

import java.io.Serializable;

@Data
public class EdgeGwIfState implements Serializable
{
	int      status;
	String   nodeId;
	String advertise;
	long  loginTime;
	long   lastTime;
}
