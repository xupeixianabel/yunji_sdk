package com.yunji.handler.http;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;

import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;

/**
 * @author vincent
 *
 */
public abstract class HttpCallBack<T>
{

    private CommonLog log = LogFactory.createLog("HttpCallBack");

    private boolean progress = true;

    private int rate = 1000 * 1;// 姣��

    private RequestContext requestContext;

    private IFailTaskHandler failTaskHandler;

    private int stateCode = 0;

    public boolean isProgress()
    {
        return progress;
    }

    public int getRate()
    {
        return rate;
    }

    public void setFailTaskHandler(IFailTaskHandler failTaskHandler)
    {
        this.failTaskHandler = failTaskHandler;
    }

    /**
     * 璁剧疆杩�害,������璁剧疆浜��涓��浠ュ�锛�nLoading��������
     * 
     * @param progress
     *            ������杩�害�剧ず
     * @param rate
     *            杩�害�存�棰��
     */
    public HttpCallBack<T> progress(boolean progress, int rate)
    {
        this.progress = progress;
        this.rate = rate;
        return this;
    }

    public void onStart()
    {
    }

    /**
     * onLoading�规����progress
     * 
     * @param count
     * @param current
     */
    public void onLoading(long count, long current)
    {

    }

    public void onSuccess(T t)
    {
    }

    public void onFailure(Throwable t, int errorNo, String strMsg)
    {
        log.e("ERROR:==" + errorNo + "   " + strMsg);
        if (failTaskHandler == null)
        {
            return;
        }
        if ((t instanceof HttpResponseException))
        {
            authHandler(errorNo);
            failTaskHandler.handle(errorNo, strMsg);
            return;
        }
        if (t instanceof IOException)
        {
            strMsg = "网络连接不可用，请稍后重试";
        }
        failTaskHandler.handle(errorNo, strMsg);
    }

    public void setRequestContext(RequestContext requestContext)
    {
        this.requestContext = requestContext;
    }

    private void authHandler(int errorNo)
    {
        if (errorNo == 401 && requestContext != null)
        {
            stateCode = 401;
            failTaskHandler.refreshRequest(requestContext.getClass().toString(), requestContext);
        }
    }

    public void cleanRetry()
    {
        if (stateCode != 401)
            return;
        failTaskHandler.clean();
    }
}
