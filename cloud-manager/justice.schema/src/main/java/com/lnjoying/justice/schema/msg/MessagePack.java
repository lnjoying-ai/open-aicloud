package com.lnjoying.justice.schema.msg;

import lombok.Data;

@Data
public class MessagePack
{
    private String msgType;
    private Object messageObj;
}
