package com.lnjoying.justice.servicegw.model;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/14/23 4:29 PM
 */
@Data
public class DirectMsgEntity
{
    private ByteBuf              byteBuf;
    private String               address;
    private String                dataId;
    private ChannelHandlerContext srcCtx;
    private String workerId;
    private String srcHost;
}
