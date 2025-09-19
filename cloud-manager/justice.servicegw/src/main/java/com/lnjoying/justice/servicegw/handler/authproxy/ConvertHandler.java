package com.lnjoying.justice.servicegw.handler.authproxy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import com.lnjoying.justice.servicegw.model.ClusterProxyMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConvertHandler extends SimpleChannelInboundHandler<ClusterProxyMsg>
{
    private static Logger LOGGER = LogManager.getLogger();
    JsonParser jsonParser = new JsonParser();
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClusterProxyMsg msg)
    {
        Object object = msg.getReqObj();
        if (object instanceof HttpRequest)
        {
            HttpRequest request = (HttpRequest) object;
            //to set Http req message
            String clusterIp = msg.getClusterEntity().getClusterIp();
            int clusterPort = msg.getClusterEntity().getClusterPort();
            String srcHost = request.headers().get(HttpHeaderNames.HOST);
            msg.setSrcHost(srcHost);
            
            request.headers().set(HttpHeaderNames.HOST, clusterIp + ":" + clusterPort);
            String uri = request.uri();
            String clusterId = msg.getClusterEntity().getClusterId();
            ClusterEntity clusterEntity = msg.getClusterEntity();
            if (clusterEntity.getClusterType().equals("k8s"))
            {
                request.headers().set("Authorization", clusterEntity.getToken());
            }
            else if (clusterEntity.getClusterType().equals("clouds"))
            {
                JsonObject jsonObject = JsonUtils.fromJson(clusterEntity.getToken(), JsonObject.class);
                if (clusterEntity.getVendor().toLowerCase().equals("nsk"))
                {
                    String id = jsonObject.get("id").getAsString();
                    String  secret = jsonObject.get("secret").getAsString();

                    if (uri.contains("api/vnc"))
                    {
                        uri = uri + "?X-Access-Key=" + id +"&X-Access-Secret=" + secret;
                    }
                    else
                    {
                        request.headers().set("X-Access-Key", id);
                        request.headers().set("X-Access-Secret", secret);
                    }
                }
                else if (clusterEntity.getVendor().equalsIgnoreCase("esk") || clusterEntity.getVendor().equalsIgnoreCase("osk"))
                {
                    if (jsonObject != null)
                    {
                        String xAuthToken = jsonObject.get("xAuthToken") == null ? null : jsonObject.get("xAuthToken").getAsString();
                        if (StringUtils.isNotEmpty(xAuthToken))
                        {
                            request.headers().set("X-Auth-Token", xAuthToken);
                        }
                    }
                    if (request.headers().contains("Origin"))
                    {
                        request.headers().remove("Origin");
                    }
                }
            }
            else if (clusterEntity.getClusterType().equals("serviceport"))
            {
                if (request.headers().contains("LnjoyingNetworkTest"))
                {
                    clusterEntity.setNetworkTest(true);
                }
            }

            if (uri.contains(clusterId)) uri =  uri.substring(uri.indexOf(msg.getClusterEntity().getClusterId()) + clusterId.length());
            if (StringUtils.isNotEmpty(msg.getClusterEntity().getUrlPrefix()))
            {
                uri = msg.getClusterEntity().getUrlPrefix() + uri;
            }
            request.setUri(uri);
            
        }
        else
        {
            ClusterEntity clusterEntity = msg.getClusterEntity();
            if (clusterEntity.getClusterType().equals("serviceport") && ! (object instanceof HttpContent))
            {
                byte[] data = (byte[]) object;
                for (int i = 0; i < (data.length - 18); i++)
                {
                    //LnjoyingNetworkTest
                    if (data[i] == 76 && data[i + 1] == 110 && data[i + 2] == 106 && data[i + 3] == 111 &&
                            data[i + 4] == 121 && data[i + 5] == 105 && data[i + 6] == 110 && data[i + 7] == 103 &&
                            data[i + 8] == 78 && data[i + 9] == 101 && data[i + 10] == 116 && data[i + 11] == 119 &&
                            data[i + 12] == 111 && data[i + 13] == 114 && data[i + 14] == 107 && data[i + 15] == 84 &&
                            data[i + 16] == 101 && data[i + 17] == 115 && data[i + 18] == 116)
                    {
                        clusterEntity.setNetworkTest(true);
                        break;
                    }
                }
            }
        }
    
        ctx.fireChannelRead(msg);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        LOGGER.info("convert handler error, {}", cause.getMessage());
        ctx.fireExceptionCaught(cause);
    }
    
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("convertHandler inactive");
        ctx.fireChannelInactive();
    }
}
