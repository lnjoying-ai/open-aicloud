package com.lnjoying.justice.cluster.agent.common;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/28/22 10:09 PM
 */
public interface TunnelOperName
{
    String CREATE_TUNNEL_REQ   = "create_tunnel_req";
    String CREATE_TUNNEL_RSP   = "create_tunnel_rsp";
    String CLOSE_TUNNEL_REQ    = "close_tunnel_req";
    String CLOSE_TUNNEL_RSP    = "close_tunnel_rsp";
    String FWD_TUNNEL_DATA_REQ = "fwd_tunnel_data_req";
    String FWD_TUNNEL_DATA_RSP = "fwd_tunnel_data_rsp";
    String FWD_DATA_REQ        = "fwd_data_req";
    String FWD_DATA_RSP        = "fwd_data_rsp";
    String FWD_CLOSE_REQ        = "fwd_close_req";
}
