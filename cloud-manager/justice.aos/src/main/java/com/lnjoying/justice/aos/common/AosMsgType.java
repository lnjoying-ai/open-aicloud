package com.lnjoying.justice.aos.common;

public interface AosMsgType
{
	String SCHEDULE_STACK      = "schedule_stack";
	String SCHEDULE_DAEMONSET_STACK      = "schedule_daemonset_stack";
	String RE_SCHEDULE_STACK   = "re_schedule_stack";

	String SYNC_CFG            = "sync_cfg";
	String ASSIGN_STACK        = "assign_stack";
	String RESEND_STACK        = "resend_stack";
	String NO_RSP              = "no_rsp";
	String START_STACK         = "start_stack";
	String STOP_STACK          = "stop_stack";
	String DELETE_STACK        = "delete_stack";
	String RESTART_STACK       = "restart_stack";

	String STOP_SERVICE        = "stop_service";
	String START_SERVICE       = "start_service";
	String DELETE_SERVICE      = "delete_service";
	String RESTART_SERVICE     = "restart_service";

	String SPAWN_SYSTEM_STOP   = "spawn_system_stop";
	String SPAWN_OVERDUE_QUIT  = "spawn_overdue_quit";

	String LOST_STATUS_GT_10M_LT_2H = "lost_status_gt_10m_lt_2h";
	String LOST_STATUS_GT_2H = "lost_status_gt_2h";

	String RESEND_LIFE_MNG    = "resend_life_mng";

	String STACK_FAILOVER = "stack_failover";
}
