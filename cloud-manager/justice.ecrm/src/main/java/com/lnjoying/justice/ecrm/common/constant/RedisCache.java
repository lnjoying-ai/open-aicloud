package com.lnjoying.justice.ecrm.common.constant;

import com.lnjoying.justice.schema.common.RedisCacheField;

public interface RedisCache
{
    //session expire time: 30 min
    Integer EHOST_OPERATOR_SESSION_DURATION = 1*30*60;
    //label option expire time: 60 min
    Integer LABEL_OPTION_DURATION = 1*60*60;


    //EDGE:ONLINE                              all online edge set.
    String ALL_ONLINE_EDGE = RedisCacheField.EDGE + ":" + RedisCacheField.ONLINE;
    //EDGE:ONLINE:REGION:{region_id}           region's online edge set.
    String REGION_ONLINE_EDGE = RedisCacheField.EDGE + ":" + RedisCacheField.ONLINE + ":" + RedisCacheField.REGION + ":";
    //EDGE:ONLINE:SITE:{site_id}               site's online edge set.
    String SITE_ONLINE_EDGE = RedisCacheField.EDGE + ":" + RedisCacheField.ONLINE + ":" + RedisCacheField.SITE + ":";

    //LABEL:REGION                             region label set.
    String LABEL_REGION_SET = RedisCacheField.LABEL + ":" + RedisCacheField.REGION;
    //LABEL:REGION:{label_key}:{label_value}   label region set.
    //LABEL:REGION:{label_key}                 label region set.
    String LABEL_REGION = RedisCacheField.LABEL + ":" + RedisCacheField.REGION + ":";
    //LABEL:SITE                               site label set.
    String LABEL_SITE_SET = RedisCacheField.LABEL + ":" + RedisCacheField.SITE;
    //LABEL:SITE:{label_key}:{label_value}     label site set.
    //LABEL:SITE:{label_key}                   label site set.
    String LABEL_SITE = RedisCacheField.LABEL + ":" + RedisCacheField.SITE + ":";
    //LABEL:EDGE                               edge label set.
    String LABEL_EDGE_SET = RedisCacheField.LABEL + ":" + RedisCacheField.EDGE;
    //LABEL:EDGE:{label_key}:{label_value}     label edge set.
    //LABEL:EDGE:{label_key}                   label edge set.
    String LABEL_EDGE = RedisCacheField.LABEL + ":" + RedisCacheField.EDGE + ":";

    //ECRM:INSTALLFILE                         install shell file name.
    String INSTALL_FILE = RedisCacheField.ECRM + ":" + RedisCacheField.INSTALLFILE;
    //ECRM:AGENT:IMAGE                         edge agent image name.
    String AGENT_IMAGE = RedisCacheField.ECRM + ":" + RedisCacheField.AGENT + ":" + RedisCacheField.IMAGE;
    //ECRM:AGENT:RESTPORT                      edge agent rest port.
    String AGENT_REST_PORT = RedisCacheField.ECRM + ":" + RedisCacheField.AGENT + ":" + RedisCacheField.RESTPORT;
    //ECRM:GW:IMAGE                            edge gw image name.
    String GW_IMAGE = RedisCacheField.ECRM + ":" + RedisCacheField.GW + ":" + RedisCacheField.IMAGE;
    //ECRM:GW:RESTPORT                         edge gw rest port.
    String GW_REST_PORT = RedisCacheField.ECRM + ":" + RedisCacheField.GW + ":" + RedisCacheField.RESTPORT;
    //ECRM:GW:LOCALPORT                        edge gw local port.
    String GW_LOCAL_PORT = RedisCacheField.ECRM + ":" + RedisCacheField.GW + ":" + RedisCacheField.LOCALPORT;
    //ECRM:GW:CLOUD                            edge gw cloud url.
    String CLOUD_URL = RedisCacheField.ECRM + ":" + RedisCacheField.GW + ":" + RedisCacheField.CLOUD;
    //ECRM:EDGE:IF:{node_id}                    edge if.
    String EDGE_IF = RedisCacheField.ECRM + ":" + RedisCacheField.EDGE + ":" + RedisCacheField.IF + ":";
    //ECRM:GW:IF:{gw_id}                        gw if.
    String GW_IF = RedisCacheField.ECRM + ":" + RedisCacheField.GW + ":" + RedisCacheField.IF + ":";
    //ECRM:SITE:{region_id}:{site_id}:{gw_id}
    String SITE_GW_EDGE = RedisCacheField.ECRM + ":" + RedisCacheField.SITE + ":";

    //ECRM:EHOSTOPERATOR:SESSION:{session_id}  ehost_operator session.
    String EHOST_OPERATOR_SESSION = RedisCacheField.ECRM + ":" + RedisCacheField.EHOSTOPERATOR + ":" + RedisCacheField.SESSION + ":";


    //SCHED:REGION                             all region set.
    String SCHED_ALL_REGION = RedisCacheField.SCHED + ":" + RedisCacheField.REGION;
    //SCHED:SITE:REGION:{region_id}            region's site set.
    String SCHED_REGION_SITE = RedisCacheField.SCHED + ":" + RedisCacheField.SITE + ":" + RedisCacheField.REGION + ":";
    //SCHED:SITE                               all region set.
    String SCHED_ALL_SITE = RedisCacheField.SCHED + ":" + RedisCacheField.SITE;


    //ECRM:LABEL:OPTION:{label_type}           label option.
    String LABEL_OPTION = RedisCacheField.ECRM + ":" + RedisCacheField.LABEL + ":" + RedisCacheField.OPTION + ":";
    //ECRM:TAINT:OPTION:{taint_type}           taint option.
    String TAINT_OPTION = RedisCacheField.ECRM + ":" + RedisCacheField.TAINT + ":" + RedisCacheField.OPTION + ":";

    //ECRM:UPGRADE                             upgrade node id set.
    String UPGRADE_NODEID_SET = RedisCacheField.ECRM + ":" + RedisCacheField.UPGRADE;

    //ECRM:HOSTCONFIG                          host config session set.
    String HOST_CONFIG_SET = RedisCacheField.ECRM + ":" + RedisCacheField.HOSTCONFIG;
    //ECRM:HOSTCONFIG:SESSION:{session_id}     host config session detail.
    String HOST_CONFIG_SESSION = RedisCacheField.ECRM + ":" + RedisCacheField.HOSTCONFIG + ":" + RedisCacheField.SESSION + ":";
    //ECRM:HOSTCONFIG:NODE:{node_id}           host config node detail.
    String HOST_CONFIG_NODE = RedisCacheField.ECRM + ":" + RedisCacheField.HOSTCONFIG + ":" + RedisCacheField.NODE + ":";
    //ECRM:HOSTCONFIG:NODE:SESSION:{node_id}   host config latest session.
    String HOST_CONFIG_NODE_SESSION = RedisCacheField.ECRM + ":" + RedisCacheField.HOSTCONFIG + ":" + RedisCacheField.NODE + ":" + RedisCacheField.SESSION + ":";
    //ECRM:HOSTCONFIG:OFFLINE                  host config offline node set.
    String HOST_CONFIG_OFFLINE = RedisCacheField.ECRM + ":" + RedisCacheField.HOSTCONFIG + ":" + RedisCacheField.OFFLINE + ":";

    //ECRM:WORKER:IF:{worker_id}                  worker if.
    String WORKER_IF = RedisCacheField.ECRM + ":" + RedisCacheField.WORKER + ":" + RedisCacheField.IF + ":";
    //ECRM:REGION:{region_id}:{gw_id}
    String REGION_GW_WORKER = RedisCacheField.ECRM + ":" + RedisCacheField.REGION + ":";
}
