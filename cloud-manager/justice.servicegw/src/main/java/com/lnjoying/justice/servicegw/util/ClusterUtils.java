package com.lnjoying.justice.servicegw.util;


import com.google.gson.Gson;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorData;
import com.lnjoying.justice.servicegw.model.ClusterInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/3/25 15:50
 */

public class ClusterUtils
{
    public static String getClusterId(String uri)
    {
        // /k8s/clusters/c-r24xq/api?timeout=32s
        String [] uArray = uri.split("/");
        int clustersIndex = Arrays.asList(uArray).indexOf("clusters");
        if (clustersIndex == -1)
        {
            clustersIndex = Arrays.asList(uArray).indexOf("clouds");
        }
        int idIndex = clustersIndex + 1;
        if (clustersIndex == -1)
        {
            idIndex = uArray.length - 1;
        }
        return uArray[idIndex];
    }
    
    public static ClusterInfo getClusterInfo(String uri)
    {
        // /k8s/clusters/c-r24xq/api?timeout=32s
        if (uri.startsWith("/k8s"))
        {
            return getClusterInfo_old(uri);
        }

        String [] array = uri.split("\\?"); //array[0]:path array[1]:query
        String [] uArray = array[0].split("/");
    
        ClusterInfo clusterInfo = new ClusterInfo();
    
        clusterInfo.setVendor(uArray[2]);
        clusterInfo.setClusterType(uArray[3]);
        
        clusterInfo.setClusterId(uArray[4]);

        //support ESK/NSK multi port.
        if (clusterInfo.getVendor().equalsIgnoreCase("esk") || clusterInfo.getVendor().equalsIgnoreCase("osk"))
        {
            //supprot vnc wss url.
            // e.g.
            // wss://192.168.1.250:9080/proxy/ESK/clouds/ESK_a04e7d9502d34602b181995666c37534/?token=5d477e29-585b-484c-9348-a14a81f7a3bc
            if (uArray.length == 5)
            {
                clusterInfo.setResources("vnc_auto.html");
            }
            //supprot vnc url.
            // e.g.
            // https://192.168.1.250:9080/proxy/ESK/clouds/ESK_a04e7d9502d34602b181995666c37534/vnc_auto.html?path=%3Ftoken%3D5d477e29-585b-484c-9348-a14a81f7a3bc
            // https://192.168.1.250:9080/proxy/ESK/clouds/ESK_a04e7d9502d34602b181995666c37534/vendor/promise.js
            else if (uArray.length == 6 || ! uArray[5].matches("(v|V)[0-9]+(.[0-9]+)*"))
            {
                clusterInfo.setResources(uArray[5]);
            }
            //support url with project_id
            // e.g.
            // https://192.168.1.250:9080/proxy/ESK/clouds/ESK_6746061989b843a5a316f8b6ca3b7873/v3/%7Bproject_id%7D/volumes/detail
            else if (uArray[6].length() > 30)
            {
                clusterInfo.setResources(uArray[7]);
            }
            else
            {
                clusterInfo.setResources(uArray[6]);
            }
        }
        
        return clusterInfo;
    }
    
    public static ClusterInfo getClusterInfo_old(String uri)
    {
        // /k8s/clusters/c-r24xq/api?timeout=32s
        String [] uArray = uri.split("/");
        if (uArray.length < 4)
        {
            return null;
        }
        ClusterInfo clusterInfo = new ClusterInfo();
    
        clusterInfo.setVendor(uArray[1]);
        clusterInfo.setClusterType(uArray[2]);
    
        clusterInfo.setClusterId(uArray[3]);
    
        return clusterInfo;
    }
    
    public static String trimCusterServerPrefix(String uri)
    {
        String clusterId = getClusterId(uri);
        return uri.substring(uri.indexOf(clusterId) + clusterId.length());
    }
    
    public static void sendHttpErrorResponse(ErrorCode errorCode, ChannelHandlerContext ctx)
    {
        ErrorData errorData = new ErrorData();
        errorData.setCode(errorCode.getCode());
        errorData.setMessage(errorCode.getMessage());
        FullHttpResponse response= new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(400));
    
        if (errorCode.getCode() == ErrorCode.InvalidAuthority.getCode())
        {
            response.setStatus(HttpResponseStatus.valueOf(401));
        }
        
        String rsp = JsonUtils.toJson(errorData);
    
        ByteBuf buffer = Unpooled.copiedBuffer(new StringBuilder(rsp), CharsetUtil.UTF_8);
        try
        {
            response.content().writeBytes(buffer);
            response.headers().setInt("Content-Length", response.content().readableBytes());
            response.headers().set("content-type", MediaType.APPLICATION_JSON_VALUE);
            ctx.writeAndFlush(response);
        }
        finally
        {
            buffer.release();
        }
    }
}
