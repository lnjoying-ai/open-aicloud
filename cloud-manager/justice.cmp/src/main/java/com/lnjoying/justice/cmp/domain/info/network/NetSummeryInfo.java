package com.lnjoying.justice.cmp.domain.info.network;

import lombok.Data;

import java.io.Serializable;

@Data
public class NetSummeryInfo implements Serializable
{
    private int natCount;
    private int eipCount;
    private int sgCount;
}
