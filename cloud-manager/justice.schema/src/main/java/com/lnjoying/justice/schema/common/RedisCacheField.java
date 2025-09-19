package com.lnjoying.justice.schema.common;

public interface RedisCacheField
{
    ////////////////////main key////////////////////
    String AUTH_VER_CODE                   = "auth_verification_code";
    String PATCH_VER_CODE                  = "patch_verification_code";

    String REG_VER_CODE                    =   "reg_verification_code";
    String ACCESS_TOKEN_EXPIRE             =     "access_token_expire";
    String INVITATION_CODE                 =         "invitation_code";

    String REGION                          =                  "REGION";
    String SITE                            =                    "SITE";

    String SESSION                         =                 "SESSION";
    String LIFE                            =                    "LIFE";
    String TOKEN                           =                   "TOKEN";
    String EVENT                           =                   "EVENT";
    String RESET_PWD_MAX_ATTEMPT           =   "RESET_PWD_MAX_ATTEMPT:";
    String RESET_PWD_ERROR_TIMES           =   "RESET_PWD_ERROR_TIMES:";
    String RESET_PWD_LOCK_SEC              =      "RESET_PWD_LOCK_SEC:";
    String SYSTEM_INFO                     =             "SYSTEM_INFO";
    ////////////////////stat////////////////////
    String STAT                            =                    "STAT";
    String NODE                            =                    "NODE";

    ////////////////////scheduler////////////////////
    String SCHED                           =                   "SCHED";
    String EDGE                            =                    "EDGE";
    String TEMP                            =                    "TEMP";
    String RESOURCES                       =               "RESOURCES";
    String ONLINE                          =                  "ONLINE";
    String OFFLINE                         =                 "OFFLINE";
    String LABEL                           =                   "LABEL";
    String TAINT                           =                   "TAINT";

    ////////////////////ecrm////////////////////
    String ECRM                            =                    "ECRM";
    String AGENT                           =                   "AGENT";
    String GW                              =                      "GW";
    String IMAGE                           =                   "IMAGE";
    String CLOUD                           =                   "CLOUD";
    String RESTPORT                        =                "RESTPORT";
    String LOCALPORT                       =               "LOCALPORT";
    String INSTALLFILE                     =             "INSTALLFILE";
    String EHOSTOPERATOR                   =           "EHOSTOPERATOR";
    String OPTION                          =                  "OPTION";
    String UPGRADE                         =                 "UPGRADE";
    String HOSTCONFIG                      =              "HOSTCONFIG";

    ////////////////////api////////////////////
    String API                             =                     "API";
    String IF                              =                      "IF";

    ////////////////////cls////////////////////
    String CLS                             =                     "CLS";
    String WORKER                          =                  "WORKER";

    ////////////////////cis////////////////////
    String CIS                             =                     "CIS";
    String KEEPALIVE                       =               "KEEPALIVE";
    String SPEC                            =                    "SPEC";

    ////////////////////aos////////////////////
    String AOS                             =                     "AOS";

    ////////////////////cmp////////////////////
    String CMP                             =                     "CMP";
    String HEALTH                          =                  "HEALTH";
    String SYNC                            =                    "SYNC";

    ////////////////////servicemanager////////////////////
    String SERVICEMANAGER                  =          "SERVICEMANAGER";
    String SERVICEGW                       =               "SERVICEGW";

    ////////////////////omc////////////////////
    String OMC                             =                     "OMC";
    String API_LOG_EXPIRE_REMOVE_LOCK      =   "API_LOG_EXPIRE_REMOVE";
    String API_LOG_LAST_CLEAN_DATE         =    "API_LOG_LAST_CLEAN_DATE";
    String EVENT_LOG_EXPIRE_REMOVE_LOCK="EVENT_LOG_EXPIRE_REMOVE_LOCK";
    String EVENT_LOG_LAST_CLEAN_DATE       =    "EVENT_LOG_LAST_CLEAN_DATE";

    //key pattern                              description                   set               use         delete
    //----------------------------------------------------ONLINE--------------------------------------------------------
    //EDGE:ONLINE                              all online edge set.          api\ecrm          shced
    //EDGE:ONLINE:REGION:{region_id}           region's online edge set.     api\ecrm          shced       ecrm
    //EDGE:ONLINE:SITE:{site_id}               site's online edge set.       api\ecrm          shced       ecrm
    //-----------------------------------------------------LABEL--------------------------------------------------------
    //LABEL:REGION:{label_key}:{label_value}   label region set.             ecrm              shced       ecrm
    //LABEL:REGION:{label_key}                 label region set.             ecrm              shced       ecrm
    //LABEL:SITE:{label_key}:{label_value}     label site set.               ecrm              shced       ecrm
    //LABEL:SITE:{label_key}                   label site set.               ecrm              shced       ecrm
    //LABEL:EDGE:{label_key}:{label_value}     label edge set.               ecrm              shced       ecrm
    //LABEL:EDGE:{label_key}                   label edge set.               ecrm              shced       ecrm
    //LABEL:REGION                             region label set.             ecrm              shced
    //LABEL:SITE                               site label set.               ecrm              shced
    //LABEL:EDGE                               edge label set.               ecrm              shced
    //----------------------------------------------------SCHED---------------------------------------------------------
    //SCHED:EDGE:RESOURCES:{node_id}           edge remain resources.        shced             shced       shced
    //SCHED:REGION                             all region set.               shced\ecrm        shced
    //SCHED:SITE                               all site set.                 shced\ecrm        shced
    //SCHED:SITE:REGION:{region_id}            region's site set.            shced\ecrm        shced       ecrm
    //SCHED:TEMP:REGION:{ref_id}               shceduler middle result.      shced             shced       shced
    //SCHED:TEMP:SITE:{ref_id}                 shceduler middle result.      shced             shced       shced
    //SCHED:TEMP:EDGE:{ref_id}                 shceduler middle result.      shced             shced       shced
    //SCHED:TEMP:{ref_id}                      shceduler middle result.      shced             shced       shced
    //-----------------------------------------------------ECRM---------------------------------------------------------
    //ECRM:INSTALLFILE                         install shell file name.      manual            ecrm
    //ECRM:AGENT:IMAGE                         edge agent image name.        manual            ecrm
    //ECRM:AGENT:RESTPORT                      edge agent rest port.         manual            ecrm
    //ECRM:GW:IMAGE                            edge gw image name.           manual            ecrm
    //ECRM:GW:RESTPORT                         edge gw rest port.            manual            ecrm
    //ECRM:GW:LOCALPORT                        edge gw local port.           manual            ecrm
    //ECRM:GW:CLOUD                            edge gw cloud url.            manual            ecrm
    //ECRM:SITE:{region_id}:{site_id}:{gw_id}                                api               ecrm
    //ECRM:EDGE:IF:{node_id}                   edge if.
    //ECRM:GW:IF:{gw_id}                       gw if.
    //ECRM:EHOSTOPERATOR:SESSION:{session_id}  ehost_operator session.       ecrm              ecrm         auto
    //ECRM:LABEL:OPTION:{label_type}           label option.                 ecrm              ecrm         auto
    //ECRM:TAINT:OPTION:{taint_type}           taint option.                 ecrm              ecrm         auto
    //ECRM:UPGRADE                             upgrade node id set.
    //ECRM:HOSTCONFIG                          host config session set.
    //ECRM:HOSTCONFIG:SESSION:{session_id}     host config session detail.
    //ECRM:HOSTCONFIG:NODE:{node_id}           host config node detail.
    //ECRM:HOSTCONFIG:OFFLINE                  host config offline node set.
    //ECRM:HOSTCONFIG:NODE:SESSION:{node_id}   host config latest session.
    //ECRM:WORKER:IF:{worker_id}               worker if.
    //ECRM:REGION:{region_id}:{gw_id}
    //-----------------------------------------------------CIS----------------------------------------------------------
    //CIS:LIFE:EVENT                           cis life mng event set.
    //CIS:LIFE:EVENT:{session_id}              cis life mng event.
    //CIS:KEEPALIVE:SPEC                       keepalive spec id set
    //CIS:KEEPALIVE:{spec_id}                  keepalive inst id set
    //-----------------------------------------------------AOS----------------------------------------------------------
    //AOS:KEEPALIVE:SPEC                       keepalive spec id set
    //AOS:KEEPALIVE:{spec_id}                  keepalive stack id set
    //-----------------------------------------------------API----------------------------------------------------------
    //AOS:LIFE:EVENT                           aos life mng event set.
    //AOS:LIFE:EVENT:{session_id}              aos life mng event.
    //-----------------------------------------------------CLS----------------------------------------------------------
    //CLS:WORKER:IF:{worker_id}                worker if.
    //CLS:REGION:{region_id}:{gw_id}
    //-----------------------------------------------SERVICEMANAGER-----------------------------------------------------
    //SERVICEMANAGER:SERVICEGW                 service gw map json.
    //----------------------------------------------------OTHER---------------------------------------------------------
}
