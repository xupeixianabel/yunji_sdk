package com.yunji.sdk;


import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yunji.handler.YunJiApiFactory;
import com.yunji.handler.api.ICallback;
import com.yunji.handler.api.IResultCallback;
import com.yunji.handler.api.IYunJiAPIManger;
import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;
import com.yunji.handler.vo.AllRobotStatusVO;
import com.yunji.handler.vo.CustTaskStatusVO;
import com.yunji.handler.vo.DoorStatusVO;
import com.yunji.handler.vo.EStopVO;
import com.yunji.handler.vo.LangVO;
import com.yunji.handler.vo.TopicSubcribeVO;
import com.yunji.handler.vo.MarkerVO;
import com.yunji.handler.vo.PowerStatusVO;
import com.yunji.handler.vo.RobotInfoVO;
import com.yunji.handler.vo.TaskStatusVO;
import com.yunji.handler.vo.VolumeVO;
import com.yunji.sdk.application.YunJiApplication;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class MainActivity extends Activity
{

    private CommonLog commonLog = LogFactory.createLog(MainActivity.class);

    private  Boolean isSubcrible = true;

    private Button openBtn;

    private Button closeBtn;

    private Button cancel;

    private Button moveBtn;

    private Button forceChargeBtn;

    private Button taskStatusBtn;

    private Button getAllPointBtn;

//    private Button settargetWitdhCallbackBtn;
//
//    private Button settargetNobackWithCallbackBtn;
//
//    private Button flowBtn;

//    private Button setTargetNoBackBtn;

//    private Button getLangBtn;

    private Button setLangBtn;

//    private Button getVolumeBtn;

    private Button setVolumeBtn;

//    private Button getHotelIdBtn;

//    private Button getAdEnableBtn;

    private Button setAdEnableBtn;

//    private Button getRobotNoBtn;

//    private Button getVersionBtn;

    private Button payBtn;

   // private Button startCruiseBtn;

    private Button subcribeBtn;

    private Button robotInfoBtn;



    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subcribeBtn = (Button)findViewById(R.id.action_setting_btn);
        subcribeBtn.setText("取消订阅广播");
        subcribeBtn.setOnClickListener(new View.OnClickListener()
           {
               public void onClick(View v)
               {
                   if (isSubcrible)
                   {
                       unSubcribleHandler();
                       subcribeBtn.setText("订阅广播");
                   }
                   else
                   {
                       subcribeBtn.setText("取消订阅广播");
                       subcribeHandler();
                   }
                   isSubcrible = !isSubcrible;
                   commonLog.d("Start subcribe socket event");

               }
            });
        openBtn = (Button) findViewById(R.id.open_action_btn);
        openBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                YunJiApplication.apiManger.openHandler(new IResultCallback<String>()
                {

                    public void onSuccess(String t)
                    {
                        //t=0 成功
                        commonLog.d("openHandler="+t);
                    }

                    @Override
                    public void onError(String msg)
                    {
                        commonLog.d(msg);
                    }
                });

            }
        });
        closeBtn = (Button) findViewById(R.id.close_action_btn);
        closeBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                YunJiApplication.apiManger.closeHandler(new IResultCallback<String>()
                {

                    public void onSuccess(String t)
                    {
                        //t=0 成功
                        commonLog.d("closeHandler="+t);
                    }

                    @Override
                    public void onError(String msg)
                    {
                        commonLog.d("closeHandler="+msg);
                    }
                });
            }
        });

        moveBtn = (Button)findViewById(R.id.move_action_btn);
        moveBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                String marker ="001";
                YunJiApplication.apiManger.moveHandler(marker, new IResultCallback<String>()
                {

                    public void onSuccess(String t)
                    {
                        //t=0 成功
                        commonLog.d("moveHandler="+t);
                    }

                    @Override
                    public void onError(String msg)
                    {
                        commonLog.d("moveHandler="+msg);
                    }
                });
            }
        });

        cancel = (Button) findViewById(R.id.cancel_action_btn);
        cancel.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                YunJiApplication.apiManger.cancelTaskHandler(new IResultCallback<String>()
                {

                    public void onSuccess(String t)
                    {
                        //t=0 成功
                        commonLog.d("cancelTaskHandler="+t);
                    }

                    @Override
                    public void onError(String msg)
                    {
                        commonLog.d("cancelTaskHandler="+msg);
                    }
                });

            }
        });

        taskStatusBtn = (Button) findViewById(R.id.task_status_action_btn);
        taskStatusBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                YunJiApplication.apiManger.getTaskStatusHandler(new IResultCallback<CustTaskStatusVO>()
                {

                    public void onSuccess(CustTaskStatusVO t)
                    {
                        commonLog.d("getTaskStatusHandler="+t);

                    }

                    public void onError(String msg)
                    {
                        commonLog.d("getTaskStatusHandler="+msg);
                    }
                });
            }
        });

        forceChargeBtn = (Button) findViewById(R.id.force_charge_btn);
        forceChargeBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                YunJiApplication.apiManger.forceChargeHandler(new IResultCallback<String>()
                {

                    public void onSuccess(String t)
                    {
                        //t=0 成功
                        commonLog.d("forceChargeHandler="+t);
                    }

                    @Override
                    public void onError(String msg)
                    {
                        commonLog.d("forceChargeHandler="+msg);
                    }
                });

            }
        });

        getAllPointBtn = (Button) findViewById(R.id.get_all_point_btn);
        getAllPointBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                YunJiApplication.apiManger.queryMarkerList(new IResultCallback<List<MarkerVO>>()
                {

                    @Override
                    public void onSuccess(List<MarkerVO> t)
                    {
                        //
                        commonLog.d("queryMarkerList="+t);

                    }

                    @Override
                    public void onError(String msg)
                    {
                        commonLog.d("queryMarkerList="+msg);

                    }
                });

            }
        });

//        settargetWitdhCallbackBtn = (Button) findViewById(R.id.settarget_witdh_callback_btn);
//        settargetWitdhCallbackBtn.setOnClickListener(new View.OnClickListener()
//        {
//
//
//            public void onClick(View view)
//            {
//                String marker="001";//点位名称
//                String startCallback="";//选填
//                String successCallback="";//选填
//                String faildCallback="";//选填
//                YunJiApplication.apiManger.excuteTaskWithCallbackHandler(marker, startCallback, successCallback, faildCallback, new IResultCallback<String>()
//                {
//
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//
//                    public void onSuccess(String s)
//                    {
//                        commonLog.d("excuteTaskWithCallbackHandler="+s);
//                    }
//                });
//            }
//        });
//
//        settargetNobackWithCallbackBtn = (Button) findViewById(R.id.settarget_noback_with_callback_btn);
//        settargetNobackWithCallbackBtn.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view)
//            {
//                String marker="001";//点位名称
//                String startCallback="";//选填
//                String successCallback="";//选填
//                String faildCallback="";//选填
//                YunJiApplication.apiManger.excuteTaskNobackWithCallbackHandler(marker, startCallback, successCallback, faildCallback, new IResultCallback<String>()
//                {
//
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//
//                    public void onSuccess(String s)
//                    {
//                        commonLog.d("excuteTaskWithCallbackHandler="+s);
//                    }
//                });
//            }
//        });
//
//        flowBtn = (Button) findViewById(R.id.flow_btn);
//        flowBtn.setOnClickListener(new View.OnClickListener()
//        {
//
//            public void onClick(View view)
//            {
//                String taskType = "multiple_transport";
//                JsonArray jsonValues = Json.createArrayBuilder().add("001").add("002").build();
//                YunJiApplication.apiManger.flowHandler(taskType, jsonValues, new IResultCallback<String>()
//                {
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//
//                    public void onSuccess(String s)
//                    {
//                        //s=0 成功
//                        commonLog.d("flowHandler="+s);
//
//                    }
//                });
//
//            }
//        });

//        setTargetNoBackBtn = (Button) findViewById(R.id.settarget_noback_btn);
//        setTargetNoBackBtn.setOnClickListener(new View.OnClickListener()
//        {
//
//            public void onClick(View view)
//            {
//                String marker="001";//点位名称
//                YunJiApplication.apiManger.excuteTaskWithNoBackHandler(marker, new IResultCallback<String>()
//                {
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//
//                    public void onSuccess(String s)
//                    {
//                        //s=0 成功
//                        commonLog.d("flowHandler="+s);
//
//                    }
//                });
//            }
//        });


//        getLangBtn = (Button) findViewById(R.id.get_lang_btn);
//        getLangBtn.setOnClickListener(new View.OnClickListener()
//        {
//
//            public void onClick(View view)
//            {
//                YunJiApplication.apiManger.getLangHandler(new IResultCallback<LangVO>()
//                {
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//                    public void onSuccess(LangVO langVO)
//                    {
//                        commonLog.d("getLangHandler="+langVO.getLang());
//
//                    }
//                });
//
//            }
//        });

        setLangBtn = (Button) findViewById(R.id.set_lang_btn);
        setLangBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                String lang = "zh";//"lang": "zh"(目前支持zh,en,ko_KR)
                YunJiApplication.apiManger.setLangHandler(lang, new IResultCallback<String>()
                {

                    public void onError(String msg)
                    {
                        commonLog.d(msg);
                    }

                    //s=0 成功
                    public void onSuccess(String s)
                    {
                        commonLog.d("setLangHandler="+s);
                    }
                });

            }
        });

//        getVolumeBtn = (Button) findViewById(R.id.get_volume_btn);
//        getVolumeBtn.setOnClickListener(new View.OnClickListener()
//        {
//
//            public void onClick(View view)
//            {
//                YunJiApplication.apiManger.getVolumeHandler(new IResultCallback<VolumeVO>()
//                {
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//
//                    public void onSuccess(VolumeVO volumeVO)
//                    {
//                        commonLog.d("getVolume="+volumeVO.getDayVolume()+":"+volumeVO.getNightVolume());
//                    }
//                });
//
//            }
//        });

        setVolumeBtn = (Button) findViewById(R.id.set_volume_btn);
        setVolumeBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                String dayVolumne = "50%";
                String nightVolume = "50%";
                YunJiApplication.apiManger.setVolumeHandler(dayVolumne, nightVolume, new IResultCallback<String>()
                {

                    public void onError(String msg)
                    {
                        commonLog.d(msg);
                    }


                    //s=0//成功
                    public void onSuccess(String s)
                    {
                        commonLog.d("setVolumeHandler="+s);
                    }
                });

            }
        });

//        getHotelIdBtn = (Button) findViewById(R.id.get_hotel_id_btn);
//        getHotelIdBtn.setOnClickListener(new View.OnClickListener()
//        {
//
//            public void onClick(View view)
//            {
//                YunJiApplication.apiManger.getHotelIdHandler(new IResultCallback<String>()
//                {
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//
//                    //s=hotel id
//                    public void onSuccess(String s)
//                    {
//                        commonLog.d("getHotelIdHandler="+s);
//                    }
//                });
//
//            }
//        });

//        getAdEnableBtn = (Button) findViewById(R.id.get_ad_enable_btn);
//        getAdEnableBtn.setOnClickListener(new View.OnClickListener()
//        {
//
//            public void onClick(View view)
//            {
//                YunJiApplication.apiManger.getAdEnableHandler(new IResultCallback<Boolean>()
//                {
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//
//                    //aBoolean= true or false
//                    public void onSuccess(Boolean aBoolean)
//                    {
//                        commonLog.d("getAdEnableHandler="+aBoolean);
//
//                    }
//                });
//
//            }
//        });

        setAdEnableBtn = (Button) findViewById(R.id.set_ad_enable_btn);
        setAdEnableBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                Boolean adEnabled = true;
                YunJiApplication.apiManger.setAdEnableHandler(adEnabled, new IResultCallback<String>()
                {

                    public void onError(String msg)
                    {
                        commonLog.d("setAdEnableHandler="+msg);
                    }


                    //s=0 成功
                    public void onSuccess(String s)
                    {
                        commonLog.d("setAdEnableHandler="+s);
                    }
                });

            }
        });

//        getRobotNoBtn = (Button) findViewById(R.id.get_robot_no_btn);
//        getRobotNoBtn.setOnClickListener(new View.OnClickListener()
//        {
//
//            public void onClick(View view)
//            {
//
//                YunJiApplication.apiManger.getRobotNoHandler(new IResultCallback<String>()
//                {
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//
//                    public void onSuccess(String s)
//                    {
//                        commonLog.d("getRobotNoHandler="+s);
//                    }
//                });
//            }
//        });
//
//        getVersionBtn = (Button) findViewById(R.id.get_version_btn);
//        getVersionBtn.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View view)
//            {
//                YunJiApplication.apiManger.getVersionHandler(new IResultCallback<String>()
//                {
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d(msg);
//                    }
//
//                    // s=版本号
//                    public void onSuccess(String s)
//                    {
//                        commonLog.d("getVersionHandler="+s);
//                    }
//                });
//            }
//        });

        payBtn = (Button) findViewById(R.id.pay_btn);
        payBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                String text="Jordan你好";
                YunJiApplication.apiManger.playHandler(text, new IResultCallback<String>()
                {

                    public void onError(String msg)
                    {
                        commonLog.d("playHandler="+msg);
                    }


                    //s=0成功
                    public void onSuccess(String s)
                    {
                        commonLog.d("playHandler="+s);
                    }
                });

            }
        });

//        startCruiseBtn = (Button) findViewById(R.id.start_cruise_btn);
//        startCruiseBtn.setOnClickListener(new View.OnClickListener()
//        {
//
//            public void onClick(View view)
//            {
//
//                String type = "time";//"type": "time" or "times", //按时间或者按次对应一下某个字段生效
//                int time = 10; //(int)分钟
//                int times = 5;//  "times": 5,    //(int)次
//                String name ="default"; //目前传default即可
//                String soundType = "sync";//"sync" or "async",  // 说完再走 或 边说边走
//                JsonObject target1 = Json.createObjectBuilder().add("marker","002").add("sound","cruise_测试1").build();
//                JsonObject target2 = Json.createObjectBuilder().add("marker","003").add("sound","cruise_测试2").build();
//                JsonArray targets = Json.createArrayBuilder().add(target1).add(target2).build();//"targets": [{"marker": "002", "sound": "cruise_测试1"}, ...]   //点位名称和要说的语音文件
//                YunJiApplication.apiManger.startCruiseHandler(type, time, times, name, soundType, targets, new IResultCallback<String>()
//                {
//
//                    public void onError(String msg)
//                    {
//                        commonLog.d("startCruiseHandler="+msg);
//                    }
//
//
//                    //S=0 成功
//                    public void onSuccess(String s)
//                    {
//                        commonLog.d("startCruiseHandler="+s);
//                    }
//                });
//
//            }
//        });

        robotInfoBtn = (Button)findViewById(R.id.robot_info_btn);
        robotInfoBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {

                YunJiApplication.apiManger.getRobotInfo(new IResultCallback<RobotInfoVO>()
                {
                    @Override
                    public void onError(String msg)
                    {
                        commonLog.d("getRobotInfo="+msg);
                    }

                    @Override
                    public void onSuccess(RobotInfoVO robotInfoVO)
                    {
                        commonLog.d("getRobotInfo="+robotInfoVO.toString());
                    }
                });
            }
        });

        registerBroadcastHandler();

    }



    public  void registerBroadcastHandler()
    {
        registerDoorStatusBroadCasts();
        registerEStopBroadCasts();
        registerPowerStatusBroadCasts();
        registerTaskStatusBroadCasts();
        registerRobotStatusBroadCasts();
        registerRobotStatusBroadCastsOnA();
    }


///////订阅Demo,支持不同场景的订阅返回处理

    public void registerRobotStatusBroadCasts()
    {
        YunJiApplication.apiManger.registerRobotStatusBroadCasts("registerRobotStatusBroadCasts",new ICallback<AllRobotStatusVO>()
        {
            public  void onSuccess(AllRobotStatusVO vo)
            {
                commonLog.d("registerRobotStatusBroadCasts===="+vo.toString());
            }
        });

    }


    public void registerRobotStatusBroadCastsOnA()
    {
        YunJiApplication.apiManger.registerRobotStatusBroadCasts("registerRobotStatusBroadCastsOnA",new ICallback<AllRobotStatusVO>()
        {
            public  void onSuccess(AllRobotStatusVO vo)
            {
                commonLog.d("registerRobotStatusBroadCastsOnA===="+vo.toString());
            }
        });

    }

    // 注册获取任务状态
    public  void registerTaskStatusBroadCasts()
    {

        YunJiApplication.apiManger.registerTaskStatusBroadCasts("registerTaskStatusBroadCasts",new ICallback<TaskStatusVO>()
        {
            public  void onSuccess(TaskStatusVO vo)
            {
                commonLog.d("registerTaskStatusBroadCasts===="+vo.toString());
            }
        });
    }
    // 注册获取门状态
    public  void registerDoorStatusBroadCasts()
    {
        YunJiApplication.apiManger.registerDoorStatusBroadCasts("registerDoorStatusBroadCasts",new ICallback<DoorStatusVO>()
        {
            public  void onSuccess(DoorStatusVO vo)
            {
                commonLog.d("registerDoorStatusBroadCasts===="+vo.toString());
            }
        });
    }
    // 注册获取电源状态
    public  void registerPowerStatusBroadCasts()
    {
        YunJiApplication.apiManger.registerPowerStatusBroadCasts("registerPowerStatusBroadCasts",new ICallback<PowerStatusVO>()
        {
            public  void onSuccess(PowerStatusVO vo)
            {
                commonLog.d("registerPowerStatusBroadCasts===="+vo.toString());
            }
        });
    }
    // 注册获取急停状态
    public  void registerEStopBroadCasts()
    {
        YunJiApplication.apiManger.registerEStopBroadCasts("registerEStopBroadCasts",new ICallback<EStopVO>()
        {
            public  void onSuccess(EStopVO vo)
            {
                commonLog.d("registerEStopBroadCasts===="+vo.toString());
            }
        });

    }
    // 取消注册获取机器人状态
    public void unRegisterRobotStatusBroadCasts()
    {

        YunJiApplication.apiManger.unRegisterRobotStatusBroadCasts("registerRobotStatusBroadCasts");

    }
    // 取消注册获取任务状态
    public  void unRegisterTaskStatusBroadCasts(String broadcastKey)
    {
        YunJiApplication.apiManger.unRegisterTaskStatusBroadCasts("registerTaskStatusBroadCasts");

    }
    // 取消注册获取门状态
    public  void unRegisterDoorStatusBroadCasts(String broadcastKey)
    {
        YunJiApplication.apiManger.unRegisterDoorStatusBroadCasts("registerDoorStatusBroadCasts");

    }
    // 取消注册获取电源状态
    public  void unRegisterPowerStatusBroadCasts(String broadcastKey)
    {
        YunJiApplication.apiManger.unRegisterPowerStatusBroadCasts("registerPowerStatusBroadCasts");
    }
    // 取消注册获取急停状态
    public  void unRegisterEStopBroadCasts(String broadcastKey)
    {
        YunJiApplication.apiManger.unRegisterEStopBroadCasts("registerEStopBroadCasts");

    }
    //根据TopicSubcribeVO配置取消订阅
    public void  subcribeHandler()
    {
        TopicSubcribeVO topicSubcribeVO = new TopicSubcribeVO();
        topicSubcribeVO.setDoorStatusSub(true);
        topicSubcribeVO.seteStopSub(true);
        topicSubcribeVO.setPowerStatusSub(true);
        topicSubcribeVO.setRobotStatusSub(true);
        topicSubcribeVO.setTaskStautsSub(true);
        YunJiApplication.apiManger.subcribeHandler(topicSubcribeVO);

    }
    //根据TopicSubcribeVO配置订阅
    public  void unSubcribleHandler()
    {
        TopicSubcribeVO topicSubcribeVO = new TopicSubcribeVO();
        topicSubcribeVO.setDoorStatusSub(false);
        topicSubcribeVO.seteStopSub(false);
        topicSubcribeVO.setPowerStatusSub(false);
        topicSubcribeVO.setRobotStatusSub(false);
        topicSubcribeVO.setTaskStautsSub(false);
        YunJiApplication.apiManger.unSubcribleHandler(topicSubcribeVO);

    }


}
