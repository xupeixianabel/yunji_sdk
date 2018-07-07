package com.yunji.handler.log;

/**
 * @author vincent
 *
 */
public class LogFactory
{

    private static final String TAG = "LogFactory";

    private static CommonLog log = null;

    public static CommonLog createLog()
    {
        if (log == null)
        {
            log = new CommonLog();
        }

        log.setTag(TAG);
        return log;
    }

    public static CommonLog createLog(Class clazz)
    {
        if (log == null)
        {
            log = new CommonLog();
        }
        String tag = clazz.getName();
        if (tag == null || tag.length() < 1)
        {
            log.setTag(TAG);
        }
        else
        {
            log.setTag(tag);
        }
        return log;
    }

    public static CommonLog createLog(String tag)
    {
        if (log == null)
        {
            log = new CommonLog();
        }

        if (tag == null || tag.length() < 1)
        {
            log.setTag(TAG);
        }
        else
        {
            log.setTag(tag);
        }
        return log;
    }
}