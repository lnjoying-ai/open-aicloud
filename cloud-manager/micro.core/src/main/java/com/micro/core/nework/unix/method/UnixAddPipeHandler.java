package com.micro.core.nework.unix.method;

import io.netty.channel.unix.UnixChannel;

public interface UnixAddPipeHandler
{
    public void addHandler(UnixChannel unixChannel);
}
