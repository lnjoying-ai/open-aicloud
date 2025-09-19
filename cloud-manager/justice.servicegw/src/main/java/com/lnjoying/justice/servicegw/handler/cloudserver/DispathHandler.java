package com.lnjoying.justice.servicegw.handler.cloudserver;

import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.servicegw.handler.iam.AuthRsp;
import com.lnjoying.justice.servicegw.handler.iam.AuthUtil;
import com.lnjoying.justice.servicegw.service.rpc.CombRpcService;
import com.lnjoying.justice.servicegw.util.ClusterUtils;
import com.micro.core.common.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.vertx.http.ReadStreamPart;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.TRANSFER_ENCODING;
import static io.netty.handler.codec.http.HttpHeaderValues.CHUNKED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

public class DispathHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private CombRpcService combRpcService;

    public DispathHandler(CombRpcService combRpcService)
    {
        this.combRpcService = combRpcService;
    }

    private static Logger LOGGER = LogManager.getLogger();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req)
    {
        LOGGER.info("\nchannel:"+ctx.channel().hashCode() +  "recv src req");
        
        String uri = req.uri();
        
        if (! uri.startsWith("/api"))
        {
            ctx.close();
        }

        AuthRsp authRsp;

        if (uri.matches("/api/.*/anonymous/.*"))
        {
            authRsp = new AuthRsp();
            authRsp.setErrorCode(ErrorCode.SUCCESS);
        }
        else
        {
            authRsp = AuthUtil.doAuth(uri, req.headers(), req.method().name(), combRpcService.getAuthzService(), false);
        }

        LOGGER.info("auth rsp is {}", authRsp);

        if (authRsp != null && authRsp.getErrorCode().getCode() != ErrorCode.SUCCESS.getCode())
        {
            ClusterUtils.sendHttpErrorResponse(authRsp.getErrorCode(), ctx);
            ctx.close();
            return;
        }
        
    
        RestTemplate template = RestTemplateBuilder.create();
        
        if (! uri.startsWith("/api/"))
        {
            return;
        }
    
        String tmpUrl = uri.substring(5);
        int index = tmpUrl.indexOf('/');
        String model = tmpUrl.substring(0, index);
        String cseurl = Utils.buildStr("cse://", model, "/" , tmpUrl);
    
    
        HttpHeaders httpHeaders  = new HttpHeaders();
        if (! StringUtils.isEmpty(authRsp.getUserId()))
        {
            httpHeaders.set(UserHeadInfo.USERID,   authRsp.getUserId());
        }
        if (! StringUtils.isEmpty(authRsp.getUserName()))
        {
            httpHeaders.set(UserHeadInfo.USERNAME, authRsp.getUserName());
        }
        if (! StringUtils.isEmpty(authRsp.getBpId()))
        {
            httpHeaders.set(UserHeadInfo.BPID,     authRsp.getBpId());
        }
    
        if (! StringUtils.isEmpty(authRsp.getBpName()))
        {
            httpHeaders.set(UserHeadInfo.BpName,   authRsp.getBpName());
        }
    
        if (! StringUtils.isEmpty(authRsp.getUserKind()))
        {
            httpHeaders.set(UserHeadInfo.USERKIND, authRsp.getUserKind());
        }
    
        if (! StringUtils.isEmpty(authRsp.getAuthorities()))
        {
            httpHeaders.set(UserHeadInfo.AUTIORITIES, authRsp.getAuthorities());
        }

        if (! StringUtils.isEmpty(authRsp.getProjectId()))
        {
            httpHeaders.set(UserHeadInfo.PROJECT, authRsp.getProjectId());
        }
        
        String method = req.method().name();
        
            
        for (Map.Entry<String, String> entry : req.headers().entries())
        {
            //remove jwt token
            String key = entry.getKey();
            if (key.equals("X-Access-Token"))
            {
                continue;
            }
            
            if (key.equals("Cookie"))
            {
                int tokenIndex = key.indexOf("Access-Token=");
                if (tokenIndex >= 0)
                {
                    int next = key.indexOf(";", tokenIndex);
                    if (next >= 0)
                    {
                        key = key.substring(0, tokenIndex) + key.substring(next+1);
                    }
                    else
                    {
                        key = key.substring(0, tokenIndex);
                    }
        
                }
            }
            
            httpHeaders.add(key, entry.getValue());
        }
        
        int len = req.content().readableBytes();

        HttpEntity<Object> httpEntity = null;
        HttpMethod httpMethod = HttpMethod.resolve(method);

        if (len == 0)
        {
            httpEntity = new HttpEntity(httpHeaders);
        }
        else
        {
            Buffer srcbuffer = new BufferImpl();
            srcbuffer.appendBytes(ByteBufUtil.getBytes(req.content()));
            MediaType contentType = httpHeaders.getContentType();
            if (Objects.nonNull(contentType) && contentType.equalsTypeAndSubtype(APPLICATION_FORM_URLENCODED))
            {
                httpEntity = new HttpEntity<>(srcbuffer, httpHeaders);
            }
            else if (Objects.nonNull(contentType) &&  contentType.equalsTypeAndSubtype(MULTIPART_FORM_DATA))
            {
                try
                {
                    MultiValueMap<String, Object> map = parseMultipartFormData(req);
                    httpEntity = new HttpEntity<>(map, httpHeaders);
                }
                catch (IOException e)
                {
                    LOGGER.error("parse multipart form data error", e);
                    throw new RuntimeException(e);
                }
            }
            else
            {
                httpEntity = new HttpEntity<>(srcbuffer.toJsonObject(), httpHeaders);
            }
        }
        
        ByteBuf buffer = null;
        
        try
        {
            ResponseEntity<Object> responseEntity = template.exchange(cseurl, httpMethod, httpEntity, Object.class);
            
            FullHttpResponse response= new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(responseEntity.getStatusCodeValue()));
            Set<Map.Entry<String, List<String>>> hdrs = responseEntity.getHeaders().entrySet();
            hdrs.forEach(s ->  response.headers().set(s.getKey(), s.getValue()));

            String trans = response.headers().get("trans");
            String contentType = response.headers().get(HttpHeaders.CONTENT_TYPE);
            String transfer = response.headers().get(TRANSFER_ENCODING);

            if (responseEntity.getBody() != null)
            {

                // todo currently just simple deal
                if (StringUtils.isNotBlank(transfer) && CHUNKED.contentEquals(transfer))
                {
                    response.headers().remove(TRANSFER_ENCODING);
                }
                if (StringUtils.isNotEmpty(trans) && trans.equals("convert"))
                {
                    String content = (String)responseEntity.getBody();
                    buffer = Buffer.buffer(Utils.hexToByteArray(content)).getByteBuf();
                    response.headers().remove("trans");
                }
                else if (StringUtils.isNotEmpty(contentType) && contentType.toLowerCase(Locale.ROOT).contains("application/octet-stream"))
                {
                    ReadStreamPart content = (ReadStreamPart)responseEntity.getBody();
                    buffer = Buffer.buffer(content.saveAsBytes().get()).getByteBuf();
                }
                else
                {
                    String s = JsonUtils.toJson(responseEntity.getBody());
                    buffer = Unpooled.copiedBuffer(new StringBuilder(s), CharsetUtil.UTF_8);
                }

                response.content().writeBytes(buffer);
                response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
            }
            else
            {
                response.content().writeBytes(Unpooled.copiedBuffer("", StandardCharsets.UTF_8));
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                response.headers().setInt("Content-Length", 0);
                if (StringUtils.isNotBlank(transfer) && CHUNKED.contentEquals(transfer))
                {
                    response.headers().remove(TRANSFER_ENCODING);
                }
            }

            ctx.writeAndFlush(response);
        }
        catch (InvocationException invocationException)
        {
            Object errorData = invocationException.getErrorData();
            FullHttpResponse response= new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(invocationException.getStatusCode()));
            String s = JsonUtils.toJson(errorData);
            buffer = Unpooled.copiedBuffer(new StringBuilder(s), CharsetUtil.UTF_8);
            response.content().writeBytes(buffer);
            response.headers().setInt("Content-Length", response.content().readableBytes());
            response.headers().set("content-type", MediaType.APPLICATION_JSON_VALUE);
            ctx.writeAndFlush(response);
            
            LOGGER.error("error info: {}", invocationException);
        }
        catch (Exception e)
        {
            FullHttpResponse response= new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(500));
            String s = JsonUtils.toJson(ErrorCode.SystemError);
            buffer = Unpooled.copiedBuffer(new StringBuilder(s), CharsetUtil.UTF_8);
            response.content().writeBytes(buffer);
            response.headers().setInt("Content-Length", response.content().readableBytes());
            response.headers().set("content-type", MediaType.APPLICATION_JSON_VALUE);
            ctx.writeAndFlush(response);
            
        }
        finally
        {
            if (buffer != null)
            {
                buffer.release();
            }
        }
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

    private MultiValueMap<String, Object> parseMultipartFormData(FullHttpRequest request) throws IOException
    {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 创建HttpPostRequestDecoder来解析MULTIPART_FORM_DATA
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);

        // 遍历解析结果
        List<InterfaceHttpData> httpDatas = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : httpDatas) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                FileUpload fileUpload = (FileUpload) data;
                ByteBuf byteBuf = fileUpload.getByteBuf();
                byte[] fileBytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(fileBytes);

                body.add(fileUpload.getName(), fileBytes);
            } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                String fieldName = ((io.netty.handler.codec.http.multipart.Attribute) data).getName();
                String fieldValue = ((io.netty.handler.codec.http.multipart.Attribute) data).getValue();
                body.add(fieldName, fieldValue);
            }
        }

        // 释放资源
        decoder.destroy();

        return body;
    }

}
