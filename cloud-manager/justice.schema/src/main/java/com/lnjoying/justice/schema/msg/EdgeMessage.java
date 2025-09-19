package com.lnjoying.justice.schema.msg;

import lombok.Data;

import java.io.Serializable;

@Data
public class EdgeMessage implements Serializable
{
    String nodeId;  //(1) cloud->gw->edge，fill edge nodeId;(2) cloud->gw, fill gw nodeId (3) gw->cloud, fill gw nodeId
    byte[] netMessage;
}
