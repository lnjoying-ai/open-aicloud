package com.micro.core.nework.entity;

public class NetEntity
{
    static final long serialVersionUID = 42L;
    private String Id;
    private String Desc;
    public enum ChannelType {CLIENT,SERVER,DUPLEX};
    public NetEntity()
    {

    }
    public NetEntity(String host, int port, ChannelType channelType)
    {
        this.host = host;
        this.port = port;
        this.channelType = channelType;
    }

    private ChannelType channelType;
    private String host;
    private int port;

    public void setHost(String host) {this.host = host;}
    public String getHost() {return this.host;}

    public void setPort(Integer port) {this.port = port;}
    public Integer getPort() {return this.port;}

    public void setChannelType(ChannelType chType) {this.channelType = chType;}
    public ChannelType getChannelType() {return this.channelType;}
}
