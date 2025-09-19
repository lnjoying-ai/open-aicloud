package com.lnjoying.justice.servicegw.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.PrematureChannelClosureException;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.*;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.AppendableCharSequence;
import io.netty.util.internal.ObjectUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/16/23 4:57 PM
 */
public abstract class HttpObjectDecoderT
{
    public static final int DEFAULT_MAX_INITIAL_LINE_LENGTH = 4096;
    public static final int DEFAULT_MAX_HEADER_SIZE = 8192;
    public static final boolean DEFAULT_CHUNKED_SUPPORTED = true;
    public static final int DEFAULT_MAX_CHUNK_SIZE = 8192;
    public static final boolean DEFAULT_VALIDATE_HEADERS = true;
    public static final int DEFAULT_INITIAL_BUFFER_SIZE = 128;
    public static final boolean DEFAULT_ALLOW_DUPLICATE_CONTENT_LENGTHS = false;
    private static final String EMPTY_VALUE = "";
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private final int maxChunkSize;
    private final boolean chunkedSupported;
    protected final boolean validateHeaders;
    private final boolean allowDuplicateContentLengths;
    private final HttpObjectDecoderT.HeaderParser headerParser;
    private final HttpObjectDecoderT.LineParser lineParser;
    private HttpMessage message;
    private long chunkSize;
    private long contentLength;
    private volatile boolean resetRequested;
    private CharSequence name;
    private CharSequence value;
    private LastHttpContent trailer;
    private HttpObjectDecoderT.State currentState;
    
    protected HttpObjectDecoderT() {
        this(4096, 8192, 8192, true);
    }
    
    protected HttpObjectDecoderT(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, true);
    }
    
    protected HttpObjectDecoderT(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, validateHeaders, 128);
    }
    
    protected HttpObjectDecoderT(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders, int initialBufferSize) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, validateHeaders, initialBufferSize, false);
    }
    
    protected HttpObjectDecoderT(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders, int initialBufferSize, boolean allowDuplicateContentLengths) {
        this.contentLength = -9223372036854775808L;
        this.currentState = HttpObjectDecoderT.State.SKIP_CONTROL_CHARS;
        ObjectUtil.checkPositive(maxInitialLineLength, "maxInitialLineLength");
        ObjectUtil.checkPositive(maxHeaderSize, "maxHeaderSize");
        ObjectUtil.checkPositive(maxChunkSize, "maxChunkSize");
        AppendableCharSequence seq = new AppendableCharSequence(initialBufferSize);
        this.lineParser = new HttpObjectDecoderT.LineParser(seq, maxInitialLineLength);
        this.headerParser = new HttpObjectDecoderT.HeaderParser(seq, maxHeaderSize);
        this.maxChunkSize = maxChunkSize;
        this.chunkedSupported = chunkedSupported;
        this.validateHeaders = validateHeaders;
        this.allowDuplicateContentLengths = allowDuplicateContentLengths;
    }
    
    public void decode(ByteBuf buffer, List<Object> out) throws Exception {
        if (this.resetRequested) {
            this.resetNow();
        }
        
        int toRead;
        AppendableCharSequence line;
        switch(this.currentState) {
            case SKIP_CONTROL_CHARS:
            case READ_INITIAL:
                try {
                    line = this.lineParser.parse(buffer);
                    if (line == null) {
                        return;
                    }
                    
                    String[] initialLine = splitInitialLine(line);
                    if (initialLine.length < 3) {
                        this.currentState = HttpObjectDecoderT.State.SKIP_CONTROL_CHARS;
                        return;
                    }
                    
                    this.message = this.createMessage(initialLine);
                    this.currentState = HttpObjectDecoderT.State.READ_HEADER;
                } catch (Exception var9) {
                    out.add(this.invalidMessage(buffer, var9));
                    return;
                }
            case READ_HEADER:
                try {
                    HttpObjectDecoderT.State nextState = this.readHeaders(buffer);
                    if (nextState == null) {
                        return;
                    }
                    
                    this.currentState = nextState;
                    switch(nextState) {
                        case SKIP_CONTROL_CHARS:
                            out.add(this.message);
                            out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                            this.resetNow();
                            return;
                        case READ_CHUNK_SIZE:
                            if (!this.chunkedSupported) {
                                throw new IllegalArgumentException("Chunked messages not supported");
                            }
                            
                            out.add(this.message);
                            return;
                        default:
                            long contentLength = this.contentLength();
                            if (contentLength != 0L && (contentLength != -1L || !this.isDecodingRequest())) {
                                assert nextState == HttpObjectDecoderT.State.READ_FIXED_LENGTH_CONTENT || nextState == HttpObjectDecoderT.State.READ_VARIABLE_LENGTH_CONTENT;
                                
                                out.add(this.message);
                                if (nextState == HttpObjectDecoderT.State.READ_FIXED_LENGTH_CONTENT) {
                                    this.chunkSize = contentLength;
                                }
                                
                                return;
                            }
                            
                            out.add(this.message);
                            out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                            this.resetNow();
                            return;
                    }
                } catch (Exception var10) {
                    out.add(this.invalidMessage(buffer, var10));
                    return;
                }
            case READ_CHUNK_SIZE:
                try {
                    line = this.lineParser.parse(buffer);
                    if (line == null) {
                        return;
                    }
                    
                    toRead = getChunkSize(line.toString());
                    this.chunkSize = (long)toRead;
                    if (toRead == 0) {
                        this.currentState = HttpObjectDecoderT.State.READ_CHUNK_FOOTER;
                        return;
                    }
                    
                    this.currentState = HttpObjectDecoderT.State.READ_CHUNKED_CONTENT;
                } catch (Exception var8) {
                    out.add(this.invalidChunk(buffer, var8));
                    return;
                }
            case READ_CHUNKED_CONTENT:
                assert this.chunkSize <= 2147483647L;
                
                toRead = Math.min((int)this.chunkSize, this.maxChunkSize);
                toRead = Math.min(toRead, buffer.readableBytes());
                if (toRead == 0) {
                    return;
                }
                
                HttpContent chunk = new DefaultHttpContent(buffer.readRetainedSlice(toRead));
                this.chunkSize -= (long)toRead;
                out.add(chunk);
                if (this.chunkSize != 0L) {
                    return;
                }
                
                this.currentState = HttpObjectDecoderT.State.READ_CHUNK_DELIMITER;
            case READ_CHUNK_DELIMITER:
                toRead = buffer.writerIndex();
                toRead = buffer.readerIndex();
                
                while(toRead > toRead) {
                    byte next = buffer.getByte(toRead++);
                    if (next == 10) {
                        this.currentState = HttpObjectDecoderT.State.READ_CHUNK_SIZE;
                        break;
                    }
                }
                
                buffer.readerIndex(toRead);
                return;
            case READ_VARIABLE_LENGTH_CONTENT:
                toRead = Math.min(buffer.readableBytes(), this.maxChunkSize);
                if (toRead > 0) {
                    ByteBuf content = buffer.readRetainedSlice(toRead);
                    out.add(new DefaultHttpContent(content));
                }
                
                return;
            case READ_FIXED_LENGTH_CONTENT:
                toRead = buffer.readableBytes();
                if (toRead == 0) {
                    return;
                }
                
                toRead = Math.min(toRead, this.maxChunkSize);
                if ((long)toRead > this.chunkSize) {
                    toRead = (int)this.chunkSize;
                }
                
                ByteBuf content = buffer.readRetainedSlice(toRead);
                this.chunkSize -= (long)toRead;
                if (this.chunkSize == 0L) {
                    out.add(new DefaultLastHttpContent(content, this.validateHeaders));
                    this.resetNow();
                } else {
                    out.add(new DefaultHttpContent(content));
                }
                
                return;
            case READ_CHUNK_FOOTER:
                try {
                    LastHttpContent trailer = this.readTrailingHeaders(buffer);
                    if (trailer == null) {
                        return;
                    }
                    
                    out.add(trailer);
                    this.resetNow();
                    return;
                } catch (Exception var7) {
                    out.add(this.invalidChunk(buffer, var7));
                    return;
                }
            case BAD_MESSAGE:
                buffer.skipBytes(buffer.readableBytes());
                break;
            case UPGRADED:
                toRead = buffer.readableBytes();
                if (toRead > 0) {
                    out.add(buffer.readBytes(toRead));
                }
        }
    }
    
    protected void decodeLast(ByteBuf in, List<Object> out) throws Exception {
        //to do
//        super.decodeLast(ctx, in, out);
        if (this.resetRequested) {
            this.resetNow();
        }
        
        if (this.message != null) {
            boolean chunked = HttpUtil.isTransferEncodingChunked(this.message);
            if (this.currentState == HttpObjectDecoderT.State.READ_VARIABLE_LENGTH_CONTENT && !in.isReadable() && !chunked) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                this.resetNow();
                return;
            }
            
            if (this.currentState == HttpObjectDecoderT.State.READ_HEADER) {
                out.add(this.invalidMessage(Unpooled.EMPTY_BUFFER, new PrematureChannelClosureException("Connection closed before received headers")));
                this.resetNow();
                return;
            }
            
            boolean prematureClosure;
            if (!this.isDecodingRequest() && !chunked) {
                prematureClosure = this.contentLength() > 0L;
            } else {
                prematureClosure = true;
            }
            
            if (!prematureClosure) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            }
            
            this.resetNow();
        }
        
    }
    
    public void userEventTriggered(Object evt) throws Exception {
        if (evt instanceof HttpExpectationFailedEvent) {
            switch(this.currentState) {
                case READ_CHUNK_SIZE:
                case READ_VARIABLE_LENGTH_CONTENT:
                case READ_FIXED_LENGTH_CONTENT:
                    this.reset();
                case READ_INITIAL:
                case READ_HEADER:
            }
        }
        
        //to do
//        super.userEventTriggered(ctx, evt);
    }
    
    protected boolean isContentAlwaysEmpty(HttpMessage msg) {
        if (msg instanceof HttpResponse) {
            HttpResponse res = (HttpResponse)msg;
            int code = res.status().code();
            if (code >= 100 && code < 200) {
                return code != 101 || res.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT) || !res.headers().contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true);
            }
            
            switch(code) {
                case 204:
                case 304:
                    return true;
            }
        }
        
        return false;
    }
    
    protected boolean isSwitchingToNonHttp1Protocol(HttpResponse msg) {
        if (msg.status().code() != HttpResponseStatus.SWITCHING_PROTOCOLS.code()) {
            return false;
        } else {
            String newProtocol = msg.headers().get(HttpHeaderNames.UPGRADE);
            return newProtocol == null || !newProtocol.contains(HttpVersion.HTTP_1_0.text()) && !newProtocol.contains(HttpVersion.HTTP_1_1.text());
        }
    }
    
    public void reset() {
        this.resetRequested = true;
    }
    
    private void resetNow() {
        HttpMessage message = this.message;
        this.message = null;
        this.name = null;
        this.value = null;
        this.contentLength = -9223372036854775808L;
        this.lineParser.reset();
        this.headerParser.reset();
        this.trailer = null;
        if (!this.isDecodingRequest()) {
            HttpResponse res = (HttpResponse)message;
            if (res != null && this.isSwitchingToNonHttp1Protocol(res)) {
                this.currentState = HttpObjectDecoderT.State.UPGRADED;
                return;
            }
        }
        
        this.resetRequested = false;
        this.currentState = HttpObjectDecoderT.State.SKIP_CONTROL_CHARS;
    }
    
    private HttpMessage invalidMessage(ByteBuf in, Exception cause) {
        this.currentState = HttpObjectDecoderT.State.BAD_MESSAGE;
        in.skipBytes(in.readableBytes());
        if (this.message == null) {
            this.message = this.createInvalidMessage();
        }
        
        this.message.setDecoderResult(DecoderResult.failure(cause));
        HttpMessage ret = this.message;
        this.message = null;
        return ret;
    }
    
    private HttpContent invalidChunk(ByteBuf in, Exception cause) {
        this.currentState = HttpObjectDecoderT.State.BAD_MESSAGE;
        in.skipBytes(in.readableBytes());
        HttpContent chunk = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        chunk.setDecoderResult(DecoderResult.failure(cause));
        this.message = null;
        this.trailer = null;
        return chunk;
    }
    
    private HttpObjectDecoderT.State readHeaders(ByteBuf buffer) {
        HttpMessage message = this.message;
        HttpHeaders headers = message.headers();
        AppendableCharSequence line = this.headerParser.parse(buffer);
        if (line == null) {
            return null;
        } else {
            if (line.length() > 0) {
                do {
                    char firstChar = line.charAtUnsafe(0);
                    if (this.name != null && (firstChar == ' ' || firstChar == '\t')) {
                        String trimmedLine = line.toString().trim();
                        String valueStr = String.valueOf(this.value);
                        this.value = valueStr + ' ' + trimmedLine;
                    } else {
                        if (this.name != null) {
                            headers.add(this.name, this.value);
                        }
                        
                        this.splitHeader(line);
                    }
                    
                    line = this.headerParser.parse(buffer);
                    if (line == null) {
                        return null;
                    }
                } while(line.length() > 0);
            }
            
            if (this.name != null) {
                headers.add(this.name, this.value);
            }
            
            this.name = null;
            this.value = null;
            List<String> contentLengthFields = headers.getAll(HttpHeaderNames.CONTENT_LENGTH);
            if (!contentLengthFields.isEmpty()) {
                HttpVersion version = message.protocolVersion();
                boolean isHttp10OrEarlier = version.majorVersion() < 1 || version.majorVersion() == 1 && version.minorVersion() == 0;
                this.contentLength = HttpUtil.normalizeAndGetContentLength(contentLengthFields, isHttp10OrEarlier, this.allowDuplicateContentLengths);
                if (this.contentLength != -1L) {
                    headers.set(HttpHeaderNames.CONTENT_LENGTH, this.contentLength);
                }
            }
            
            if (this.isContentAlwaysEmpty(message)) {
                HttpUtil.setTransferEncodingChunked(message, false);
                return HttpObjectDecoderT.State.SKIP_CONTROL_CHARS;
            } else if (HttpUtil.isTransferEncodingChunked(message)) {
                if (!contentLengthFields.isEmpty() && message.protocolVersion() == HttpVersion.HTTP_1_1) {
                    this.handleTransferEncodingChunkedWithContentLength(message);
                }
                
                return HttpObjectDecoderT.State.READ_CHUNK_SIZE;
            } else {
                return this.contentLength() >= 0L ? HttpObjectDecoderT.State.READ_FIXED_LENGTH_CONTENT : HttpObjectDecoderT.State.READ_VARIABLE_LENGTH_CONTENT;
            }
        }
    }
    
    protected void handleTransferEncodingChunkedWithContentLength(HttpMessage message) {
        message.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
        this.contentLength = -9223372036854775808L;
    }
    
    private long contentLength() {
        if (this.contentLength == -9223372036854775808L) {
            this.contentLength = HttpUtil.getContentLength(this.message, -1L);
        }
        
        return this.contentLength;
    }
    
    private LastHttpContent readTrailingHeaders(ByteBuf buffer) {
        AppendableCharSequence line = this.headerParser.parse(buffer);
        if (line == null) {
            return null;
        } else {
            LastHttpContent trailer = this.trailer;
            if (line.length() == 0 && trailer == null) {
                return LastHttpContent.EMPTY_LAST_CONTENT;
            } else {
                CharSequence lastHeader = null;
                if (trailer == null) {
                    trailer = this.trailer = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
                }
                
                do {
                    if (line.length() <= 0) {
                        this.trailer = null;
                        return trailer;
                    }
                    
                    char firstChar = line.charAtUnsafe(0);
                    if (lastHeader != null && (firstChar == ' ' || firstChar == '\t')) {
                        List<String> current = trailer.trailingHeaders().getAll(lastHeader);
                        if (!current.isEmpty()) {
                            int lastPos = current.size() - 1;
                            String lineTrimmed = line.toString().trim();
                            String currentLastPos = (String)current.get(lastPos);
                            current.set(lastPos, currentLastPos + lineTrimmed);
                        }
                    } else {
                        this.splitHeader(line);
                        CharSequence headerName = this.name;
                        if (!HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(headerName) && !HttpHeaderNames.TRANSFER_ENCODING.contentEqualsIgnoreCase(headerName) && !HttpHeaderNames.TRAILER.contentEqualsIgnoreCase(headerName)) {
                            trailer.trailingHeaders().add(headerName, this.value);
                        }
                        
                        lastHeader = this.name;
                        this.name = null;
                        this.value = null;
                    }
                    
                    line = this.headerParser.parse(buffer);
                } while(line != null);
                
                return null;
            }
        }
    }
    
    protected abstract boolean isDecodingRequest();
    
    protected abstract HttpMessage createMessage(String[] var1) throws Exception;
    
    protected abstract HttpMessage createInvalidMessage();
    
    private static int getChunkSize(String hex) {
        hex = hex.trim();
        
        for(int i = 0; i < hex.length(); ++i) {
            char c = hex.charAt(i);
            if (c == ';' || Character.isWhitespace(c) || Character.isISOControl(c)) {
                hex = hex.substring(0, i);
                break;
            }
        }
        
        return Integer.parseInt(hex, 16);
    }
    
    private static String[] splitInitialLine(AppendableCharSequence sb) {
        int aStart = findNonSPLenient(sb, 0);
        int aEnd = findSPLenient(sb, aStart);
        int bStart = findNonSPLenient(sb, aEnd);
        int bEnd = findSPLenient(sb, bStart);
        int cStart = findNonSPLenient(sb, bEnd);
        int cEnd = findEndOfString(sb);
        return new String[]{sb.subStringUnsafe(aStart, aEnd), sb.subStringUnsafe(bStart, bEnd), cStart < cEnd ? sb.subStringUnsafe(cStart, cEnd) : ""};
    }
    
    private void splitHeader(AppendableCharSequence sb) {
        int length = sb.length();
        int nameStart = findNonWhitespace(sb, 0, false);
        
        int nameEnd;
        for(nameEnd = nameStart; nameEnd < length; ++nameEnd) {
            char ch = sb.charAtUnsafe(nameEnd);
            if (ch == ':' || !this.isDecodingRequest() && isOWS(ch)) {
                break;
            }
        }
        
        if (nameEnd == length) {
            throw new IllegalArgumentException("No colon found");
        } else {
            int colonEnd;
            for(colonEnd = nameEnd; colonEnd < length; ++colonEnd) {
                if (sb.charAtUnsafe(colonEnd) == ':') {
                    ++colonEnd;
                    break;
                }
            }
            
            this.name = sb.subStringUnsafe(nameStart, nameEnd);
            int valueStart = findNonWhitespace(sb, colonEnd, true);
            if (valueStart == length) {
                this.value = "";
            } else {
                int valueEnd = findEndOfString(sb);
                this.value = sb.subStringUnsafe(valueStart, valueEnd);
            }
            
        }
    }
    
    private static int findNonSPLenient(AppendableCharSequence sb, int offset) {
        for(int result = offset; result < sb.length(); ++result) {
            char c = sb.charAtUnsafe(result);
            if (!isSPLenient(c)) {
                if (Character.isWhitespace(c)) {
                    throw new IllegalArgumentException("Invalid separator");
                }
                
                return result;
            }
        }
        
        return sb.length();
    }
    
    private static int findSPLenient(AppendableCharSequence sb, int offset) {
        for(int result = offset; result < sb.length(); ++result) {
            if (isSPLenient(sb.charAtUnsafe(result))) {
                return result;
            }
        }
        
        return sb.length();
    }
    
    private static boolean isSPLenient(char c) {
        return c == ' ' || c == '\t' || c == 11 || c == '\f' || c == '\r';
    }
    
    private static int findNonWhitespace(AppendableCharSequence sb, int offset, boolean validateOWS) {
        for(int result = offset; result < sb.length(); ++result) {
            char c = sb.charAtUnsafe(result);
            if (!Character.isWhitespace(c)) {
                return result;
            }
            
            if (validateOWS && !isOWS(c)) {
                throw new IllegalArgumentException("Invalid separator, only a single space or horizontal tab allowed, but received a '" + c + "' (0x" + Integer.toHexString(c) + ")");
            }
        }
        
        return sb.length();
    }
    
    private static int findEndOfString(AppendableCharSequence sb) {
        for(int result = sb.length() - 1; result > 0; --result) {
            if (!Character.isWhitespace(sb.charAtUnsafe(result))) {
                return result + 1;
            }
        }
        
        return 0;
    }
    
    private static boolean isOWS(char ch) {
        return ch == ' ' || ch == '\t';
    }
    
    private final class LineParser extends HttpObjectDecoderT.HeaderParser {
        LineParser(AppendableCharSequence seq, int maxLength) {
            super(seq, maxLength);
        }
        
        public AppendableCharSequence parse(ByteBuf buffer) {
            this.reset();
            return super.parse(buffer);
        }
        
        public boolean process(byte value) throws Exception {
            if (HttpObjectDecoderT.this.currentState == HttpObjectDecoderT.State.SKIP_CONTROL_CHARS) {
                char c = (char)(value & 255);
                if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                    this.increaseCount();
                    return true;
                }
                
                HttpObjectDecoderT.this.currentState = HttpObjectDecoderT.State.READ_INITIAL;
            }
            
            return super.process(value);
        }
        
        protected TooLongFrameException newException(int maxLength) {
            return new TooLongFrameException("An HTTP line is larger than " + maxLength + " bytes.");
        }
    }
    
    private static class HeaderParser implements ByteProcessor
    {
        private final AppendableCharSequence seq;
        private final int maxLength;
        private int size;
        
        HeaderParser(AppendableCharSequence seq, int maxLength) {
            this.seq = seq;
            this.maxLength = maxLength;
        }
        
        public AppendableCharSequence parse(ByteBuf buffer) {
            int oldSize = this.size;
            this.seq.reset();
            int i = buffer.forEachByte(this);
            if (i == -1) {
                this.size = oldSize;
                return null;
            } else {
                buffer.readerIndex(i + 1);
                return this.seq;
            }
        }
        
        public void reset() {
            this.size = 0;
        }
        
        public boolean process(byte value) throws Exception {
            char nextByte = (char)(value & 255);
            if (nextByte == '\n') {
                int len = this.seq.length();
                if (len >= 1 && this.seq.charAtUnsafe(len - 1) == '\r') {
                    --this.size;
                    this.seq.setLength(len - 1);
                }
                
                return false;
            } else {
                this.increaseCount();
                this.seq.append(nextByte);
                return true;
            }
        }
        
        protected final void increaseCount() {
            if (++this.size > this.maxLength) {
                throw this.newException(this.maxLength);
            }
        }
        
        protected TooLongFrameException newException(int maxLength) {
            return new TooLongFrameException("HTTP header is larger than " + maxLength + " bytes.");
        }
    }
    
    private static enum State {
        SKIP_CONTROL_CHARS,
        READ_INITIAL,
        READ_HEADER,
        READ_VARIABLE_LENGTH_CONTENT,
        READ_FIXED_LENGTH_CONTENT,
        READ_CHUNK_SIZE,
        READ_CHUNKED_CONTENT,
        READ_CHUNK_DELIMITER,
        READ_CHUNK_FOOTER,
        BAD_MESSAGE,
        UPGRADED;
        
        private State() {
        }
    }
}
