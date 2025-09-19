package com.lnjoying.justice.schema.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class CombRetPacket implements Serializable
{
    Integer status = 0;
    Object obj;
}
