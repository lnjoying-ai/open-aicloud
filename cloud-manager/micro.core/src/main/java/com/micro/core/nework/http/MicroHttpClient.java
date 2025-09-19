package com.micro.core.nework.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.micro.core.common.Utils;
import io.vertx.core.json.Json;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MicroHttpClient
{
    private static Logger LOGGER = LogManager.getLogger();
    private static PoolingHttpClientConnectionManager connectionManager = null;
    private static HttpClientBuilder httpBulder = null;
    private static RequestConfig requestConfig = null;

    private static int MAXCONNECTION = 10;

    private static int DEFAULTMAXCONNECTION = 5;

    private  String IP ;
    private  int PORT;


    public MicroHttpClient(String host, int port)
    {
        //设置http的状态参数
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();

        this.IP = host;
        this.PORT = port;
        HttpHost target = new HttpHost(IP, PORT);
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAXCONNECTION);
        connectionManager.setDefaultMaxPerRoute(DEFAULTMAXCONNECTION);
        connectionManager.setMaxPerRoute(new HttpRoute(target), 20);
        httpBulder = HttpClients.custom();
        httpBulder.setConnectionManager(connectionManager);
    }

    public static CloseableHttpClient getConnection()
    {
        CloseableHttpClient httpClient =  httpBulder.build();
        return httpClient;
    }


    public final String postMethodJson(String url, Map var2) throws Exception
    {
        HttpClient client = getConnection();
        HttpRequestBase clientMethod = createRequest("POST", url);

        String message = new String(Json.encode(var2).getBytes(),"utf-8");
        HttpEntity requestEntity = new StringEntity(message,"UTF-8");
        ((HttpEntityEnclosingRequestBase)clientMethod).setEntity(requestEntity);
        clientMethod.addHeader("Content-Type", "application/json; charset=UTF-8");
        clientMethod.addHeader("User-Agent", "Mozilla/4.0");
        String ss = clientMethod.toString();

        String rsp;
        try
        {
            HttpResponse response = client.execute(clientMethod);
            int status = response.getStatusLine().getStatusCode();
            if(status == 301 || status == 302)
            {
                Header  header;
                if((header = response.getFirstHeader("location")) != null) {
                    rsp = header.getValue();
                    LOGGER.info("The page was redirected to: {}", rsp);
                } else {
                    LOGGER.info("Location field value is null.");
                }
            }

            HttpEntity entity =  response.getEntity();
            if (null == entity)
            {
                return null;
            }
            rsp = EntityUtils.toString(entity, "utf-8");
        } catch (UnknownHostException var8) {
            LOGGER.error("UnknownHostException" + var8);
            throw var8;
        } catch (IOException var9) {
            LOGGER.error("IOException" + var9);
            throw var9;
        } catch (Exception var10) {
            LOGGER.error("Exception" + var10);
            throw var10;
        } finally {
            clientMethod.releaseConnection();
        }

        return rsp;
    }

    public final String getMethodJson(String url, List<Header> headers, Map var2) throws Exception
    {
        HttpClient client = getConnection();
        HttpRequestBase clientMethod = createRequest("GET", url);

        clientMethod.addHeader("Content-Type", "application/json; charset=UTF-8");
//        clientMethod.addHeader("User-Agent", "Mozilla/5.0");
        if (headers != null)
        {
            for (Header header:headers)
            {
                clientMethod.addHeader(header);
            }
        }

        String rsp;
        try
        {
            HttpResponse response = client.execute(clientMethod);
            int status = response.getStatusLine().getStatusCode();
            if(status == 301 || status == 302)
            {
                Header  header;
                if((header = response.getFirstHeader("location")) != null) {
                    rsp = header.getValue();
                    LOGGER.info("The page was redirected to: {}", rsp);
                } else {
                    LOGGER.info("Location field value is null.");
                }
            }

            HttpEntity entity =  response.getEntity();
            if (null == entity)
            {
                return null;
            }
            rsp = EntityUtils.toString(entity, "utf-8");
            System.out.println(rsp);
        } catch (UnknownHostException var8) {
            LOGGER.error("UnknownHostException" + var8);
            throw var8;
        } catch (IOException var9) {
            LOGGER.error("IOException" + var9);
            throw var9;
        } catch (Exception var10) {
            LOGGER.error("Exception" + var10);
            throw var10;
        } finally {
            clientMethod.releaseConnection();
        }

        return rsp;
    }


    public HttpFwdRet fwd(String method, String url, List<Header> headers, InputStream stream)
    {
        clientMethod.setConfig(requestConfig);
        for(Header header:headers)
        {
            clientMethod.addHeader(header);
        }

        if (clientMethod instanceof HttpEntityEnclosingRequestBase)
        {
            HttpEntity requestEntity = new InputStreamEntity(stream);

            clientMethod.removeHeaders("Content-Length");
            Header content_type = clientMethod.getFirstHeader("content-type");
            clientMethod.removeHeaders("content-type");
            ((InputStreamEntity) requestEntity).setContentType(content_type);
            ((HttpEntityEnclosingRequestBase)clientMethod).setEntity(requestEntity);

        }

        try
        {
            HttpFwdRet fwdRet = new HttpFwdRet();
            HttpResponse response = client.execute(clientMethod);
            fwdRet.setStatus(response.getStatusLine().getStatusCode());
            fwdRet.setHeaders(response.getAllHeaders());
            if (response.getEntity().isChunked())
            {
                System.out.println("chunked");
            }

            if (response.getEntity().isStreaming())
            {
                System.out.println("streaming");
            }

            if (response.getEntity().isRepeatable())
            {
                System.out.println("repeatable");
            }
            fwdRet.setIStream(response.getEntity().getContent());

            return fwdRet;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
        }
        return null;
    }

    public HttpFwdRet fwdReq(List<Header> headers)
    {
        for(Header header:headers)
        {
            clientMethod.addHeader(header);
        }

        if (clientMethod instanceof HttpEntityEnclosingRequestBase)
        {
            clientMethod.removeHeaders("Content-Length");
            Header content_type = clientMethod.getFirstHeader("content-type");
        }

        try
        {
            HttpFwdRet fwdRet = new HttpFwdRet();
            HttpResponse response = client.execute(clientMethod);
            fwdRet.setStatus(response.getStatusLine().getStatusCode());
            fwdRet.setHeaders(response.getAllHeaders());
            if (response.getEntity().isChunked())
            {
                System.out.println("chunked");
            }

            if (response.getEntity().isStreaming())
            {
                System.out.println("streaming");
            }

            if (response.getEntity().isRepeatable())
            {
                System.out.println("repeatable");
            }
            fwdRet.setIStream(response.getEntity().getContent());
            return fwdRet;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    HttpRequestBase clientMethod = null;
    HttpClient client =null;

    public void createClient(String method, String url)
    {
        LOGGER.info("fwd message to url {}", url);
        client = getConnection();

        clientMethod = createRequest(method, url);
        clientMethod.setConfig(requestConfig);
    }

    public void destroyClient()
    {
        clientMethod.releaseConnection();
    }
    public HttpFwdRet fwdContent( List<Header> headers, InputStream stream)
    {

        for(Header header:headers)
        {
            clientMethod.addHeader(header);
        }

        if (clientMethod instanceof HttpEntityEnclosingRequestBase)
        {
            if (stream != null)
            {
                HttpEntity requestEntity = new InputStreamEntity(stream);

                clientMethod.removeHeaders("Content-Length");
                Header content_type = clientMethod.getFirstHeader("content-type");
                clientMethod.removeHeaders("content-type");
                ((InputStreamEntity) requestEntity).setContentType(content_type);
                ((HttpEntityEnclosingRequestBase)clientMethod).setEntity(requestEntity);
            }
        }

        try
        {
            HttpFwdRet fwdRet = new HttpFwdRet();
            HttpResponse response = client.execute(clientMethod);
            fwdRet.setStatus(response.getStatusLine().getStatusCode());
            fwdRet.setHeaders(response.getAllHeaders());
            if (response.getEntity().isChunked())
            {
                System.out.println("chunked");
            }

            if (response.getEntity().isStreaming())
            {
                System.out.println("streaming");
            }

            if (response.getEntity().isRepeatable())
            {
                System.out.println("repeatable");
            }
            fwdRet.setIStream(response.getEntity().getContent());
            return fwdRet;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    HttpRequestBase createRequest(String method, String url)
    {
        if (method.equals("GET"))
        {
            return new HttpGet(url);
        }

        if (method.equals("POST"))
        {
            return new HttpPost(url);
        }

        if (method.equals("PUT"))
        {
            return new HttpPut(url);
        }

        if (method.equals("DELETE"))
        {
            return new HttpDelete(url);
        }

        if (method.equals("OPTIONS"))
        {
            return new HttpOptions(url);
        }

        if (method.equals("HEAD"))
        {
            return new HttpHead(url);
        }
        return null;
    }
}
