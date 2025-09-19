package com.lnjoying.justice.servicegw.handler.authproxy;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.cluster.ClusterAuthParams;
import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import com.lnjoying.justice.servicegw.handler.iam.AuthRsp;
import com.lnjoying.justice.servicegw.handler.iam.AuthUtil;
import com.lnjoying.justice.servicegw.model.ClusterInfo;
import com.lnjoying.justice.servicegw.model.ClusterProxyMsg;
import com.lnjoying.justice.servicegw.service.rpc.CombRpcService;
import com.lnjoying.justice.servicegw.util.BeanMapTool;
import com.lnjoying.justice.servicegw.util.ClusterUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.spdy.SpdyDataFrame;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.lnjoying.justice.schema.common.ErrorCode.RPC_AUTH_CLUSTER_FAILED;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class AuthHandler extends SimpleChannelInboundHandler<Object>
{
    private static Logger LOGGER = LogManager.getLogger();
    
    ClusterEntity clusterEntity;
    
    CombRpcService combRpcService;
    
    private boolean enalbeUpgrade ;
    
    static Map<String, ClusterEntity> clusterEntityMap = new HashMap<>();

    public AuthHandler(CombRpcService combRpcService)
    {
        this.combRpcService = combRpcService;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
    
    private static void notify100Continue(ChannelHandlerContext ctx)
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }
    
    boolean setK8sClusterEntity(ClusterInfo clusterInfo)
    {
        ClusterAuthParams clusterAuthParams =  ClusterAuthParams.builder().clusterId(clusterInfo.getClusterId()).token(clusterInfo.getToken()).build();
        //to do: call cluster manager auth rpc
        try
        {
            if (clusterInfo.getClusterType().equals("clusters"))
            {
                CombRetPacket retPacket = combRpcService.getClusterManagerService().authCluster(clusterAuthParams);
                if (retPacket.getStatus() != ErrorCode.SUCCESS.getCode())
                {
                    LOGGER.error("{} auth failed.", clusterInfo.getClusterId());
                    return false;
                }
            
                clusterEntity = BeanMapTool.mapToBean((LinkedHashMap) retPacket.getObj(), ClusterEntity.class);
                clusterEntity.setClusterType(clusterInfo.getClusterType());
                clusterEntity.setVendor(clusterInfo.getVendor());
            }
        
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(RPC_AUTH_CLUSTER_FAILED, ErrorLevel.ERROR);
        }
        return true;
    }
    
    boolean setCmpEntity(ClusterInfo clusterInfo)
    {
        //to do for cmp
        ClusterAuthParams clusterAuthParams =  ClusterAuthParams.builder().clusterId(clusterInfo.getClusterId()).token(clusterInfo.getToken()).resources(clusterInfo.getResources()).build();
        //to do: call cluster manager auth rpc
        try
        {
            CombRetPacket retPacket = combRpcService.getCmpManagerService().authCloud(clusterAuthParams);
            if (retPacket.getStatus() != ErrorCode.SUCCESS.getCode())
            {
                LOGGER.error("{} auth failed.", clusterInfo.getClusterId());
                return false;
            }
    
            clusterEntity = BeanMapTool.mapToBean((LinkedHashMap) retPacket.getObj(), ClusterEntity.class);
            clusterEntity.setClusterType(clusterInfo.getClusterType());
            clusterEntity.setVendor(clusterInfo.getVendor());
        
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(RPC_AUTH_CLUSTER_FAILED, ErrorLevel.ERROR);
        }
        return true;
    }
    
    boolean setClusterEntity(ClusterInfo clusterInfo)
    {
        clusterEntity = clusterEntityMap.get(clusterInfo.getClusterId() + clusterInfo.getToken() + clusterInfo.getResources());
        boolean setResult = false;
        
        if (null == clusterEntity)
        {
            if (clusterInfo.getVendor().equals("k8s"))
            {
                setResult = setK8sClusterEntity(clusterInfo);
            }
            else
            {
                setResult = setCmpEntity(clusterInfo);
            }
            
            if (setResult)
            {
                clusterEntityMap.put(clusterInfo.getClusterId() + clusterInfo.getToken() + clusterInfo.getResources(), clusterEntity);
            }
        }
        else
        {
            setResult = true;
        }
        return setResult;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
    {
    
        LOGGER.info("channel {} recv k8s src req, recv http request: {}", ctx.channel().hashCode(),  msg);
        
        if (msg instanceof HttpRequest)
        {
            HttpRequest req = (HttpRequest) msg;
            if (HttpUtil.is100ContinueExpected(req))
            {
                notify100Continue(ctx);
            }
            
            String uri = req.uri();
            ClusterInfo clusterInfo = ClusterUtils.getClusterInfo(uri);
            if (clusterInfo == null)
            {
                ctx.close();
                return;
            }
            
            if (clusterInfo.getVendor().equals("k8s"))
            {
                String token = req.headers().get("Authorization");
                if (token.isEmpty())
                {
                    ctx.close();
                    return;
                }
                
                clusterInfo.setToken(token);
            }
            else
            {
                String token = AuthUtil.getToken(req.headers());
                if (StringUtils.isNotEmpty(token) && token.startsWith("systemadmin"))
                {
                    clusterInfo.setToken(token);
                }
                else
                {
                    AuthRsp authRsp = AuthUtil.doAuth(uri, req.headers(), req.method().name(), combRpcService.getAuthzService(), true);
                    if (authRsp.getErrorCode().getCode() != ErrorCode.SUCCESS.getCode())
                    {
                        ClusterUtils.sendHttpErrorResponse(authRsp.getErrorCode(), ctx);
                        ctx.close();
                        return;
                    }
                    clusterInfo.setToken("");
                }
            }
            
            //to do: parse uri.
            if (null == clusterEntity)
            {
                if (! setClusterEntity(clusterInfo))
                {
                    ctx.close();
                    return;
                }
            }
            else
            {
                if (! clusterInfo.getClusterId().equals(clusterEntity.getClusterId()))
                {
                    LOGGER.error("cluster id {} not equal pre request cluster id{}, reset cluster id", clusterInfo.getClusterId(), clusterEntity.getClusterId());
                    if (! setClusterEntity(clusterInfo))
                    {
                        ctx.close();
                        return;
                    }
                }
            }

            if ((new Date().getTime() - clusterEntity.getTimestamp()) > 100000)
            {
                LOGGER.info("refresh clusterEntity, clusterId: {}", clusterInfo.getClusterId());
                clusterEntityMap.remove(clusterInfo.getClusterId() + clusterInfo.getToken() + clusterInfo.getResources());
                if (! setClusterEntity(clusterInfo))
                {
                    ctx.close();
                }
            }
            
            String connection = req.headers().get(HttpHeaderNames.CONNECTION);
            if (StringUtils.isNotBlank(connection))
            {
                if (connection.toUpperCase().contains("UPGRADE"))
                {
                    String dst = req.headers().get(HttpHeaderNames.UPGRADE);
                    dst = dst.toUpperCase();
                    if (! dst.isEmpty())
                    {
                        enalbeUpgrade = true;
                    }
                    
                    if (dst.contains("SPDY"))
                    {
                        LOGGER.info("upgrade to  spdy: {}", dst);
                    }
                    
                }
            }
        }
        else if (msg instanceof HttpContent)
        {
            LOGGER.debug("auth refcnt {}", ((HttpContent) msg).refCnt());
            
            if (msg instanceof LastHttpContent)
            {
                if (enalbeUpgrade)
                {
                    setAuthDecodePipeLine(ctx.pipeline());
                }
            }
        }
        else
        {
            if (msg instanceof SpdyDataFrame)
            {
                LOGGER.info("it is spdy data frame");
            }
            else
            {
                LOGGER.info("unknown msg");
            }
        }
        
        if (clusterEntity == null)
        {
            LOGGER.info("cluster entity is null");
            return;
        }
        LOGGER.info("send to worker {}", clusterEntity.getWorkderId());
        ClusterProxyMsg proxyMsg = new ClusterProxyMsg();
        proxyMsg.setClusterEntity(clusterEntity);
        proxyMsg.setReqObj(msg);
        ctx.fireChannelRead(proxyMsg);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        LOGGER.info("auth handler error");
        ctx.fireExceptionCaught(cause);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("auth handler inactive");
        ctx.fireChannelInactive();
    }
    
    void setAuthDecodePipeLine(ChannelPipeline pipeline)
    {
        pipeline.replace("httpDecoder", "byteDecoder", new ByteArrayDecoder());
    }
}
