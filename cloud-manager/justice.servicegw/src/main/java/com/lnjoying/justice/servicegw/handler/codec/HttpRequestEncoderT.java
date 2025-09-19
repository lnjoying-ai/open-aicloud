package com.lnjoying.justice.servicegw.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/13/23 10:23 PM
 */
public class HttpRequestEncoderT extends HttpObjectEncoderT<HttpRequest>
{
    private static final char SLASH = '/';
    private static final char QUESTION_MARK = '?';
    private static final int SLASH_AND_SPACE_SHORT = 12064;
    private static final int SPACE_SLASH_AND_SPACE_MEDIUM = 2109216;
    
    public HttpRequestEncoderT() {
    }
    
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && !(msg instanceof HttpResponse);
    }
    
    protected void encodeInitialLine(ByteBuf buf, HttpRequest request) throws Exception {
        ByteBufUtil.copy(request.method().asciiName(), buf);
        String uri = request.uri();
        if (uri.isEmpty()) {
            ByteBufUtil.writeMediumBE(buf, 2109216);
        } else {
            CharSequence uriCharSequence = uri;
            boolean needSlash = false;
            int start = uri.indexOf("://");
            if (start != -1 && uri.charAt(0) != '/') {
                start += 3;
                int index = uri.indexOf(63, start);
                if (index == -1) {
                    if (uri.lastIndexOf(47) < start) {
                        needSlash = true;
                    }
                } else if (uri.lastIndexOf(47, index) < start) {
                    uriCharSequence = (new StringBuilder(uri)).insert(index, '/');
                }
            }
            
            buf.writeByte(32).writeCharSequence((CharSequence)uriCharSequence, CharsetUtil.UTF_8);
            if (needSlash) {
                ByteBufUtil.writeShortBE(buf, 12064);
            } else {
                buf.writeByte(32);
            }
        }
        String vtxt = request.protocolVersion().text();
        buf.writeCharSequence(vtxt, CharsetUtil.US_ASCII);
//        request.protocolVersion().encode(buf);
        ByteBufUtil.writeShortBE(buf, 3338);
    }
}