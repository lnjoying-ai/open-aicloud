package com.lnjoying.justice.cmp.common;

import lombok.Data;

@Data
public class PortType
{
    public final static short vm = 0;
    public final static short baremetal = 1;
    public final static short vip = 2;
    public final static short lb = 3;
    public final static short nfs = 4;
}
