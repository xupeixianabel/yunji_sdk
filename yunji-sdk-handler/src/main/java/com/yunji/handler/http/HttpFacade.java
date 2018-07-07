package com.yunji.handler.http;

import java.io.File;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import com.yunji.handler.http.entity.MultipartEntity;
import com.yunji.handler.http.interceptor.RequestInterceptor;
import com.yunji.handler.http.interceptor.ResponseInterceptor;
import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;

/**
 * @author vincent
 *
 */
public class HttpFacade
{

    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8 * 1024; // 8KB

    private static int maxConnections = 10; // http请求最大并发连接数

    private static int socketTimeout = 600 * 1000; // 超时时间，默认10秒

    private static int maxRetries = 6;// 错误尝试次数，错误异常表请在RetryHandler添加

    private static int httpThreadCount = 3;// http线程池数量

    private DefaultHttpClient httpClient;

    private HttpContext httpContext;

    public static final String JSON_CONTENT_TYPE = "application/json";

    public static final String X_WWW_FORM_URLENCODE = "application/x-www-form-urlencoded";

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private String charset = "utf-8";

    private Map<String, String> clientHeaderMap;

    public static final String POST = "POST";

    public static final String GET = "GET";

    public static final String PUT = "PUT";

    public static final String DELETE = "DELETE";

    public static final String TRACE = "TRACE";

    private boolean isTest = false;

    private String gobalContentType = JSON_CONTENT_TYPE;

    private IFailTaskHandler failTaskHandler;

    private boolean ignoreSSL = false;

    private CommonLog logger = LogFactory.createLog();

    private SocketFactory factory;

    public void setGobalContentType(String gobalContentType)
    {
        this.gobalContentType = gobalContentType;
    }

    private static final ThreadFactory sThreadFactory = new ThreadFactory()
    {

        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r)
        {
            Thread tread = new Thread(r, "Http #" + mCount.getAndIncrement());
            tread.setPriority(Thread.NORM_PRIORITY - 1);
            return tread;
        }
    };

    private static final Executor executor = Executors.newFixedThreadPool(httpThreadCount, sThreadFactory);

    public HttpFacade()
    {
        initHttpClient();
    }

    public HttpFacade(boolean ignoreSSL)
    {
        this.ignoreSSL = ignoreSSL;
        initHttpClient();
    }

    public HttpFacade(SocketFactory factory)
    {
        this.factory = factory;
        initHttpClient();
    }

    protected void initHttpClient()
    {
        BasicHttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);

        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        if (ignoreSSL)
        {
            try
            {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                schemeRegistry.register(new Scheme("https", sf, 443));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        // else
        // {
        //
        // schemeRegistry.register(
        // new Scheme("https", factory == null ?
        // SSLSocketFactory.getSocketFactory() : factory, 443));
        // }
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        clientHeaderMap = new HashMap<String, String>();
        httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        httpClient = new DefaultHttpClient(cm, httpParams);
        httpClient.addRequestInterceptor(new RequestInterceptor(clientHeaderMap));
        httpClient.addResponseInterceptor(new ResponseInterceptor());
        httpClient.setHttpRequestRetryHandler(new RetryHandler(maxRetries));
    }

    public HttpClient getHttpClient()
    {
        return this.httpClient;
    }

    public boolean isIgnoreSSL()
    {
        return ignoreSSL;
    }

    public void setIgnoreSSL(boolean ignoreSSL)
    {
        this.ignoreSSL = ignoreSSL;
    }

    public HttpContext getHttpContext()
    {
        return this.httpContext;
    }

    public void setTest(boolean isTest)
    {
        this.isTest = isTest;
    }

    public void configCharset(String charSet)
    {
        if (charSet != null && charSet.trim().length() != 0)
            this.charset = charSet;
    }

    public void configCookieStore(CookieStore cookieStore)
    {
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public void configUserAgent(String userAgent)
    {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
    }

    /**
     * 设置网络连接超时时间，默认为10秒钟
     * 
     * @param timeout
     */
    public void configTimeout(int timeout)
    {
        final HttpParams httpParams = this.httpClient.getParams();
        ConnManagerParams.setTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
    }

    /**
     * 设置https请求时 的 SSLSocketFactory
     * 
     * @param sslSocketFactory
     */
    public void configSSLSocketFactory(SSLSocketFactory sslSocketFactory)
    {
        Scheme scheme = new Scheme("https", sslSocketFactory, 443);
        this.httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
    }

    public void configSSLSocketFactory(SSLSocketFactory sslSocketFactory, int port)
    {
        Scheme scheme = new Scheme("https", sslSocketFactory, port);
        this.httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
    }

    /**
     * 配置错误重试次数
     * 
     * @param retry
     */
    public void configRequestExecutionRetryCount(int count)
    {
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(count));
    }

    /**
     * 添加http请求头
     * 
     * @param header
     * @param value
     */
    public void addHeader(String header, String value)
    {
        if (clientHeaderMap.containsKey(header))
        {
            clientHeaderMap.remove(header);
        }
        clientHeaderMap.put(header, value);
    }

    // ////////////////优化访问方式／／／／／／／／／／／／／／／／
    /**
     * 常用POST模式
     * 
     * @param requestURL
     * @param params
     * @param callBack
     */
    public <T> void postHandler(String requestURL, HttpRequestParams params, HttpCallBack<T> callback)
    {
        HttpEntityEnclosingRequestBase uriRequest = (HttpEntityEnclosingRequestBase) getRequestType(POST, requestURL);
        logger.d(params.toString());
        addEntityToRequestBase(uriRequest, params.getStringEntity());
        RequestContext requestContext = new PostRequestContext(params, uriRequest);
        callback.setRequestContext(requestContext);
        callback.setFailTaskHandler(failTaskHandler);
        new HttpTaskExecutor<T>(httpClient, httpContext, callback, charset, isTest).executeOnExecutor(executor,
                    uriRequest);
    }

    public <T> void getRequestWithPathModel(String requestURL, HttpRequestParams params, HttpCallBack<T> callback)
    {
        HttpUriRequest uriRequest =
                    getRequestType(GET, params == null ? requestURL : params.getRequestString(requestURL));
        RequestContext requestContext = new GetRequestContext(params, uriRequest, requestURL, this, 0);
        callback.setRequestContext(requestContext);
        callback.setFailTaskHandler(failTaskHandler);
        new HttpTaskExecutor<T>(httpClient, httpContext, callback, charset, isTest).executeOnExecutor(executor,
                    uriRequest);
    }

    public <T> void putHandler(String requestURL, HttpRequestParams params, HttpCallBack<T> callBack)
    {
        HttpEntityEnclosingRequestBase uriRequest = (HttpEntityEnclosingRequestBase) getRequestType(PUT, requestURL);
        addEntityToRequestBase(uriRequest, params.getStringEntity());
        new HttpTaskExecutor<T>(httpClient, httpContext, callBack, charset, isTest).executeOnExecutor(executor,
                    uriRequest);
    }

    /**
     * Get请求模式 Query模式
     * 
     * @param url
     * @param params
     * @param callback
     */
    public void getRequestWithQueryModel(String requestURL, HttpRequestParams params,
                HttpCallBack<? extends Object> callback)
    {
        HttpGet httpGet = new HttpGet(params.getUrlWithEncodeQueryString(requestURL));
        RequestContext requestContext = new GetRequestContext(params, httpGet, requestURL, this, 0);
        callback.setRequestContext(requestContext);
        callback.setFailTaskHandler(failTaskHandler);
        sendRequest(httpClient, httpContext, httpGet, null, callback);
    }

    public void get(String url, Header[] headers, HttpRequestParams params, HttpCallBack<? extends Object> callBack)
    {
        HttpUriRequest request = new HttpGet(getUrlWithEncodeQueryString(url, params));
        if (headers != null)
            request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, null, callBack);
    }

    // public void get(String url, HttpCallBack<? extends Object> callBack)
    // {
    // get(url, null, callBack);
    // }
    //
    // public void getRequestHandler(String url, HttpRequestParams params,
    // HttpCallBack<? extends Object> callBack)
    // {
    // sendRequest(httpClient, httpContext, new
    // HttpGet(getUrlWithQueryString(url, params)), null, callBack);
    // }

    // ------------------post 请求-----------------------

    public void post(String url, HttpRequestParams params, HttpCallBack<? extends Object> callback)
    {
        logger.d(params.getStringEntity());
        HttpEntity entity = params.getEntity();
        if (entity instanceof MultipartEntity)
        {
            MultipartEntity multipartEntity = (MultipartEntity) entity;
            post(url, multipartEntity, multipartEntity.getContentType().getValue(), callback);
            return;
        }
        post(url, params.getEntity(), null, callback);
    }

    public void post(String url, HttpEntity entity, String contentType, HttpCallBack<? extends Object> callBack)
    {
        sendRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType, callBack);
    }

    public <T> void post(String url, Header[] headers, HttpRequestParams params, String contentType,
                HttpCallBack<T> callBack)
    {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if (params != null)
            request.setEntity(paramsToEntity(params));
        if (headers != null)
            request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType, callBack);
    }

    public void post(String url, Header[] headers, HttpEntity entity, String contentType,
                HttpCallBack<? extends Object> callBack)
    {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
        if (headers != null)
            request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType, callBack);
    }

    // ------------------put 请求-----------------------

    public void put(String url, HttpCallBack<? extends Object> callBack)
    {
        put(url, null, callBack);
    }

    public void put(String url, HttpRequestParams params, HttpCallBack<? extends Object> callBack)
    {
        put(url, paramsToEntity(params), null, callBack);
    }

    public void put(String url, HttpEntity entity, String contentType, HttpCallBack<? extends Object> callBack)
    {
        sendRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPut(url), entity), contentType, callBack);
    }

    public void put(String url, Header[] headers, HttpEntity entity, String contentType,
                HttpCallBack<? extends Object> callBack)
    {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPut(url), entity);
        if (headers != null)
            request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType, callBack);
    }

    // ------------------delete 请求-----------------------
    public void delete(String url, HttpCallBack<? extends Object> callBack)
    {
        final HttpDelete delete = new HttpDelete(url);
        sendRequest(httpClient, httpContext, delete, null, callBack);
    }

    public void delete(String url, Header[] headers, HttpCallBack<? extends Object> callBack)
    {
        final HttpDelete delete = new HttpDelete(url);
        if (headers != null)
            delete.setHeaders(headers);
        sendRequest(httpClient, httpContext, delete, null, callBack);
    }

    // ---------------------下载---------------------------------------
    public HttpTaskExecutor<File> download(String url, String target, HttpCallBack<File> callback)
    {
        return download(url, null, target, false, callback);
    }

    public HttpTaskExecutor<File> download(String url, String target, boolean isResume, HttpCallBack<File> callback)
    {
        return download(url, null, target, isResume, callback);
    }

    public HttpTaskExecutor<File> download(String url, HttpRequestParams params, String target,
                HttpCallBack<File> callback)
    {
        return download(url, params, target, false, callback);
    }

    public HttpTaskExecutor<File> download(String url, HttpRequestParams params, String target, boolean isResume,
                HttpCallBack<File> callback)
    {
        final HttpGet get = new HttpGet(getUrlWithEncodeQueryString(url, params));
        HttpTaskExecutor<File> handler = new HttpTaskExecutor<File>(httpClient, httpContext, callback, charset, isTest);
        handler.executeOnExecutor(executor, get, target, isResume);
        return handler;
    }

    public HttpTaskExecutor<File> postDownload(String url, HttpRequestParams params, String target, boolean isResume,
                HttpCallBack<File> callback, String chartSet)
    {
        HttpUriRequest uriRequest = addEntityToRequestBase(new HttpPost(url), params.getEntity());
        HttpTaskExecutor<File> handler = new HttpTaskExecutor<File>(httpClient, httpContext, callback,
                    chartSet == null ? charset : chartSet, isTest);
        handler.executeOnExecutor(executor, uriRequest, target, isResume);
        return handler;
    }

    public HttpTaskExecutor<File> postDownload(String url, HttpRequestParams params, String target, boolean isResume,
                HttpCallBack<File> callback)
    {
        HttpUriRequest uriRequest = addEntityToRequestBase(new HttpPost(url), params.getEntity());
        HttpTaskExecutor<File> handler = new HttpTaskExecutor<File>(httpClient, httpContext, callback, charset, isTest);
        handler.executeOnExecutor(executor, uriRequest, target, isResume);
        return handler;
    }

    protected <T> void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest,
                String contentType, HttpCallBack<T> httpCallBack)
    {

        if (contentType != null)
        {
            uriRequest.addHeader("Content-Type", contentType);
        }
        else
        // 自定义Content-type
        {
            if ((gobalContentType != null))
                uriRequest.addHeader("Content-Type", gobalContentType);
        }
        httpCallBack.setFailTaskHandler(failTaskHandler);
        new HttpTaskExecutor<T>(client, httpContext, httpCallBack, charset, isTest).executeOnExecutor(executor,
                    uriRequest);

    }

    // ///////////////////////////////////////////////////////////////////////
    public Object postSyncHandler(String url, HttpRequestParams params, String jsonString)
    {
        return sendSyncRequest(httpClient, httpContext,
                    addEntityToRequestBase(new HttpPost(url), params.getStringEntity(jsonString)), null);

    }

    protected Object sendSyncRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest,
                String contentType)
    {
        if (gobalContentType != null)
        {
            uriRequest.addHeader("Content-Type", gobalContentType);
        }
        if (contentType != null)
        {
            uriRequest.addHeader("Content-Type", contentType);
        }
        return new SyncRequestHandler(client, httpContext, charset).sendRequest(uriRequest);
    }

    public static String getUrlWithQueryString(String url, HttpRequestParams params)
    {
        if (params != null)
        {
            String paramString = params.toString();
            url += "?" + paramString;
        }
        return url;
    }

    public static String getUrlWithEncodeQueryString(String url, HttpRequestParams params)
    {
        if (params != null)
        {
            String paramString = params.getParamString();
            url += "?" + paramString;
        }
        return url;
    }

    private HttpEntity paramsToEntity(HttpRequestParams params)
    {
        HttpEntity entity = null;

        if (params != null)
        {
            entity = params.getEntity();
        }

        return entity;
    }

    private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase requestBase,
                HttpEntity entity)
    {
        for (String key : clientHeaderMap.keySet())
        {
            requestBase.addHeader(key, clientHeaderMap.get(key));
        }
        if (entity != null)
        {
            requestBase.setEntity(entity);
        }
        return requestBase;
    }

    private HttpRequestBase getRequestType(String requestMethod, String requestURL)
    {
        if (requestMethod.equals(POST))
        {
            return new HttpPost(requestURL);
        }
        if (requestMethod.equals(PUT))
        {
            return new HttpPut(requestURL);
        }
        if (requestMethod.equals(GET))
        {
            return new HttpGet(requestURL);
        }
        return null;
    }

    public void setFailTaskHandler(IFailTaskHandler failTaskHandler)
    {
        this.failTaskHandler = failTaskHandler;
    }

}
