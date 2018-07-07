package com.yunji.handler.http;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vincent
 *
 */
public abstract class IFailTaskHandler
{

    private Map<String, RequestContext> concurrentHashMap = new ConcurrentHashMap<>();

    public abstract void handle(int errorNo, String msg);

    protected void refreshRequest(String key, RequestContext context)
    {
        concurrentHashMap.put(key, context);
    }

    public void updateTokenHandler(String tokenKey, String tokenValue)
    {
        for (Entry<String, RequestContext> entry : concurrentHashMap.entrySet())
        {
            RequestContext requestContext = entry.getValue();
            requestContext.updateTokenHandler(tokenKey, tokenValue);
        }
    }

    protected void clean()
    {
        concurrentHashMap.clear();
    }

    protected void cleanByKey(String key)
    {
        if (concurrentHashMap.containsKey(key))
        {
            concurrentHashMap.remove(key);
        }
    }

}
