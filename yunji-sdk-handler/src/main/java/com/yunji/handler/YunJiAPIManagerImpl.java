package com.yunji.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.yunji.handler.api.ICallback;
import com.yunji.handler.api.IResultCallback;
import com.yunji.handler.api.IYunJiAPIManger;
import com.yunji.handler.delegate.YunJiHttpDelegate;
import com.yunji.handler.delegate.YunJiSocketDelegate;
import com.yunji.handler.http.YunJiHttpCallback;
import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;
import com.yunji.handler.model.CookieModel;
import com.yunji.handler.utils.CommonConstants;
import com.yunji.handler.utils.YunJiUtils;
import com.yunji.handler.vo.AllRobotStatusVO;
import com.yunji.handler.vo.CustTaskStatusVO;
import com.yunji.handler.vo.DoorStatusVO;
import com.yunji.handler.vo.EStopVO;
import com.yunji.handler.vo.LangVO;
import com.yunji.handler.vo.MarkerVO;
import com.yunji.handler.vo.PowerStatusVO;
import com.yunji.handler.vo.RobotInfoVO;
import com.yunji.handler.vo.TaskStatusVO;
import com.yunji.handler.vo.TopicSubcribeVO;
import com.yunji.handler.vo.VolumeVO;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;

/**
 * @author vincent
 *
 */
class YunJiAPIManagerImpl implements IYunJiAPIManger
{

    private CommonLog commonLog = LogFactory.createLog(YunJiAPIManagerImpl.class);

    private YunJiHttpDelegate yunJiDelegate;

    private  RosAPIMananger rosAPIMananger;

    private static YunJiAPIManagerImpl instance;

    private String appName;

    private String appKey;

    private Context context;

    private String httpURL;

    private String authHttpURL;

    private  TopicSubcribeVO  topicSubcribeVO;



    private YunJiAPIManagerImpl(Context context) throws  Exception
    {
        this.context = context;
        checkConfigHandler();
        CookieModel cookieModel = CookieModel.init(context);
        cookieModel.addCookie(CommonConstants.Config.HTTP_URL, httpURL);
        cookieModel.addCookie(CommonConstants.Config.AUTH_HTTP_URL, authHttpURL);
        yunJiDelegate = new YunJiHttpDelegate(context);
        yunJiDelegate.setFailTaskHandler(new YunJiFailTaskHandler());
        CookieModel.init(context);
        Thread initRosThread = new Thread(initRosHandler);
        initRosThread.start();
        initRosThread.join();
        new Thread()
        {
            public void run()
            {
                Looper.prepare();
                new Handler().post(registerHander);
                Looper.loop();
            }
        }.start();

    }





    /**
     * 检查assets/config.properties文件中的相关配置
     * @throws Exception
     */
    private void checkConfigHandler() throws  Exception
    {
        httpURL = YunJiUtils.getPropertiesValue(context, CommonConstants.Config.HTTP_URL);
        if(YunJiUtils.isBlank(httpURL))
        {
            throw new Exception("Please config http url in the assets/config.properties file");
        }
        authHttpURL= YunJiUtils.getPropertiesValue(context, CommonConstants.Config.AUTH_HTTP_URL);
        appName = YunJiUtils.getPropertiesValue(context, CommonConstants.Config.YUN_JI_NAME);
        appKey = YunJiUtils.getPropertiesValue(context, CommonConstants.Config.YUN_JI_KEY);
        CommonLog.isDebug = Boolean.valueOf(YunJiUtils.getPropertiesValue(context, CommonConstants.Config.LOG_DEBUG));
        if (YunJiUtils.isBlank(appName) || YunJiUtils.isBlank(appKey))
        {
            throw  new Exception("Please config appName or appKey  in the assets/config.properties file,if you have any problem, please contact YunJi.");

        }
        topicSubcribeVO = new TopicSubcribeVO();
        topicSubcribeVO.setDoorStatusSub(Boolean.valueOf(YunJiUtils.getPropertiesValue(context, CommonConstants.Config.DOOR_STATUS_SUB)));
        topicSubcribeVO.seteStopSub(Boolean.valueOf(YunJiUtils.getPropertiesValue(context, CommonConstants.Config.E_STOP_SUB)));
        topicSubcribeVO.setPowerStatusSub(Boolean.valueOf(YunJiUtils.getPropertiesValue(context, CommonConstants.Config.POWER_STATUS_SUB)));
        topicSubcribeVO.setRobotStatusSub(Boolean.valueOf(YunJiUtils.getPropertiesValue(context, CommonConstants.Config.ROBOT_STATUS_SUB)));
        topicSubcribeVO.setTaskStautsSub(Boolean.valueOf(YunJiUtils.getPropertiesValue(context, CommonConstants.Config.TASK_STATUS_SUB)));

    }


    Runnable initRosHandler = new Runnable()
    {

        public void run()
        {
            try
            {
                rosAPIMananger = new RosAPIMananger(context);
                subcribeHandler(topicSubcribeVO);

            }catch (Exception e)
            {
                commonLog.d(e.getMessage());
            }

        }
    };


    Runnable registerHander = new Runnable()
    {

        public void run()
        {
            register(new IResultCallback<String>() {

                public void onError(String msg)
                {
                    commonLog.d("register fail on yunji sdk, the cause:"+msg);
                }

                public void onSuccess(String s)
                {
                    commonLog.d("register success on yunji sdk");
                }
            });

        }
    };


    public static YunJiAPIManagerImpl init(Context context) throws  Exception
    {
        if (instance == null)
        {
            instance = new YunJiAPIManagerImpl(context);
        }
        return instance;
    }

    public static YunJiAPIManagerImpl getInstance()
    {
        if (instance == null)
        {
            try
            {
                throw new Exception("First excute init method");
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        return instance;
    }

    public void register(final  IResultCallback<String> callback)
    {
        String ts = String.valueOf(System.currentTimeMillis());
        String value = "appname:"+appName+"|secret:"+appKey +"|ts:"+ts;
        String sign = YunJiUtils.Md5(value);
        yunJiDelegate.register(appName, ts, sign, new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
               JsonObject jsonResult = Json.createReader(new StringReader(result)).readObject();
               String token = jsonResult.getString("accessToken");
                yunJiDelegate.updateTokenHandler(token);
                if (callback != null)
                    callback.onSuccess(token);
            }

            @Override
            public void errorHandler(String message)
            {

            }
        });

    }

    /**
     * 开门
     * @param callback
     */
    public void openHandler(final IResultCallback<String> callback)
    {
        yunJiDelegate.open(new YunJiHttpCallback()
        {

            @Override
            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            @Override
            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);

            }
        });
    }

    /**
     * 关门
     * @param callback
     */
    public void closeHandler(final IResultCallback<String> callback)
    {
        yunJiDelegate.close(new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    /**
     *
     ## 发任务(自动返程,带回调)
     * @param marker
     * @param startCallback
     * @param successCallback
     * @param faildCallback
     * @param callback
     */
    public void excuteTaskWithCallbackHandler(String marker, String startCallback, String successCallback,
                String faildCallback, final IResultCallback<String> callback)
    {
        yunJiDelegate.excuteTaskWithCallback(marker, startCallback, successCallback, faildCallback,
                    new YunJiHttpCallback()
                    {

                        public void successHandler(String result)
                        {
                            if (callback != null)
                                callback.onSuccess(result);
                        }

                        public void errorHandler(String message)
                        {
                            if (callback != null)
                                callback.onError(message);
                        }
                    });
    }

    /**
     * ## 发任务(不自动返程，不打电话，任务结束才会返回)
     * @param marker
     * @param callback
     */
    public void excuteTaskWithNoBackHandler(String marker, IResultCallback<String> callback)
    {
        yunJiDelegate.excuteTaskWithNoBack(marker, new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    /**
     * ## 发任务(不返程,带回调)
     * @param marker
     * @param startCallback
     * @param successCallback
     * @param faildCallback
     * @param callback
     */
    public void excuteTaskNobackWithCallbackHandler(String marker, String startCallback, String successCallback,
                String faildCallback, IResultCallback<String> callback)
    {
        yunJiDelegate.excuteTaskNobackWithCallback(marker, startCallback, successCallback, faildCallback,
                    new YunJiHttpCallback()
                    {

                        public void successHandler(String result)
                        {
                            if (callback != null)
                                callback.onSuccess(result);
                        }

                        public void errorHandler(String message)
                        {
                            if (callback != null)
                                callback.onError(message);
                        }
                    });
    }


    /**
     * 发任务(目前只支持多点送物) "taskType": "multiple_transport","message": ["001", "002", "003"]
     * @param taskType
     * @param message
     * @param callback
     */
    public void flowHandler(String taskType, JsonArray message, IResultCallback<String> callback)
    {
        yunJiDelegate.flow(taskType, message, new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });
    }


    /**
     * ## 移动点位
     * @param marker
     * @param callback
     */
    public void moveHandler(String marker, IResultCallback<String> callback)
    {
        yunJiDelegate.moveHandler(marker, new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    /**
     * 取消任务
     * @param callback
     */
    public void cancelTaskHandler(final IResultCallback<String> callback)
    {
        yunJiDelegate.cancelTask(new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });
    }

    /**
     * 机器人状态
     * @param callback
     */
    public void getTaskStatusHandler(final IResultCallback<CustTaskStatusVO> callback)
    {
        yunJiDelegate.getTaskStatus(new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                JsonObject jsonObject = Json.createReader(new StringReader(result)).readObject();
                CustTaskStatusVO taskStatusVO = new CustTaskStatusVO(jsonObject);
                if (callback != null)
                    callback.onSuccess(taskStatusVO);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    /**
     * 回充电桩
     * @param callback
     */
    public void forceChargeHandler(final IResultCallback<String> callback)
    {
        yunJiDelegate.forceCharge(new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    /**
     * 获取所有点位(含坐标)
     * @param callback
     */
    public void queryMarkerList(final IResultCallback<List<MarkerVO>> callback)
    {
        yunJiDelegate.queryMarkerList(new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                JsonObject jsonObject = Json.createReader(new StringReader(result)).readObject();
                List<MarkerVO> markerVOs = new MarkerVO(jsonObject).getList();
                if (callback != null)
                    callback.onSuccess(markerVOs);

            }

            @Override
            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }



    /**
     * 获取当前语言
     * @param callback
     */
    public void getLangHandler(final IResultCallback<LangVO> callback)
    {
        yunJiDelegate.getLangHandler(new YunJiHttpCallback()
        {

            @Override
            public void successHandler(String result)
            {
                JsonObject jsonObject = Json.createReader(new StringReader(result)).readObject();
                LangVO langVO = new LangVO(jsonObject);
                if (callback != null)
                    callback.onSuccess(langVO);

            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);

            }
        });

    }

    /**
     *
     * @param lang  目前支持zh,en,ko_KR
     * @param callback
     */
    public void setLangHandler(String lang, IResultCallback<String> callback)
    {
        yunJiDelegate.setLangHandler(lang, new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    /**
     *返回酒店ID
     * @param callback
     */
    public void getHotelIdHandler(IResultCallback<String> callback)
    {
        yunJiDelegate.getHotelIdHandler(new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                JsonObject jsonObject = Json.createReader(new StringReader(result)).readObject();
                String hotelid = jsonObject.getString("hotelid");
                if (callback != null)
                    callback.onSuccess(hotelid);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });
    }

    /**
     * 返回站定语音开关
     * @param callback
     */
    public void getAdEnableHandler(IResultCallback<Boolean> callback)
    {
        yunJiDelegate.getAdEnableHandler(new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                JsonObject jsonObject = Json.createReader(new StringReader(result)).readObject();
                Boolean adPlayEnable = jsonObject.getBoolean("ad_paly_enable");
                if (callback != null)
                    callback.onSuccess(adPlayEnable);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });
    }

    /**
     *
     * @param enable 站定语音开关
     * @param callback
     */
    public void setAdEnableHandler(Boolean enable, IResultCallback<String> callback)
    {
        yunJiDelegate.setAdEnableHandler(enable, new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    /**
     * 返回机器人编号
     * @param callback
     */
    public void getRobotNoHandler(final IResultCallback<String> callback)
    {
        yunJiDelegate.getRobotNoHandler(new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {

                JsonObject jsonObject = Json.createReader(new StringReader(result)).readObject();
                String sn = jsonObject.getString("sn");
                if (callback != null)
                    callback.onSuccess(sn);
            }

            @Override
            public void errorHandler(String message)
            {

                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    public void getVolumeHandler(final IResultCallback<VolumeVO> callback)
    {
        yunJiDelegate.getVolumeHandler(new YunJiHttpCallback()
        {

            @Override
            public void successHandler(String result)
            {
                JsonObject jsonObject = Json.createReader(new StringReader(result)).readObject();
                VolumeVO volumeVO = new VolumeVO(jsonObject);
                if (callback != null)
                    callback.onSuccess(volumeVO);

            }

            @Override
            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);

            }
        });
    }

    /**
     *
     * @param dayVolume 白天音量
     * @param nightVolume 晚上音量
     * @param callback
     */
    public void setVolumeHandler(String dayVolume, String nightVolume, IResultCallback<String> callback)
    {
        yunJiDelegate.setVolumeHandler(dayVolume, nightVolume, new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    public void getVersionHandler(final IResultCallback<String> callback)
    {
        yunJiDelegate.getVersionHandler(new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                JsonObject jsonObject = Json.createReader(new StringReader(result)).readObject();
                String version = jsonObject.getString("version");
                if (callback != null)
                    callback.onSuccess(version);

            }

            @Override
            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    /**
     *
     * @param text 说话内容
     * @param callback
     */
    public void playHandler(String text, IResultCallback<String> callback)
    {
        yunJiDelegate.playHandler(text, new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });
    }

    /**
     * @param type "time" or "times", //按时间或者按次对应一下某个字段生效
     * @param time (int)分钟
     * @param times (int)次
     * @param name 目前传default即可
     * @param soundType "sync" or "async",  // 说完再走 或 边说边走
     * @param targets //点位名称和要说的语音文件 [{"marker": "002", "sound": "cruise_测试1"}, ...]
     * @param callback
     */
    public void startCruiseHandler(String type, int time, int times, String name, String soundType, JsonArray targets,
                IResultCallback<String> callback)
    {
        yunJiDelegate.startCruiseHandler(type, time, times, name, soundType, targets, new YunJiHttpCallback()
        {

            public void successHandler(String result)
            {
                if (callback != null)
                    callback.onSuccess(result);
            }

            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });
    }

    /**
     *  获取机器人基础信息
     * @param callback
     */
    public void getRobotInfo(final IResultCallback<RobotInfoVO> callback)
    {
        yunJiDelegate.getRobotInfo(new YunJiHttpCallback() {

            public void successHandler(String result)
            {
                JsonObject jsonObject = Json.createReader(new StringReader(result)).readObject();
                RobotInfoVO robotInfoVO = new RobotInfoVO(jsonObject);
                if (callback != null)
                    callback.onSuccess(robotInfoVO);

            }

            @Override
            public void errorHandler(String message)
            {
                if (callback != null)
                    callback.onError(message);
            }
        });

    }

    // //// API from socket pattern


    public void  unSubcribleHandler(TopicSubcribeVO topicSubcribeVO)
    {
        rosAPIMananger.unSubcribeHandler(topicSubcribeVO);

    }

    public  void subcribeHandler(TopicSubcribeVO topicSubcribeVO)
    {
        rosAPIMananger.subcribeHandler(topicSubcribeVO);
    }


    // 注册获取机器人状
    public void registerRobotStatusBroadCasts(String broadcastKey, ICallback<AllRobotStatusVO> callback)
    {
        rosAPIMananger.registerRobotStatusBroadCasts(broadcastKey, callback);
    }
    // 注册获取任务状态
    public  void registerTaskStatusBroadCasts(String broadcastKey, ICallback<TaskStatusVO> callback)
    {
        rosAPIMananger.registerTaskStatusBroadCasts(broadcastKey, callback);
    }
    // 注册获取门状态
    public  void registerDoorStatusBroadCasts(String broadcastKey, ICallback<DoorStatusVO> callback)
    {
        rosAPIMananger.registerDoorStatusBroadCasts(broadcastKey, callback);

    }
    // 注册获取电源状态
    public  void registerPowerStatusBroadCasts(String broadcastKey, ICallback<PowerStatusVO> callback)
    {
        rosAPIMananger.registerPowerStatusBroadCasts(broadcastKey, callback);

    }
    // 注册获取急停状态
    public  void registerEStopBroadCasts(String broadcastKey, ICallback<EStopVO> callback)
    {
        rosAPIMananger.registerEStopBroadCasts(broadcastKey, callback);

    }
    // 取消注册获取机器人状态
    public void unRegisterRobotStatusBroadCasts(String broadcastKey)
    {
        rosAPIMananger.unRegisterRobotStatusBroadCasts(broadcastKey);

    }
    // 取消注册获取任务状态
    public  void unRegisterTaskStatusBroadCasts(String broadcastKey)
    {
        rosAPIMananger.unRegisterTaskStatusBroadCasts(broadcastKey);
    }
    // 取消注册获取门状态
    public  void unRegisterDoorStatusBroadCasts(String broadcastKey)
    {
        rosAPIMananger.unRegisterDoorStatusBroadCasts(broadcastKey);

    }
    // 取消注册获取电源状态
    public  void unRegisterPowerStatusBroadCasts(String broadcastKey)
    {
        rosAPIMananger.unRegisterPowerStatusBroadCasts(broadcastKey);
    }
    // 取消注册获取急停状态
    public  void unRegisterEStopBroadCasts(String broadcastKey)
    {
        rosAPIMananger.unRegisterEStopBroadCasts(broadcastKey);

    }




}
