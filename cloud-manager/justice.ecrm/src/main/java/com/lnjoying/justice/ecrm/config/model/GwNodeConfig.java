package com.lnjoying.justice.ecrm.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GwNodeConfig
{
	String imageName;
	int restPort;
	int localPort;
	String cloud;
}
