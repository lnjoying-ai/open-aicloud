package com.lnjoying.justice.cis.common.constant;

public interface CisMsgType
{
	String SCHEDULE_INST = "schedule_inst";
	String RE_SCHEDULE_INST = "re_schedule_inst";

	String ASSIGN_INST    = "assign_inst";
	String RESEND_INST    = "resend_inst";
	String CREATE_NO_RSP  = "create_no_rsp";

	String SPAWN_SYSTEM_STOP  = "spawn_system_stop";
	String SPAWN_USER_STOP_QUIT  = "spawn_user_stop_quit";
	String SPAWN_OVERDUE_QUIT  = "spawn_overdue_quit";
	String SPAWNED_CLOUD_REMOVE  = "spawn_cloud_remove";

	String LOST_STATUS_GT_10M_LT_2H = "lost_status_gt_10m_lt_2h";
	String LOST_STATUS_GT_2H = "lost_status_gt_2h";

	String RESEND_LIFE_MNG    = "resend_life_mng";

	String INST_FAILOVER = "inst_failover";

	String CONFIG_FILE_CREATE = "config_file_create";
}
