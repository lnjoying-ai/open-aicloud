package com.lnjoying.justice.servicegw.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.FileRegion;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/13/23 10:13 PM
 */
public abstract class HttpObjectEncoderT<H extends HttpMessage>
{
    static final int CRLF_SHORT = 3338;
    private static final int ZERO_CRLF_MEDIUM = 3149066;
    private static final byte[] ZERO_CRLF_CRLF = new byte[]{48, 13, 10, 13, 10};
    private static final ByteBuf CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(2).writeByte(13).writeByte(10));
    private static final ByteBuf ZERO_CRLF_CRLF_BUF;
    private static final float HEADERS_WEIGHT_NEW = 0.2F;
    private static final float HEADERS_WEIGHT_HISTORICAL = 0.8F;
    private static final float TRAILERS_WEIGHT_NEW = 0.2F;
    private static final float TRAILERS_WEIGHT_HISTORICAL = 0.8F;
    private static final int ST_INIT = 0;
    private static final int ST_CONTENT_NON_CHUNK = 1;
    private static final int ST_CONTENT_CHUNK = 2;
    private static final int ST_CONTENT_ALWAYS_EMPTY = 3;
    private int state = 0;
    private float headersEncodedSizeAccumulator = 256.0F;
    private float trailersEncodedSizeAccumulator = 256.0F;
    
    public HttpObjectEncoderT() {
    }
    
    public void encode(Object msg, List<Object> out) throws Exception {
        ByteBuf buf = null;
        if (msg instanceof HttpMessage) {
            if (this.state != 0) {
                throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg) + ", state: " + this.state);
            }
    
            H m = (H)msg;
            buf = Unpooled.buffer();
            this.encodeInitialLine(buf, m);
            this.state = this.isContentAlwaysEmpty(m) ? 3 : (HttpUtil.isTransferEncodingChunked(m) ? 2 : 1);
            this.sanitizeHeadersBeforeEncode(m, this.state == 3);
            this.encodeHeaders(m.headers(), buf);
            ByteBufUtil.writeShortBE(buf, 3338);
            this.headersEncodedSizeAccumulator = 0.2F * (float)padSizeForAccumulation(buf.readableBytes()) + 0.8F * this.headersEncodedSizeAccumulator;
        }
        
        if (msg instanceof ByteBuf) {
            ByteBuf potentialEmptyBuf = (ByteBuf)msg;
            if (!potentialEmptyBuf.isReadable()) {
                out.add(potentialEmptyBuf.retain());
                return;
            }
        }
        
        if (!(msg instanceof HttpContent) && !(msg instanceof ByteBuf) && !(msg instanceof FileRegion)) {
            if (buf != null) {
                out.add(buf);
            }
        } else {
            switch(this.state) {
                case 0:
                    throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg) + ", state: " + this.state);
                case 1:
                    long contentLength = contentLength(msg);
                    if (contentLength > 0L) {
                        if (buf != null && (long)buf.writableBytes() >= contentLength && msg instanceof HttpContent) {
                            buf.writeBytes(((HttpContent)msg).content());
                            out.add(buf);
                        } else {
                            if (buf != null) {
                                out.add(buf);
                            }
                            
                            out.add(encodeAndRetain(msg));
                        }
                        
                        if (msg instanceof LastHttpContent) {
                            this.state = 0;
                        }
                        break;
                    }
                case 3:
                    if (buf != null) {
                        out.add(buf);
                    } else {
                        out.add(Unpooled.EMPTY_BUFFER);
                    }
                    break;
                case 2:
                    if (buf != null) {
                        out.add(buf);
                    }
                    
                    this.encodeChunkedContent(msg, contentLength(msg), out);
                    break;
                default:
                    throw new Error();
            }
            
            if (msg instanceof LastHttpContent) {
                this.state = 0;
            }
        }
        
    }
    
    protected void encodeHeaders(HttpHeaders headers, ByteBuf buf) {
        Iterator iter = headers.iteratorCharSequence();
        
        while(iter.hasNext()) {
            Map.Entry<CharSequence, CharSequence> header = (Map.Entry)iter.next();
            HttpHeadersEncoderT.encoderHeader((CharSequence)header.getKey(), (CharSequence)header.getValue(), buf);
        }
        
    }
    
    private void encodeChunkedContent(Object msg, long contentLength, List<Object> out) {
        ByteBuf buf;
        if (contentLength > 0L) {
            String lengthHex = Long.toHexString(contentLength);
            buf = Unpooled.buffer();
            buf.writeCharSequence(lengthHex, CharsetUtil.US_ASCII);
            ByteBufUtil.writeShortBE(buf, 3338);
            out.add(buf);
            out.add(encodeAndRetain(msg));
            out.add(CRLF_BUF.duplicate());
        }
        
        if (msg instanceof LastHttpContent) {
            HttpHeaders headers = ((LastHttpContent)msg).trailingHeaders();
            if (headers.isEmpty()) {
                out.add(ZERO_CRLF_CRLF_BUF.duplicate());
            } else {
                buf = Unpooled.buffer();
                ByteBufUtil.writeMediumBE(buf, 3149066);
                this.encodeHeaders(headers, buf);
                ByteBufUtil.writeShortBE(buf, 3338);
                this.trailersEncodedSizeAccumulator = 0.2F * (float)padSizeForAccumulation(buf.readableBytes()) + 0.8F * this.trailersEncodedSizeAccumulator;
                out.add(buf);
            }
        } else if (contentLength == 0L) {
            out.add(encodeAndRetain(msg));
        }
        
    }
    
    protected void sanitizeHeadersBeforeEncode(HttpMessage msg, boolean isAlwaysEmpty) {
    }
    
    protected boolean isContentAlwaysEmpty(HttpMessage msg) {
        return false;
    }
    
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return msg instanceof HttpObject || msg instanceof ByteBuf || msg instanceof FileRegion;
    }
    
    private static Object encodeAndRetain(Object msg) {
        if (msg instanceof ByteBuf) {
            return ((ByteBuf)msg).retain();
        } else if (msg instanceof HttpContent) {
            return ((HttpContent)msg).content().retain();
        } else if (msg instanceof FileRegion) {
            return ((FileRegion)msg).retain();
        } else {
            throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
        }
    }
    
    private static long contentLength(Object msg) {
        if (msg instanceof HttpContent) {
            return (long)((HttpContent)msg).content().readableBytes();
        } else if (msg instanceof ByteBuf) {
            return (long)((ByteBuf)msg).readableBytes();
        } else if (msg instanceof FileRegion) {
            return ((FileRegion)msg).count();
        } else {
            throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
        }
    }
    
    private static int padSizeForAccumulation(int readableBytes) {
        return (readableBytes << 2) / 3;
    }
    
    /** @deprecated */
    @Deprecated
    protected static void encodeAscii(String s, ByteBuf buf) {
        buf.writeCharSequence(s, CharsetUtil.US_ASCII);
    }
    
    protected abstract void encodeInitialLine(ByteBuf var1, H var2) throws Exception;
    
    static {
        ZERO_CRLF_CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(ZERO_CRLF_CRLF.length).writeBytes(ZERO_CRLF_CRLF));
    }
}
