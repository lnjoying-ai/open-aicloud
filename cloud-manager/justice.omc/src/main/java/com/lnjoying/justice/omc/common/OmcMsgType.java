package com.lnjoying.justice.omc.common;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/30 19:11
 */

public interface OmcMsgType
{
    String BUILD_STACK = "build_stack";

    String CHECK_STACK_RUNNING_STATUS = "check_stack_running_status";

    String PORT_MAPPING = "port_mapping";

    String ADD_EXPORTER_SCRAPE_TARGET = "add_exporter_scrape_target";

    String DELETE_EXPORTER_SCRAPE_TARGET = "delete_exporter_scrape_target";

    String REMOVE_PORT_MAPPING = "remove_port_mapping";

    String REMOVE_STACK = "remove_stack";

    String SEND_ALERT = "send_alert";
}
