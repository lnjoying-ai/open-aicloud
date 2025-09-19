package com.lnjoying.justice.cmp.common;

public class VmInstanceStatus
{
    public final static int VM = 0;

    public final static int INSTANCE_INIT = 0;

    public final static int INSTANCE_CREATING = 1;

    public final static int INSTANCE_CREATED = 2;

    public final static int INSTANCE_INJECTING =3;

    public final static int INSTANCE_INJECT_BOOTING = 4;

    public final static int INSTANCE_CLOUDINIT_DONE = 5;

    public final static int INSTANCE_RUNNING = 6;

    public final static int INSTANCE_POWEROFFING = 7;

    public final static int INSTANCE_POWEROFF = 8;

    public final static int INSTANCE_POWERONING = 9;

    public final static int INSTANCE_MONITOR_TAG_DONE = 10;

    public final static int INSTANCE_MIGRATE_INIT = 300;

    public final static int INSTANCE_SUSPENDING = 301;

    public final static int INSTANCE_SUSPENDED = 302;

    public final static int GET_PORT_MIGRATED_STATUS = 303;

    public final static int GET_INSTANCE_RESUME_STATUS = 304;

    public final static int INSTANCE_RESUMED = 305;

//    public final static int INSTANCE_MIGRATE_DONE = 306;

    public final static int GET_PORT_PHASE_STATUS = 11;

    public final static int GET_INSTANCE_CREATED_STATUS = 12;

    public final static int WAIT_INSTANCE_CLOUD_INIT_RESULT = 13;

    public final static int GET_INSTANCE_REMOVED_STATUS = 14;

    public final static int GET_SNAP_REMOVED_STATUS = 15;

    public final static int GET_INSTANCE_POWERON_RESULT = 16;

    public final static int GET_INSTANCE_POWEROFF_RESULT = 17;

    public final static int GET_SNAP_SWITCHED_STATUS = 18;

    public final static int INSTANCE_CREATE_FAILED = 21;

    public final static int INSTANCE_REMOVE_FAILED = 22;

    public final static int INSTANCE_INJECT_BOOT_FAILED = 23;

    public final static int INSTANCE_EJECT_FAILED = 24;

    public final static int SNAP_CREATE_FAILED = 25;

    public final static int SNAP_REMOVE_FAILED = 26;

    public final static int SNAP_SWITCH_FAILED = 27;

    public final static int INSTANCE_MIGRATE_FAILED = 28;

    public final static int INSTANCE_MIGRATE_CLEAN = 29;

    public final static int INSTANCE_REMOVING = 101;

    public final static int INSTANCE_REMOVED_FAILED = 103;

    public final static int INSTANCE_EJECTING = 104;

    public final static int INSTANCE_EJECTED = 105;

    public final static int INSTANCE_CREATE_FAILED_CLEANING = 110;

    public final static int INSTANCE_CREATE_FAILED_CLEANED = 111;

    public final static int SNAP_INIT = 30;
    public final static int SNAP_CREATING = 31;
    public final static int SNAP_CREATED = 32;
    public final static int SNAP_SWITCHING = 33;
    public final static int SNAP_SWITCHED = 34;
    public final static int SNAP_REMOVING = 35;


    public final static int HYPERVISOR_NODE_CREATED = 40;
    public final static int HYPERVISOR_NODE_CHECKING = 41;



    public final static int SG_ADDED = 50;

    public final static int SG_UPDATED = 51;

    public final static int SG_UPDATE_FAILED = 52;

    public final static int  APPLIED = 53;

    public final static int  UNAPPLIED = 54;

    public final static int  APPLY_FAILED = 55;

    public final static int  UNAPPLY_FAILED = 56;

    public final static int APPLYING = 57;

    public final static int UNAPPLYING = 58;


    public final static int PORT_CREATE = 70;



    public final static int DEVICE_ATTACHING = 80;

    public final static int DEVICE_AGENT_ATTACHING = 81;

    public final static int DEVICE_DETACHING = 82;

    public final static int DEVICE_AGENT_DETACHING = 83;

    public final static int DEVICE_ATTACH_FAILED = 84;

    public final static int DEVICE_DETACH_FAILED = 85;

    public final static int DEVICE_ATTACHED = 86;

    public final static int DEVICE_DETACHED = 87;

    public final static int DEVICE_INIT_CREATE = 88;

}
