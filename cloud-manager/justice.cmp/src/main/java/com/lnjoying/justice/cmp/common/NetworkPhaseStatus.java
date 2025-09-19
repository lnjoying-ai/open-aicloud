package com.lnjoying.justice.cmp.common;

public class NetworkPhaseStatus
{

    public static Integer getPhaseCode(String status)
    {
        switch (status)
        {
            case "Adding":
                return ADDING;
            case "Added":
                return ADDED;
            case "AddFailed":
                return ADD_FAILED;
            case  "Deleting":
                return DELETING;
            case "Deleted":
                return DELETED;
            case "DeleteFailed":
                return DELETE_FAILED;
        }
        return null;
    }

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

    public final static int SG_ADDED = 50;

    public final static int SG_UPDATED = 51;

    public final static int SG_UPDATE_FAILED = 52;

    public final static int  APPLIED = 53;

    public final static int  UNAPPLIED = 54;

    public final static int  APPLY_FAILED = 55;

    public final static int  UNAPPLY_FAILED = 56;

    public final static int APPLYING = 57;

    public final static int UNAPPLYING = 58;

    public final static int CHECK_APPLYING_RESULT = 59;

    public final static int CHECK_UNAPPLYING_RESULT = 60;

    public final static int CHECK_APPLIED_RESULT = 61;

    public final static int SG_REF_NOT_EXISTS = 62;


}
