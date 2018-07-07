package com.yunji.handler;

import com.yunji.handler.api.IResultCallback;
import com.yunji.handler.delegate.YunJiHttpDelegate;
import com.yunji.handler.http.IFailTaskHandler;
import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;
/**
 * @author vincent
 *
 */
class YunJiFailTaskHandler extends IFailTaskHandler
{
    private CommonLog commonLog = LogFactory.createLog(YunJiFailTaskHandler.class);

    private YunJiHttpDelegate yunJiDelegate;


    public void handle(int errorNo, String msg)
    {
        YunJiAPIManagerImpl.getInstance().register(new IResultCallback<String>() {

            public void onError(String msg)
            {
                commonLog.d("YunJiFailTaskHandler>>"+msg);

            }


            public void onSuccess(String token)
            {
                commonLog.d("YunJiFailTaskHandler fetch token success>>"+token);
                updateTokenHandler("token",token);
            }
        });
    }

}
