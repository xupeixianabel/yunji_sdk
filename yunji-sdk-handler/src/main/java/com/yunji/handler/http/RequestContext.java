package com.yunji.handler.http;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * @author vincent
 *
 */
public abstract class RequestContext
{

    protected HttpRequestParams requestBody;

    public void setRequestBody(HttpRequestParams requestBody)
    {
        this.requestBody = requestBody;
    }

    public void setHttpRequest(HttpRequestBase httpRequest)
    {
        this.httpRequest = httpRequest;
    }

    protected HttpUriRequest httpRequest;

    public RequestContext()
    {
    }

    public RequestContext(HttpRequestParams requestBody, HttpUriRequest httpRequest)
    {
        this.requestBody = requestBody;
        this.httpRequest = httpRequest;
    }

    public void updateTokenHandler(String tokenKey, String tokenValue)
    {
        if (requestBody == null || httpRequest == null || !requestBody.containsKey(tokenKey))
        {
            return;
        }
        requestBody.put(tokenKey, tokenValue);
        resetReqeustParams(tokenKey, tokenValue);
    }

    abstract protected void resetReqeustParams(String tokenKey, String tokenValue);

}
