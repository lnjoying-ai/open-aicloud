package com.lnjoying.justice.ecrm.domain.dto.model;

import lombok.Data;

@Data
public class HostNetInfo
{
	String public_ip;
	int  public_port;
	String  inner_ip;
	int   inner_port;
}
