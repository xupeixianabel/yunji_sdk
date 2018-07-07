package com.yunji.handler.http;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * @author vincent
 *
 */
public class GetRequestContext extends RequestContext
{

    private HttpFacade httpFacade;

    private String requestURL;

    private int requestMode;// 0是路径模式（Path），1是查询参数模式(query)

    public GetRequestContext(HttpRequestParams requestBody, HttpUriRequest httpRequest, String requestURL,
                             HttpFacade httpFacade, int requestMode)
    {
        super(requestBody, httpRequest);
        this.httpFacade = httpFacade;
        this.requestURL = requestURL;
        this.requestMode = requestMode;

    }

    protected void resetReqeustParams(String tokenKey, String tokenValue)
    {
        httpFacade.addHeader(tokenKey, tokenValue);
        HttpGet httpGet = (HttpGet) httpRequest;
        String url = "";
        if (requestMode == 0)
        {
            url = requestBody == null ? requestURL : requestBody.getRequestString(requestURL);
        }
        else if (requestMode == 1)
        {
            url = HttpFacade.getUrlWithEncodeQueryString(requestURL, requestBody);
        }
        try
        {
            httpGet.setURI(new URI(url));
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        return;

    }

}
