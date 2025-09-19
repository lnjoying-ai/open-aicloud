package com.lnjoying.justice.aos.common;

public interface DaemonSetDeployType
{
	//每个站点内只部署一个
	Integer ONE_IN_SITE     = 0;
	//站点内所有节点均部署
	Integer  ANY_IN_SITE     = 1;
}
