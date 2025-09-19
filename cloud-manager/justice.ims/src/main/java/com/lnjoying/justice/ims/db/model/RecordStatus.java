package com.lnjoying.justice.ims.db.model;

/**
 * General record status enumeration
 *
 * @author merak
 **/

public enum RecordStatus
{
    /**
     * normal
     */
    NORMAL(1),

    /**
     * deleted
     */
    DELETED(-1);
    private final int value;

    RecordStatus(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }
}
