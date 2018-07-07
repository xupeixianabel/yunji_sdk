package com.yunji.handler.http.interceptor;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * @author vincent
 *
 */
public class RequestInterceptor implements HttpRequestInterceptor
{

    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";

    private static final String ENCODING_GZIP = "gzip";

    private Map<String, String> clientHeaderMap;

    public RequestInterceptor(Map<String, String> clientHeaderMap)
    {
        this.clientHeaderMap = clientHeaderMap;
    }

    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
        if (!request.containsHeader(HEADER_ACCEPT_ENCODING))
        {
            request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
        }
        for (String header : clientHeaderMap.keySet())
        {
            if (request.containsHeader(header))
            {
                request.removeHeaders(header);
            }
            request.addHeader(header, clientHeaderMap.get(header));
        }

    }
}
