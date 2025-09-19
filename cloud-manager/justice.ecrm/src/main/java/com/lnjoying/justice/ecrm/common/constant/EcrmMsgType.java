package com.lnjoying.justice.ecrm.common.constant;

public interface EcrmMsgType
{
	String CONFIG_NODE    = "config_edge";
	String ACTIVE_EDGE    = "active_edge";
	String DEACTIVE_EDGE  = "deactive_edge";
	String REMOVE_EDGE    = "remove_edge";
	String EVACUATE_EDGE  = "evacuate_edge";
	String REBOOT_EDGE    = "reboot_edge";

	String CONFIG_GW      = "config_gw";
	String ACTIVE_GW      = "active_gw";
	String DEACTIVE_GW    = "deactive_gw";
	String REMOVE_GW      = "remove_gw";
	String REBOOT_GW      = "reboot_gw";

	String SET_REGION     = "set_region";
	String RM_REGION      = "rm_region";
	String REG_GW         = "reg_gw";
	String LOGIN_GW       = "login_gw";
	String GET_GW_LIST    = "get_gw_list";
	String SYNC_EDGE_IF_STATE  = "sync_edge_if_state";

	String GET_EDGE_REQ    = "get_edge_req";

	String SYNC_WORKER_IF_STATE  = "sync_worker_if_state";
	String CREATE_SERVICE_AGENT  = "create_service_agent";
}