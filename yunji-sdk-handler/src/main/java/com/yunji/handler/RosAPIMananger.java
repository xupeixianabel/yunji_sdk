package com.yunji.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.yunji.handler.api.ICallback;
import com.yunji.handler.api.IResultCallback;
import com.yunji.handler.delegate.YunJiSocketDelegate;
import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;
import com.yunji.handler.model.CookieModel;
import com.yunji.handler.utils.CommonConstants;
import com.yunji.handler.vo.AllRobotStatusVO;
import com.yunji.handler.vo.DoorStatusVO;
import com.yunji.handler.vo.EStopVO;
import com.yunji.handler.vo.PowerStatusVO;
import com.yunji.handler.vo.RobotStatusVO;
import com.yunji.handler.vo.TaskStatusVO;
import com.yunji.handler.vo.TopicSubcribeVO;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;

/**
 * Created by vincent on 2018/4/27.
 */

public class RosAPIMananger
{
    private CommonLog commonLog = LogFactory.createLog(RosAPIMananger.class);
    private Map<String, ICallback<AllRobotStatusVO>> robotStatusBroadCasts;
    private Map<String, ICallback<TaskStatusVO>> taskStautsBroadCasts;
    private Map<String, ICallback<DoorStatusVO>> doorStatusBroadCasts;
    private Map<String, ICallback<PowerStatusVO>> powerStatusBroadCasts;
    private Map<String, ICallback<EStopVO>> eStopBroadCasts;
    private YunJiSocketDelegate socketDelegate;
    private  TopicSubcribeVO topicSubcribeVO;

    public RosAPIMananger(Context context) throws  Exception
    {
        robotStatusBroadCasts = new ConcurrentHashMap<>();
        taskStautsBroadCasts = new ConcurrentHashMap<>();
        doorStatusBroadCasts = new ConcurrentHashMap<>();
        powerStatusBroadCasts = new ConcurrentHashMap<>();
        eStopBroadCasts = new ConcurrentHashMap<>();
        socketDelegate = new YunJiSocketDelegate(context);
    }


    public void registerRobotStatusBroadCasts(String broadcastKey, ICallback<AllRobotStatusVO> callback)
    {
        robotStatusBroadCasts.put(broadcastKey, callback);
    }

    public  void registerTaskStatusBroadCasts(String broadcastKey, ICallback<TaskStatusVO> callback)
    {
        taskStautsBroadCasts.put(broadcastKey, callback);
    }

    public  void registerDoorStatusBroadCasts(String broadcastKey, ICallback<DoorStatusVO> callback)
    {
        doorStatusBroadCasts.put(broadcastKey, callback);
    }

    public  void registerPowerStatusBroadCasts(String broadcastKey, ICallback<PowerStatusVO> callback)
    {
        powerStatusBroadCasts.put(broadcastKey, callback);
    }

    public  void registerEStopBroadCasts(String broadcastKey, ICallback<EStopVO> callback)
    {
        eStopBroadCasts.put(broadcastKey, callback);
    }

    public void unRegisterRobotStatusBroadCasts(String broadcastKey)
    {
        if(robotStatusBroadCasts.containsKey(broadcastKey))
        {
            robotStatusBroadCasts.remove(broadcastKey);
        }
    }

    public  void unRegisterTaskStatusBroadCasts(String broadcastKey)
    {
        if(taskStautsBroadCasts.containsKey(broadcastKey))
        {
            taskStautsBroadCasts.remove(broadcastKey);
        }

    }

    public  void unRegisterDoorStatusBroadCasts(String broadcastKey)
    {
        if(doorStatusBroadCasts.containsKey(broadcastKey))
        {
            doorStatusBroadCasts.remove(broadcastKey);
        }
    }

    public  void unRegisterPowerStatusBroadCasts(String broadcastKey)
    {
        if(powerStatusBroadCasts.containsKey(broadcastKey))
        {
            powerStatusBroadCasts.remove(broadcastKey);
        }
    }

    public  void unRegisterEStopBroadCasts(String broadcastKey)
    {
        if(eStopBroadCasts.containsKey(broadcastKey))
        {
            eStopBroadCasts.remove(broadcastKey);
        }

    }




    /**
     * 获取机器人状态
     */
    private void subcribeForRobotStatusHandler()
    {
        if (!topicSubcribeVO.isRobotStatusSub())
        {
            return;
        }
        commonLog.d(">>>>subcribeForRobotStatusHandler");
        socketDelegate.subRobotStatusTopic(new TopicCallback()
        {

            public void handleMessage(Message message)
            {
                AllRobotStatusVO robotStatusVO = new AllRobotStatusVO(message.toJsonObject());
                if(robotStatusBroadCasts.isEmpty())
                {
                    return;
                }
                for (Object key : robotStatusBroadCasts.keySet())
                {
                    robotStatusBroadCasts.get(key).onSuccess(robotStatusVO);
                }
            }
        });

    }

    /**
     * 获取任务状态
     */
    private void subscribeForTaskStatusHandler()
    {
        if (!topicSubcribeVO.isTaskStautsSub())
        {
            return;
        }
        commonLog.d(">>>>subscribeForTaskStatusHandler");
        socketDelegate.subTaskStatusTopic(new TopicCallback()
        {

            public void handleMessage(Message message)
            {
                commonLog.d(message.toString());
                TaskStatusVO taskStatusVO = new TaskStatusVO(message.toJsonObject());
                if(taskStautsBroadCasts.isEmpty())
                {
                    return;
                }
                for (Object key : taskStautsBroadCasts.keySet())
                {
                    taskStautsBroadCasts.get(key).onSuccess(taskStatusVO);
                }
            }
        });

    }

    /**
     * 获取门状态
     */
    private void subscribeForDoorStatusHandler()
    {
        if (!topicSubcribeVO.isDoorStatusSub())
        {
            return;
        }
        commonLog.d(">>>>subscribeForDoorStatusHandler");
        socketDelegate.subDoorStatusTopic(new TopicCallback()
        {

            public void handleMessage(Message message)
            {
                commonLog.d(message.toString());
                DoorStatusVO doorStatusVO = new DoorStatusVO(message.toJsonObject());
                if(doorStatusBroadCasts.isEmpty())
                {
                    return;
                }
                for (Object key : doorStatusBroadCasts.keySet())
                {
                    doorStatusBroadCasts.get(key).onSuccess(doorStatusVO);
                }
            }
        });
    }

    /**
     * ## 获取电源状态
     */
    private void subcribeForPowerStatusHandler()
    {
        if (!topicSubcribeVO.isPowerStatusSub())
        {
            return;
        }
        commonLog.d(">>>>subcribeForPowerStatusHandler");
        socketDelegate.subPowerStatusTopic(new TopicCallback()
        {

            public void handleMessage(Message message)
            {
                commonLog.d(message.toString());
                PowerStatusVO powerStatusVO = new PowerStatusVO(message.toJsonObject());
                if(powerStatusBroadCasts.isEmpty())
                {
                    return;
                }
                for (Object key : powerStatusBroadCasts.keySet())
                {
                    powerStatusBroadCasts.get(key).onSuccess(powerStatusVO);
                }

            }
        });

    }

    /**
     * 获取急停状态
     */
    private void subcribeForEStopStatus()
    {
        if (!topicSubcribeVO.iseStopSub())
        {
            return;
        }
        commonLog.d(">>>>subcribeForEStopStatus");
        socketDelegate.subEStopStatusTopic(new TopicCallback()
        {

            public void handleMessage(Message message)
            {
                commonLog.d(message.toString());
                EStopVO eStopVO = new EStopVO(message.toJsonObject());
                if(eStopBroadCasts.isEmpty())
                {
                    return;
                }
                for (Object key : eStopBroadCasts.keySet())
                {
                    eStopBroadCasts.get(key).onSuccess(eStopVO);
                }
            }
        });

    }


    public void unSubcribeHandler(TopicSubcribeVO vo)
    {
        this.topicSubcribeVO  = vo;
        new Thread()
        {
            public void run()
            {
                Looper.prepare();
                new Handler().post(unSubscribeHandler);
                Looper.loop();
            }
        }.start();


    }


    public  void subcribeHandler(TopicSubcribeVO topicSubcribeVO)
    {
        this.topicSubcribeVO = topicSubcribeVO;
        new Thread(subscribeHandler).start();
    }


    Runnable subscribeHandler = new Runnable()
    {
        public void run()
        {
            commonLog.d("subscribeHandler>>>>");
            subscribeForTaskStatusHandler();
            subscribeForDoorStatusHandler();
            subcribeForRobotStatusHandler();
            subcribeForPowerStatusHandler();
            subcribeForEStopStatus();
        }
    };



    Runnable unSubscribeHandler = new Runnable()
    {
        public void run()
        {
            commonLog.d("unSubscribeHandler>>>>");
            if (!topicSubcribeVO.isRobotStatusSub())
            {
                socketDelegate.unSubRobotStatusTopic();
            }
            if (!topicSubcribeVO.isDoorStatusSub())
            {
                socketDelegate.unSubDoorStatusTopic();
            }
            if (!topicSubcribeVO.iseStopSub())
            {
                socketDelegate.unSubEStopStatusTopic();
            }
            if (!topicSubcribeVO.isPowerStatusSub())
            {
                socketDelegate.unSubPowerStatusTopic();
            }
            if (!topicSubcribeVO.isTaskStautsSub())
            {
                socketDelegate.unSubTaskStatusTopic();
            }
        }
    };

}
