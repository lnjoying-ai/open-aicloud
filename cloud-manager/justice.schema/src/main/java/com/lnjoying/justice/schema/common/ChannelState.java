package com.lnjoying.justice.schema.common;

public enum ChannelState
{
    DISCONNECTED(0), CONNECTED(1);

    private final int value;
    private ChannelState(int value)
    {
        this.value = value;
    }

    public static ChannelState valueOf(int value)
    {
        switch (value) {
            case 0:
                return ChannelState.DISCONNECTED;
            case 1:
                return ChannelState.CONNECTED;
            default:
                return null;
        }
    }

    public int getValue()
    {
        return value;
    }
}
