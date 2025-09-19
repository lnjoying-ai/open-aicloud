package com.lnjoying.justice.schema.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class CombRetPbPacket implements Serializable
{
    Integer status = 0;
    byte [] obj;
}
