package com.lnjoying.justice.schema.msg;

public interface MessageName
{
    String  HEART_BEAT_REQ      = "heartbeat_req";
    String  HEART_BEAT_RSP      = "heartbeat_rsp";
    String  EDGE_LOGIN_GW_REQ   = "edge_login_gw_req";
    String  EDGE_LOGIN_GW_RSP   = "edge_login_gw_rsp";
    String  GW_LOGIN_GW_REQ     = "gw_login_gw_req";
    String  GW_LOGIN_GW_RSP     = "gw_login_gw_rsp";
    String  GW_LOGIN_CLOUD_REQ  = "gw_login_cloud_req";
    String  GW_LOGIN_CLOUD_RSP  = "gw_login_cloud_rsp";
    String  EDGE_REG_REQ        = "edge_reg_req";
    String  EDGE_REG_RSP        = "edge_reg_rsp";
    String  GW_REG_REQ          = "gw_reg_req";
    String  GW_REG_RSP          = "gw_reg_rsp";
    String  REG_ABILITY_REQ     = "reg_ability_req";
    String  REG_ABILITY_RSP     = "reg_ability_rsp";
    String  EXCHANGE_GWS_REQ    = "exchange_gws_req";
    String  EHOST_OPERATOR      = "ehost_operator";
    String  GHOST_OPERATOR      = "ghost_operator";
    String  INST_OPERATOR       = "inst_operator";
    String  CLOUD_CTRL          = "cloud_ctrl";
    String  EXANGE_EDGE_REQ     = "exchange_edge_req";
    String  STACK_OPERATOR      = "stack_operator";
    String  EDGE_STAT_OPERATOR  = "edge_stat_operator";
    String  GET_EDGE_REQ        = "get_edge_req";
    String  GET_EDGE_RSP        = "get_edge_rsp";
    String  IMAGE_OPERATOR      = "image_operator";
    String  TUNNEL_OPERATOR     = "tunnel_operator";
    String  WORKER_REG_REQ      = "worker_reg_req";
    String  WORKER_REG_RSP      = "worker_reg_rsp";
    String  WORKER_LOGIN_REQ    = "worker_login_req";
    String  WORKER_LOGIN_RSP    = "worker_login_rsp";
    String  FILE_CREATE_REQ     = "file_create_req";
    String  FILE_CREATE_RSP     = "file_create_rsp";
    String  FILE_OPERATOR       = "file_operator";
    String  FWD_OPERATOR        = "fwd_operator";
}