package com.yunji.handler.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author vincent
 *
 */
public class PostRequestContext extends RequestContext
{

    public PostRequestContext(HttpRequestParams requestBody, HttpRequestBase httpRequest)
    {
        super(requestBody, httpRequest);
    }

    protected void resetReqeustParams(String tokenKey, String tokenValue)
    {
        if (httpRequest instanceof HttpEntityEnclosingRequestBase)
        {
            HttpEntityEnclosingRequestBase request = (HttpEntityEnclosingRequestBase) httpRequest;
            request.setEntity(requestBody.getStringEntity());
        }

    }

}
