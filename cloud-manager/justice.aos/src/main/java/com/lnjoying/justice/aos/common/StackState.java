package com.lnjoying.justice.aos.common;

public interface StackState
{
    int UNKNOWN                             = -100;       //used by cloud -100~99
    int MAKED                               = 0;
    int SPAWNED                             = 1;
    int WAIT_ASSIGN                         = 2;
    int ASSIGNED                            = 3;
    int FWD                                 = 4;

    int SYNC_CFG                            = 5;
    int CFG_SYNCED                          = 6;

    int EDGE_UNREACHABLE                    = 50;       //cloud fin state 50-99
    int NO_MATCH_NODE                       = 51;

    int SPAWN_SYSTEM_STOP                   = 60;
    int SPAWN_USER_STOP_QUIT                = 61;
    int SPAWN_OVERDUE_QUIT                  = 62;
    int FIN_SYSTEM_STOP                     = 63;
    int FIN_USER_STOP_QUIT                  = 64;
    int FIN_OVERDUE_QUIT                    = 65;

    int CLOUD_CRETAE_FAILED_PARAMS          = 81;
    int CLOUD_CRETAE_FAILED_OVERDUE         = 82;
    int SPAWNED_CLOUD_REMOVE                = 83;
    int FIN_CLOUD_REMOVE                    = 84;

    int CLOUD_SYSTEM_ABNORMAL               = 99;

    int QUEUEING                            = 100;       //used by edge 100~110
    int IMAGE_DOWNLOADING                   = 101;
    int IMAGE_DOWNLOADED                    = 102;
    int CREATED                             = 103;
    int STARTING                            = 104;
    int RUNNING                             = 105;
    int RESTARTING                          = 108;

    int SUCCESS_QUIT                        = 110;
    int USER_STOP_QUIT                      = 111;
    int OVERDUE_QUIT                        = 112;
    int ABNORMAL_QUIT                       = 113;
    int SYSTEM_STOP                         = 120;

    int HARDWARE_ERROR                      = 122;
    int NO_IMAGE                            = 123;
    int IMAGE_DOWNLOAD_FAILED               = 124;
    int CREATE_FAILED                       = 125;

    int REMOVED                             = 130;
    int OBJECT_NOT_EXIST                    = 131;
    int OBJECT_AUTO_REMOVE                  = 132;

    int SYSTEM_ABNORMAL                     = 190;

    int KEEP_ON                             = 200;
}
