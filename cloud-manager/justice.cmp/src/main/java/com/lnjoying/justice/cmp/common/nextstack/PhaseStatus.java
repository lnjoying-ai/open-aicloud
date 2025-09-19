package com.lnjoying.justice.cmp.common.nextstack;

public class PhaseStatus
{
    public final static int ADDING = 0;

    public final static int ADDED = 1;

    public final static int ADD_FAILED = 2;

    public final static int DELETING = 3;

    public final static int DELETED = 4;

    public final static int DELETE_FAILED = 5;

    public final static int AGENT_ADDING = 6;

    public final static int AGENT_ADDING_ERR = 7;

    public final static int AGENT_DELETING = 8;

    public final static int AGENT_DELETING_ERR = 9;

    public final static int ARPING = 10;

    public final static int ARPING_DONE = 11;

    public final static int ARPING_ERR = 12;

    public final static int ARPING_OK = 13;

    public final static int SG_RULE_UPDATE = 101;
    public final static int SG_RULE_UPDATE_FAILED = 102;
    public final static int SG_RULE_UPDATING = 103;

    public final static int UPDATING = 41;

    public final static int UPDATED = 42;

    public final static int AGENT_UPDATING = 43;

    public final static int AGENT_UPDATING_ERR = 44;

    public final static int UPDATE_FAILED = 45;

    public final static int ATTACHING = 20;

    public final static int AGENT_ATTACHING = 21;

    public final static int DETACHING = 22;

    public final static int AGENT_DETACHING = 23;

    public final static int ATTACH_FAILED = 24;

    public final static int DETACH_FAILED = 25;

    public final static int ATTACHED = 26;

    public final static int DETACHED = 27;

    public final static int PRE_DEST_RESUMING = 28;

    public final static int RESUMING = 29;

    public final static int AGENT_RESUMING = 30;

    public final static int RESUMED = 31;

    public final static int SUSPEND = 32;

    public final static int EXPORTING = 33;

    public final static int AGENT_EXPORTING = 34;

    public final static int SUSPENDING = 35;

    public final static int AGENT_SUSPENDING = 36;

    public final static int SUSPEND_FAILED = 37;

    public final static int RESUME_FAILED = 38;

    public final static int IMPORTING = 39;

    public final static int SNAP_SWITCHING = 50;

    public final static int AGENT_SWITCHING = 51;

    public final static int SNAP_SWITCH_FAILED = 52;

    public final static int EXPORT_FAILED = 53;

    public final static int  UNAPPLIED = 54;

    public final static int  APPLY_FAILED = 55;

    public final static int  UNAPPLY_FAILED = 56;

    public final static int APPLYING = 57;

    public final static int UNAPPLYING = 58;

    public final static int CHECK_APPLYING_RESULT = 59;

    public final static int CHECK_UNAPPLYING_RESULT = 60;

    public final static int CHECK_APPLIED_RESULT = 61;

    public final static int SG_REF_NOT_EXISTS = 62;

    public final static int PORT_CREATING = 70;

    public final static int ATTACH_EIP_INIT = 80;

    public final static int ATTACH_EIP_ING = 81;

    public final static int ATTACH_EIP_DONE = 82;

    public final static int DETACH_EIP_INIT = 83;

    public final static int DETACH_EIP_ING = 84;

    public final static int DETACH_EIP_DONE = 85;

    public final static int ATTACH_EIP_ERR = 86;

    public final static int DETACH_EIP_ERR = 87;
}
