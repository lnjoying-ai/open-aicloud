package com.lnjoying.justice.ecrm.common.constant;

public interface UpgradeStatus
{
	int PLANED = 1;
	int UPGRADING = 2;
	int AUTO_CONFIRM = 3;
	int MANUAL_CONFIRM = 4;
	int ROLLBACK = 5;
	int CLEAN = 6;

	int UPGRADE_FAILED = 20;
	int UPGRADE_COMPLETED = 40;
}
