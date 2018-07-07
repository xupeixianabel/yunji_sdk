package com.yunji.handler;

import com.yunji.handler.api.IYunJiAPIManger;


import android.content.Context;
/**
 * @author vincent
 *
 */
public class YunJiApiFactory
{

    private YunJiApiFactory()
    {
    }

    public static IYunJiAPIManger createYunJiApi(Context context) throws  Exception
    {
        return YunJiAPIManagerImpl.init(context);
    }
}
