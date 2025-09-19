package com.lnjoying.justice.schema.msg;

public interface CtrlType
{
    String SYNC_REGION_CONNECTED_DEV_REQ       = "sync_region_connected_dev_req";
    String SYNC_EDGE_IF_STATE_REQ              = "sync_edge_if_state_req";
    String SYNC_GW_REQ                         = "sync_gw_req";
    String SET_REGION_INFO_REQ                 = "set_region_info_req";
    String GET_GW_LIST_REQ                     = "get_gw_list_req";//无消息体
    String GET_GW_LIST_RSP                     = "get_gw_list_rsp";
    String SYNC_WORKER_IF_STATE_REQ            = "sync_worker_if_state_req";
    String SYNC_REGION_CONNECTED_WORKER_REQ    = "sync_region_connected_worker_req";
}
