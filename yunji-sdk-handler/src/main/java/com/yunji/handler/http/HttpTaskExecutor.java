package com.yunji.handler.http;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import com.yunji.handler.http.entityhandler.EntityCallBack;
import com.yunji.handler.http.entityhandler.FileEntityHandler;
import com.yunji.handler.http.entityhandler.StringEntityHandler;
import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;

import android.os.AsyncTask;
import android.os.SystemClock;

/**
 * @author vincent
 *
 */
public class HttpTaskExecutor<T> extends AsyncTask<Object, Object, Object>implements EntityCallBack
{

    private final AbstractHttpClient client;

    private final HttpContext context;

    private final CommonLog logger = LogFactory.createLog(getClass());

    private final StringEntityHandler mStrEntityHandler = new StringEntityHandler();

    private final FileEntityHandler mFileEntityHandler = new FileEntityHandler();

    private final HttpCallBack<T> callback;

    private final static String UNKNOW_HOST_TIP = "网络连接不可用，请稍后重试";

    private int executionCount = 0;

    private String targetUrl = null; // 下载的路径

    private boolean isResume = false; // 是否断点续传

    private String charset;

    private boolean isTest = false;

    // private Map<String, String> loginOutStates;

    public HttpTaskExecutor(AbstractHttpClient client, HttpContext context, HttpCallBack<T> callback, String charset,
                            boolean isTest)
    {
        this.client = client;
        this.context = context;
        this.callback = callback;
        this.charset = charset;
        this.isTest = isTest;
    }

    private void makeRequestWithRetries(HttpUriRequest request) throws HttpException
    {
        if (isTest)
        {
            publishProgress(UPDATE_SUCCESS, "No Backend");
            return;
        }
        if (isResume && targetUrl != null)
        {
            File downloadFile = new File(targetUrl);
            long fileLen = 0;
            if (downloadFile.isFile() && downloadFile.exists())
            {
                fileLen = downloadFile.length();
            }
            if (fileLen > 0)
                request.setHeader("RANGE", "bytes=" + fileLen + "-");
        }
        boolean retry = true;
        HttpException cause = null;
        HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
        while (retry)
        {
            try
            {
                if (isCancelled())
                {
                    return;
                }
                HttpResponse response = client.execute(request, context);
                logger.e(request.getURI());
                if (isCancelled())
                {
                    return;
                }
                handleResponse(response);
                return;

            } catch (UnknownHostException e)
            {
                publishProgress(UPDATE_FAILURE, e, HttpException.UNKNOW_HOST_EXCEPTION_CODE, UNKNOW_HOST_TIP);
                return;
            } catch (NullPointerException e)
            {
                cause = new HttpException("NPE in HttpClient" + e.getMessage(),
                            HttpException.NULL_POINT_EXCEPTION_CODE);
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
            } catch (HttpHostConnectException e)
            {
                cause = new HttpException("NPE in HttpClient" + e.getMessage(),
                            HttpException.HOST_CONNECT_EXCTPION_CODE);
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
                logger.e(e.getMessage() + ">>" + request.getURI() + ">>Request Retry>>" + retry + ">>executionCount>>"
                            + executionCount);

            } catch (ConnectTimeoutException e)
            {
                cause = new HttpException("NPE in HttpClient" + e.getMessage(), HttpException.TIMEOUT_EXCEPTION_CODE);
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
                logger.e(e.getMessage() + ">>" + request.getURI() + ">>Request Retry>>" + retry + ">>executionCount>>"
                            + executionCount);

            } catch (HttpException e)
            {
                cause = new HttpException("Exception" + e.getMessage(), e.getCode());
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
                logger.e(e.getMessage() + ">>" + request.getURI() + ">>Request Retry>>" + retry + ">>executionCount>>"
                            + executionCount);
            } catch (IOException e)
            {
                cause = new HttpException("Exception" + e.getMessage(), HttpException.IO_EXCEPTION_CODE);
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
                logger.e(e.getMessage() + ">>" + request.getURI() + ">>Request Retry>>" + retry + ">>executionCount>>"
                            + executionCount);
            } catch (Exception e)
            {
                cause = new HttpException("Exception" + e.getMessage());
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
                logger.e(e.getMessage() + ">>" + request.getURI() + ">>Request Retry>>" + retry + ">>executionCount>>"
                            + executionCount);
            }
        }
        if (cause != null)
            throw cause;
        else
            throw new HttpException("未知网络错误");
    }

    @Override
    protected Object doInBackground(Object... params)
    {
        if (params != null && params.length == 3)
        {
            targetUrl = String.valueOf(params[1]);
            isResume = (Boolean) params[2];
        }
        try
        {
            publishProgress(UPDATE_START); // 开始
            makeRequestWithRetries((HttpUriRequest) params[0]);
        } catch (HttpException e)
        {
            publishProgress(UPDATE_FAILURE, e, e.getCode(), e.getMessage()); // 结束
        }

        return null;
    }

    private final static int UPDATE_START = 1;

    private final static int UPDATE_LOADING = 2;

    private final static int UPDATE_FAILURE = 3;

    private final static int UPDATE_SUCCESS = 4;

    protected void onProgressUpdate(Object... values)
    {
        int update = Integer.valueOf(String.valueOf(values[0]));
        switch (update)
        {
        case UPDATE_START:
            if (callback != null)
                callback.onStart();
            break;
        case UPDATE_LOADING:
            if (callback != null)
                callback.onLoading(Long.valueOf(String.valueOf(values[1])), Long.valueOf(String.valueOf(values[2])));
            break;
        case UPDATE_FAILURE:
            if (callback != null)
            {
                callback.cleanRetry();
                callback.onFailure((Throwable) values[1], (Integer) values[2], (String) values[3]);

            }
            break;
        case UPDATE_SUCCESS:
            if (callback != null)
                callback.onSuccess((T) values[1]);
            break;
        default:
            break;
        }
        super.onProgressUpdate(values);
    }

    public boolean isStop()
    {
        return mFileEntityHandler.isStop();
    }

    /**
     * @param stop
     *            停止下载任务
     */
    public void stop()
    {
        mFileEntityHandler.setStop(true);
    }

    private void handleResponse(HttpResponse response) throws Exception
    {
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() >= 300 && status.getStatusCode() != 406)
        {
            /**
             * 如果没有授权访问，访问不断重试访问,达到次数一定后放弃,同时返回结果会交给用户处理: 如重新获取令牌等操作
             */
            if (response.getStatusLine().getStatusCode() == 401)
            {
                if (executionCount % 3 == 0 && executionCount < 9)
                {
                    publishProgress(UPDATE_FAILURE,
                                new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()),
                                status.getStatusCode(), "Reason:Unauthorized");
                }
                throw new HttpException("Reason:Unauthorized", HttpException.AUTH_EXCEPTION_CODE);
            }
            String errorMsg = "服务器请求错误,错误码::" + status.getStatusCode() + ",请稍后重试";
            if (status.getStatusCode() == 416 && isResume)
            {
                errorMsg += " \n maybe you have download complete.";
            }
            publishProgress(UPDATE_FAILURE, new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()),
                        status.getStatusCode(), errorMsg);
            return;
        }
        try
        {
            HttpEntity entity = response.getEntity();
            Object responseBody = null;
            if (entity != null)
            {
                time = SystemClock.uptimeMillis();
                responseBody = targetUrl != null
                            ? mFileEntityHandler.handleEntity(entity, this, targetUrl, isResume, charset)
                            : mStrEntityHandler.handleEntity(entity, this, charset);

            }
            if (status.getStatusCode() == 406)
            {
                publishProgress(UPDATE_FAILURE,
                            new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()),
                            status.getStatusCode(), responseBody);
                return;
            }
            publishProgress(UPDATE_SUCCESS, responseBody);
        } catch (IOException e)
        {
            throw e;
        }

    }

    private long time;

    public void callBack(long count, long current, boolean mustNoticeUI)
    {
        if (callback != null && callback.isProgress())
        {
            if (mustNoticeUI)
            {
                publishProgress(UPDATE_LOADING, count, current);
                return;
            }
            long thisTime = SystemClock.uptimeMillis();
            if (thisTime - time >= callback.getRate())
            {
                time = thisTime;
                publishProgress(UPDATE_LOADING, count, current);
            }
        }
    }

}
