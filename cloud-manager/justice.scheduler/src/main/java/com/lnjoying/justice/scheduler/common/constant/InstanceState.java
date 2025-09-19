package com.lnjoying.justice.scheduler.common.constant;

public class InstanceState
{
    private InstanceState() {}

    public static final int UNKNOWN                             = -100;
    public static final int MAKED                               = 0;
    public static final int SPAWNED                             = 1;
    public static final int WAIT_ASSIGN                         = 2;
    public static final int ASSIGNED                            = 3;
    public static final int FWD                                 = 4;

    public static final int EDGE_UNREACHABLE                    = 50;
    public static final int NO_MATCH_NODE                       = 51;

    public static final int SPAWN_SYSTEM_STOP                   = 60;
    public static final int SPAWN_USER_STOP_QUIT                = 61;
    public static final int SPAWN_OVERDUE_QUIT                  = 62;
    public static final int FIN_SYSTEM_STOP                     = 63;
    public static final int FIN_USER_STOP_QUIT                  = 64;
    public static final int FIN_OVERDUE_QUIT                    = 65;

    public static final int CLOUD_CRETAE_FAILED_PARAMS          = 81;
    public static final int CLOUD_CRETAE_FAILED_OVERDUE         = 82;
    public static final int SPAWNED_CLOUD_REMOVE                = 83;
    public static final int FIN_CLOUD_REMOVE                    = 84;

    public static final int CLOUD_SYSTEM_ABNORMAL               = 99;

    public static final int QUEUEING                            = 100;
    public static final int IMAGE_DOWNLOADING                   = 101;
    public static final int IMAGE_DOWNLOADED                    = 102;
    public static final int CREATED                             = 103;
    public static final int STARTING                            = 104;
    public static final int RUNNING                             = 105;
    public static final int RESTARTING                          = 108;

    public static final int SUCCESS_QUIT                        = 110;
    public static final int USER_STOP_QUIT                      = 111;
    public static final int ABNORMAL_QUIT                       = 113;
    public static final int SYSTEM_STOP                         = 120;

    public static final int HARDWARE_ERROR                      = 122;
    public static final int NO_IMAGE                            = 123;
    public static final int IMAGE_DOWNLOAD_FAILED               = 124;
    public static final int CREATE_FAILED                       = 125;

    public static final int REMOVED                             = 130;
    public static final int OBJECT_NOT_EXIST                    = 131;
    public static final int OBJECT_AUTO_REMOVE                  = 132;

    public static final int EDGE_SYSTEM_ABNORMAL                = 190;

    public static final int KEEP_ON                             = 200;
}
