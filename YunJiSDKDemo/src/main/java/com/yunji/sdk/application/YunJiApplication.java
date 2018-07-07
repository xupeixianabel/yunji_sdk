package com.yunji.sdk.application;

import android.app.Application;
import android.util.Log;

import com.yunji.handler.YunJiApiFactory;
import com.yunji.handler.api.ICallback;
import com.yunji.handler.api.IYunJiAPIManger;
import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;
import com.yunji.handler.vo.AllRobotStatusVO;
import com.yunji.handler.vo.DoorStatusVO;
import com.yunji.handler.vo.EStopVO;
import com.yunji.handler.vo.PowerStatusVO;
import com.yunji.handler.vo.TaskStatusVO;
import com.yunji.sdk.MainActivity;

import java.util.logging.Logger;

/**
 * Created by vincent on 2018/3/22.
 */

public class YunJiApplication extends Application
{

    private CommonLog commonLog = LogFactory.createLog(YunJiApplication.class);

    public static IYunJiAPIManger apiManger;

    public void onCreate()
    {
        super.onCreate();
        try
        {
            YunJiApplication.apiManger = YunJiApiFactory.createYunJiApi(YunJiApplication.this);
            commonLog.d(apiManger.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
