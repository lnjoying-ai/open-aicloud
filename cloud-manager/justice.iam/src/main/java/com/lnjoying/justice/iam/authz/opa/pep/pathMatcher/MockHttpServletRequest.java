package com.lnjoying.justice.iam.authz.opa.pep.pathMatcher;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/22 21:06
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

public class MockHttpServletRequest implements HttpServletRequest {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String CHARSET_PREFIX = "charset=";
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final BufferedReader EMPTY_BUFFERED_READER = new BufferedReader(new StringReader(""));
    private static final String[] DATE_FORMATS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy"};
    public static final String DEFAULT_PROTOCOL = "HTTP/1.1";
    public static final String DEFAULT_SCHEME = "http";
    public static final String DEFAULT_SERVER_ADDR = "127.0.0.1";
    public static final String DEFAULT_SERVER_NAME = "localhost";
    public static final int DEFAULT_SERVER_PORT = 80;
    public static final String DEFAULT_REMOTE_ADDR = "127.0.0.1";
    public static final String DEFAULT_REMOTE_HOST = "localhost";
    private final ServletContext servletContext;
    private boolean active;
    private final Map<String, Object> attributes;
    @Nullable
    private String characterEncoding;
    @Nullable
    private byte[] content;
    @Nullable
    private String contentType;
    @Nullable
    private ServletInputStream inputStream;
    @Nullable
    private BufferedReader reader;
    private final Map<String, String[]> parameters;
    private String protocol;
    private String scheme;
    private String serverName;
    private int serverPort;
    private String remoteAddr;
    private String remoteHost;
    private final LinkedList<Locale> locales;
    private boolean secure;
    private int remotePort;
    private String localName;
    private String localAddr;
    private int localPort;
    private boolean asyncStarted;
    private boolean asyncSupported;

    private DispatcherType dispatcherType;
    @Nullable
    private String authType;
    @Nullable
    private Cookie[] cookies;
    private final Map<String, HeaderValueHolder> headers;
    @Nullable
    private String method;
    @Nullable
    private String pathInfo;
    private String contextPath;
    @Nullable
    private String queryString;
    @Nullable
    private String remoteUser;
    private final Set<String> userRoles;
    @Nullable
    private Principal userPrincipal;
    @Nullable
    private String requestedSessionId;
    @Nullable
    private String requestURI;
    private String servletPath;
    @Nullable
    private HttpSession session;
    private boolean requestedSessionIdValid;
    private boolean requestedSessionIdFromCookie;
    private boolean requestedSessionIdFromURL;
    private final MultiValueMap<String, Part> parts;

    public MockHttpServletRequest() {
        this((ServletContext)null, "", "");
    }

    public MockHttpServletRequest(@Nullable String method, @Nullable String requestURI) {
        this((ServletContext)null, method, requestURI);
    }

    public MockHttpServletRequest(@Nullable ServletContext servletContext) {
        this(servletContext, "", "");
    }

    public MockHttpServletRequest(@Nullable ServletContext servletContext, @Nullable String method, @Nullable String requestURI) {
        this.active = true;
        this.attributes = new LinkedHashMap();
        this.parameters = new LinkedHashMap(16);
        this.protocol = "HTTP/1.1";
        this.scheme = "http";
        this.serverName = "localhost";
        this.serverPort = 80;
        this.remoteAddr = "127.0.0.1";
        this.remoteHost = "localhost";
        this.locales = new LinkedList();
        this.secure = false;
        this.remotePort = 80;
        this.localName = "localhost";
        this.localAddr = "127.0.0.1";
        this.localPort = 80;
        this.asyncStarted = false;
        this.asyncSupported = false;
        this.dispatcherType = DispatcherType.REQUEST;
        this.headers = new LinkedCaseInsensitiveMap();
        this.contextPath = "";
        this.userRoles = new HashSet();
        this.servletPath = "";
        this.requestedSessionIdValid = true;
        this.requestedSessionIdFromCookie = true;
        this.requestedSessionIdFromURL = false;
        this.parts = new LinkedMultiValueMap();
        this.servletContext = (ServletContext)(servletContext != null ? servletContext : new MockServletContext());
        this.method = method;
        this.requestURI = requestURI;
        this.locales.add(Locale.ENGLISH);
    }

    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException
    {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException
    {
        return null;
    }

    public boolean isActive() {
        return this.active;
    }

    public void close() {
        this.active = false;
    }

    public void invalidate() {
        this.close();
        this.clearAttributes();
    }

    protected void checkActive() throws IllegalStateException {
        Assert.state(this.active, "Request is not active anymore");
    }

    public Object getAttribute(String name) {
        this.checkActive();
        return this.attributes.get(name);
    }

    public Enumeration<String> getAttributeNames() {
        this.checkActive();
        return Collections.enumeration(new LinkedHashSet(this.attributes.keySet()));
    }

    @Nullable
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    public void setCharacterEncoding(@Nullable String characterEncoding) {
        this.characterEncoding = characterEncoding;
        this.updateContentTypeHeader();
    }

    private void updateContentTypeHeader() {
        if (StringUtils.hasLength(this.contentType)) {
            String value = this.contentType;
            if (StringUtils.hasLength(this.characterEncoding) && !this.contentType.toLowerCase().contains("charset=")) {
                value = value + ";charset=" + this.characterEncoding;
            }

            this.doAddHeaderValue("Content-Type", value, true);
        }

    }

    public void setContent(@Nullable byte[] content) {
        this.content = content;
        this.inputStream = null;
        this.reader = null;
    }

    @Nullable
    public byte[] getContentAsByteArray() {
        return this.content;
    }

    @Nullable
    public String getContentAsString() throws IllegalStateException, UnsupportedEncodingException {
        Assert.state(this.characterEncoding != null, "Cannot get content as a String for a null character encoding. Consider setting the characterEncoding in the request.");
        return this.content == null ? null : new String(this.content, this.characterEncoding);
    }

    public int getContentLength() {
        return this.content != null ? this.content.length : -1;
    }

    public long getContentLengthLong() {
        return (long)this.getContentLength();
    }

    public void setContentType(@Nullable String contentType) {
        this.contentType = contentType;
        if (contentType != null) {
            try {
                MediaType mediaType = MediaType.parseMediaType(contentType);
                if (mediaType.getCharset() != null) {
                    this.characterEncoding = mediaType.getCharset().name();
                }
            } catch (IllegalArgumentException var4) {
                int charsetIndex = contentType.toLowerCase().indexOf("charset=");
                if (charsetIndex != -1) {
                    this.characterEncoding = contentType.substring(charsetIndex + "charset=".length());
                }
            }

            this.updateContentTypeHeader();
        }

    }

    @Nullable
    public String getContentType() {
        return this.contentType;
    }

    public ServletInputStream getInputStream() {
        if (this.inputStream != null) {
            return this.inputStream;
        } else if (this.reader != null) {
            throw new IllegalStateException("Cannot call getInputStream() after getReader() has already been called for the current request");
        } else {
            throw new IllegalStateException("Cannot call getInputStream() after getReader() has already been called for the current request");
        }
    }

    public void setParameter(String name, String value) {
        this.setParameter(name, value);
    }

    public void setParameter(String name, String... values) {
        Assert.notNull(name, "Parameter name must not be null");
        this.parameters.put(name, values);
    }

    public void setParameters(Map<String, ?> params) {
        Assert.notNull(params, "Parameter map must not be null");
        params.forEach((key, value) -> {
            if (value instanceof String) {
                this.setParameter(key, (String)value);
            } else {
                if (!(value instanceof String[])) {
                    throw new IllegalArgumentException("Parameter map value must be single value  or array of type [" + String.class.getName() + "]");
                }

                this.setParameter(key, (String[])((String[])value));
            }

        });
    }

    public void addParameter(String name, @Nullable String value) {
        this.addParameter(name, value);
    }

    public void addParameter(String name, String... values) {
        Assert.notNull(name, "Parameter name must not be null");
        String[] oldArr = (String[])this.parameters.get(name);
        if (oldArr != null) {
            String[] newArr = new String[oldArr.length + values.length];
            System.arraycopy(oldArr, 0, newArr, 0, oldArr.length);
            System.arraycopy(values, 0, newArr, oldArr.length, values.length);
            this.parameters.put(name, newArr);
        } else {
            this.parameters.put(name, values);
        }

    }

    public void addParameters(Map<String, ?> params) {
        Assert.notNull(params, "Parameter map must not be null");
        params.forEach((key, value) -> {
            if (value instanceof String) {
                this.addParameter(key, (String)value);
            } else {
                if (!(value instanceof String[])) {
                    throw new IllegalArgumentException("Parameter map value must be single value  or array of type [" + String.class.getName() + "]");
                }

                this.addParameter(key, (String[])((String[])value));
            }

        });
    }

    public void removeParameter(String name) {
        Assert.notNull(name, "Parameter name must not be null");
        this.parameters.remove(name);
    }

    public void removeAllParameters() {
        this.parameters.clear();
    }

    @Nullable
    public String getParameter(String name) {
        Assert.notNull(name, "Parameter name must not be null");
        String[] arr = (String[])this.parameters.get(name);
        return arr != null && arr.length > 0 ? arr[0] : null;
    }

    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(this.parameters.keySet());
    }

    public String[] getParameterValues(String name) {
        Assert.notNull(name, "Parameter name must not be null");
        return (String[])this.parameters.get(name);
    }

    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(this.parameters);
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        String rawHostHeader = this.getHeader("Host");
        if (rawHostHeader != null) {
            String host = rawHostHeader.trim();
            if (host.startsWith("[")) {
                int indexOfClosingBracket = host.indexOf(93);
                Assert.state(indexOfClosingBracket > -1, () -> {
                    return "Invalid Host header: " + rawHostHeader;
                });
                host = host.substring(0, indexOfClosingBracket + 1);
            } else if (host.contains(":")) {
                host = host.substring(0, host.indexOf(58));
            }

            return host;
        } else {
            return this.serverName;
        }
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        String rawHostHeader = this.getHeader("Host");
        if (rawHostHeader != null) {
            String host = rawHostHeader.trim();
            int idx;
            if (host.startsWith("[")) {
                int indexOfClosingBracket = host.indexOf(93);
                Assert.state(indexOfClosingBracket > -1, () -> {
                    return "Invalid Host header: " + rawHostHeader;
                });
                idx = host.indexOf(58, indexOfClosingBracket);
            } else {
                idx = host.indexOf(58);
            }

            if (idx != -1) {
                return Integer.parseInt(host.substring(idx + 1));
            }
        }

        return this.serverPort;
    }

    public BufferedReader getReader() throws UnsupportedEncodingException {
        if (this.reader != null) {
            return this.reader;
        } else if (this.inputStream != null) {
            throw new IllegalStateException("Cannot call getReader() after getInputStream() has already been called for the current request");
        } else {
            if (this.content != null) {
                InputStream sourceStream = new ByteArrayInputStream(this.content);
                Reader sourceReader = this.characterEncoding != null ? new InputStreamReader(sourceStream, this.characterEncoding) : new InputStreamReader(sourceStream);
                this.reader = new BufferedReader(sourceReader);
            } else {
                this.reader = EMPTY_BUFFERED_READER;
            }

            return this.reader;
        }
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getRemoteHost() {
        return this.remoteHost;
    }

    public void setAttribute(String name, @Nullable Object value) {
        this.checkActive();
        Assert.notNull(name, "Attribute name must not be null");
        if (value != null) {
            this.attributes.put(name, value);
        } else {
            this.attributes.remove(name);
        }

    }

    public void removeAttribute(String name) {
        this.checkActive();
        Assert.notNull(name, "Attribute name must not be null");
        this.attributes.remove(name);
    }

    public void clearAttributes() {
        this.attributes.clear();
    }

    public void addPreferredLocale(Locale locale) {
        Assert.notNull(locale, "Locale must not be null");
        this.locales.addFirst(locale);
        this.updateAcceptLanguageHeader();
    }

    public void setPreferredLocales(List<Locale> locales) {
        Assert.notEmpty(locales, "Locale list must not be empty");
        this.locales.clear();
        this.locales.addAll(locales);
        this.updateAcceptLanguageHeader();
    }

    private void updateAcceptLanguageHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(this.locales);
        this.doAddHeaderValue("Accept-Language", headers.getFirst("Accept-Language"), true);
    }

    public Locale getLocale() {
        return (Locale)this.locales.getFirst();
    }

    public Enumeration<Locale> getLocales() {
        return Collections.enumeration(this.locales);
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isSecure() {
        return this.secure || "https".equalsIgnoreCase(this.scheme);
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s)
    {
        return null;
    }


    /** @deprecated */
    @Deprecated
    public String getRealPath(String path) {
        return this.servletContext.getRealPath(path);
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public int getRemotePort() {
        return this.remotePort;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getLocalName() {
        return this.localName;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public String getLocalAddr() {
        return this.localAddr;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public int getLocalPort() {
        return this.localPort;
    }


    public void setAsyncStarted(boolean asyncStarted) {
        this.asyncStarted = asyncStarted;
    }

    public boolean isAsyncStarted() {
        return this.asyncStarted;
    }

    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    public boolean isAsyncSupported() {
        return this.asyncSupported;
    }

    @Override
    public AsyncContext getAsyncContext()
    {
        return null;
    }


    public void setDispatcherType(DispatcherType dispatcherType) {
        this.dispatcherType = dispatcherType;
    }

    public DispatcherType getDispatcherType() {
        return this.dispatcherType;
    }

    public void setAuthType(@Nullable String authType) {
        this.authType = authType;
    }

    @Nullable
    public String getAuthType() {
        return this.authType;
    }

    public void setCookies(@Nullable Cookie... cookies) {
        this.cookies = ObjectUtils.isEmpty(cookies) ? null : cookies;
        if (this.cookies == null) {
            this.removeHeader("Cookie");
        } else {
            this.doAddHeaderValue("Cookie", encodeCookies(this.cookies), true);
        }

    }

    private static String encodeCookies(@NonNull Cookie... cookies) {
        return (String)Arrays.stream(cookies).map((c) -> {
            return c.getName() + '=' + (c.getValue() == null ? "" : c.getValue());
        }).collect(Collectors.joining("; "));
    }

    @Nullable
    public Cookie[] getCookies() {
        return this.cookies;
    }

    public void addHeader(String name, Object value) {
        if ("Content-Type".equalsIgnoreCase(name) && !this.headers.containsKey("Content-Type")) {
            this.setContentType(value.toString());
        } else if ("Accept-Language".equalsIgnoreCase(name) && !this.headers.containsKey("Accept-Language")) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Accept-Language", value.toString());
                List<Locale> locales = headers.getAcceptLanguageAsLocales();
                this.locales.clear();
                this.locales.addAll(locales);
                if (this.locales.isEmpty()) {
                    this.locales.add(Locale.ENGLISH);
                }
            } catch (IllegalArgumentException var5) {
            }

            this.doAddHeaderValue(name, value, true);
        } else {
            this.doAddHeaderValue(name, value, false);
        }

    }

    private void doAddHeaderValue(String name, @Nullable Object value, boolean replace) {
        HeaderValueHolder header = (HeaderValueHolder)this.headers.get(name);
        Assert.notNull(value, "Header value must not be null");
        if (header == null || replace) {
            header = new HeaderValueHolder();
            this.headers.put(name, header);
        }

        if (value instanceof Collection) {
            header.addValues((Collection)value);
        } else if (value.getClass().isArray()) {
            header.addValueArray(value);
        } else {
            header.addValue(value);
        }

    }

    public void removeHeader(String name) {
        Assert.notNull(name, "Header name must not be null");
        this.headers.remove(name);
    }

    public long getDateHeader(String name) {
        HeaderValueHolder header = (HeaderValueHolder)this.headers.get(name);
        Object value = header != null ? header.getValue() : null;
        if (value instanceof Date) {
            return ((Date)value).getTime();
        } else if (value instanceof Number) {
            return ((Number)value).longValue();
        } else if (value instanceof String) {
            return this.parseDateHeader(name, (String)value);
        } else if (value != null) {
            throw new IllegalArgumentException("Value for header '" + name + "' is not a Date, Number, or String: " + value);
        } else {
            return -1L;
        }
    }

    private long parseDateHeader(String name, String value) {
        String[] var3 = DATE_FORMATS;
        int var4 = var3.length;
        int var5 = 0;

        while(var5 < var4) {
            String dateFormat = var3[var5];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
            simpleDateFormat.setTimeZone(GMT);

            try {
                return simpleDateFormat.parse(value).getTime();
            } catch (ParseException var9) {
                ++var5;
            }
        }

        throw new IllegalArgumentException("Cannot parse date value '" + value + "' for '" + name + "' header");
    }

    @Nullable
    public String getHeader(String name) {
        HeaderValueHolder header = (HeaderValueHolder)this.headers.get(name);
        return header != null ? header.getStringValue() : null;
    }

    public Enumeration<String> getHeaders(String name) {
        HeaderValueHolder header = (HeaderValueHolder)this.headers.get(name);
        return Collections.enumeration((Collection)(header != null ? header.getStringValues() : new LinkedList()));
    }

    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(this.headers.keySet());
    }

    public int getIntHeader(String name) {
        HeaderValueHolder header = (HeaderValueHolder)this.headers.get(name);
        Object value = header != null ? header.getValue() : null;
        if (value instanceof Number) {
            return ((Number)value).intValue();
        } else if (value instanceof String) {
            return Integer.parseInt((String)value);
        } else if (value != null) {
            throw new NumberFormatException("Value for header '" + name + "' is not a Number: " + value);
        } else {
            return -1;
        }
    }

    public void setMethod(@Nullable String method) {
        this.method = method;
    }

    @Nullable
    public String getMethod() {
        return this.method;
    }

    public void setPathInfo(@Nullable String pathInfo) {
        this.pathInfo = pathInfo;
    }

    @Nullable
    public String getPathInfo() {
        return this.pathInfo;
    }

    @Nullable
    public String getPathTranslated() {
        return this.pathInfo != null ? this.getRealPath(this.pathInfo) : null;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getContextPath() {
        return this.contextPath;
    }

    public void setQueryString(@Nullable String queryString) {
        this.queryString = queryString;
    }

    @Nullable
    public String getQueryString() {
        return this.queryString;
    }

    public void setRemoteUser(@Nullable String remoteUser) {
        this.remoteUser = remoteUser;
    }

    @Nullable
    public String getRemoteUser() {
        return this.remoteUser;
    }

    @Override
    public boolean isUserInRole(String s)
    {
        return false;
    }

    public void addUserRole(String role) {
        this.userRoles.add(role);
    }


    public void setUserPrincipal(@Nullable Principal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    @Nullable
    public Principal getUserPrincipal() {
        return this.userPrincipal;
    }

    public void setRequestedSessionId(@Nullable String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    @Nullable
    public String getRequestedSessionId() {
        return this.requestedSessionId;
    }

    public void setRequestURI(@Nullable String requestURI) {
        this.requestURI = requestURI;
    }

    @Nullable
    public String getRequestURI() {
        return this.requestURI;
    }

    public StringBuffer getRequestURL() {
        String scheme = this.getScheme();
        String server = this.getServerName();
        int port = this.getServerPort();
        String uri = this.getRequestURI();
        StringBuffer url = (new StringBuffer(scheme)).append("://").append(server);
        if (port > 0 && ("http".equalsIgnoreCase(scheme) && port != 80 || "https".equalsIgnoreCase(scheme) && port != 443)) {
            url.append(':').append(port);
        }

        if (StringUtils.hasText(uri)) {
            url.append(uri);
        }

        return url;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public String getServletPath() {
        return this.servletPath;
    }

    @Override
    public HttpSession getSession(boolean b)
    {
        return null;
    }


    @Nullable
    public HttpSession getSession() {
        return this.getSession(true);
    }

    @Override
    public String changeSessionId()
    {
        return null;
    }


    public void setRequestedSessionIdValid(boolean requestedSessionIdValid) {
        this.requestedSessionIdValid = requestedSessionIdValid;
    }

    public boolean isRequestedSessionIdValid() {
        return this.requestedSessionIdValid;
    }

    public void setRequestedSessionIdFromCookie(boolean requestedSessionIdFromCookie) {
        this.requestedSessionIdFromCookie = requestedSessionIdFromCookie;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return this.requestedSessionIdFromCookie;
    }

    public void setRequestedSessionIdFromURL(boolean requestedSessionIdFromURL) {
        this.requestedSessionIdFromURL = requestedSessionIdFromURL;
    }

    public boolean isRequestedSessionIdFromURL() {
        return this.requestedSessionIdFromURL;
    }

    /** @deprecated */
    @Deprecated
    public boolean isRequestedSessionIdFromUrl() {
        return this.isRequestedSessionIdFromURL();
    }

    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    public void login(String username, String password) throws ServletException {
        throw new UnsupportedOperationException();
    }

    public void logout() throws ServletException {
        this.userPrincipal = null;
        this.remoteUser = null;
        this.authType = null;
    }

    public void addPart(Part part) {
        this.parts.add(part.getName(), part);
    }

    @Nullable
    public Part getPart(String name) throws IOException, ServletException {
        return (Part)this.parts.getFirst(name);
    }

    public Collection<Part> getParts() throws IOException, ServletException {
        List<Part> result = new LinkedList();
        Iterator var2 = this.parts.values().iterator();

        while(var2.hasNext()) {
            List<Part> list = (List)var2.next();
            result.addAll(list);
        }

        return result;
    }

    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }
}
