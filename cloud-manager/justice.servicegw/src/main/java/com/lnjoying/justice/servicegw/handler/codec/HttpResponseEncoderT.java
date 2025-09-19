package com.lnjoying.justice.servicegw.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/14/23 3:05 PM
 */
public class HttpResponseEncoderT extends HttpObjectEncoderT<HttpResponse>
{
    public HttpResponseEncoderT() {
    }
    
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && !(msg instanceof HttpRequest);
    }
    
    protected void encodeInitialLine(ByteBuf buf, HttpResponse response) throws Exception {
        String vtxt = response.protocolVersion().text();
        buf.writeCharSequence(vtxt, CharsetUtil.US_ASCII);
        buf.writeByte(32);
        HttpResponseStatus status = response.status();
        AsciiString asciiStatus = status.codeAsText();
    
        ByteBufUtil.copy(asciiStatus, buf);
        buf.writeByte(32);
        String reasonPhrase = status.reasonPhrase();
        buf.writeCharSequence(reasonPhrase, CharsetUtil.US_ASCII);
        ByteBufUtil.writeShortBE(buf, 3338);
    }
    
    protected void sanitizeHeadersBeforeEncode(HttpResponse msg, boolean isAlwaysEmpty) {
        if (isAlwaysEmpty) {
            HttpResponseStatus status = msg.status();
            if (status.codeClass() != HttpStatusClass.INFORMATIONAL && status.code() != HttpResponseStatus.NO_CONTENT.code()) {
                if (status.code() == HttpResponseStatus.RESET_CONTENT.code()) {
                    msg.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
                    msg.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, 0);
                }
            } else {
                msg.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
                msg.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
            }
        }
        
    }
    
    protected boolean isContentAlwaysEmpty(HttpResponse msg) {
        HttpResponseStatus status = msg.status();
        if (status.codeClass() == HttpStatusClass.INFORMATIONAL) {
            return status.code() == HttpResponseStatus.SWITCHING_PROTOCOLS.code() ? msg.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_VERSION) : true;
        } else {
            return status.code() == HttpResponseStatus.NO_CONTENT.code() || status.code() == HttpResponseStatus.NOT_MODIFIED.code() || status.code() == HttpResponseStatus.RESET_CONTENT.code();
        }
    }
}
