package com.yunji.handler.api;

import java.util.List;

import javax.json.JsonArray;

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

/**
 * @author vincent
 *
 */

public interface IYunJiAPIManger
{
    //获取授权accessToken
    public void register(final  IResultCallback<String> callback);

    // 开门(返回只表示发出指令，不代表门开成功，可通过门的状态判断门是否开成功)
    public void openHandler(final IResultCallback<String> callback);

    // 关门
    public void closeHandler(final IResultCallback<String> callback);



    // 取消任务
    public void cancelTaskHandler(final IResultCallback<String> callback);

    // 机器人状态
    public void getTaskStatusHandler(final IResultCallback<CustTaskStatusVO> callback);

    // 回充电桩
    public void forceChargeHandler(final IResultCallback<String> callback);

    // 获取所有点位
    public void queryMarkerList(final IResultCallback<List<MarkerVO>> callback);

    //移动点位
    public void  moveHandler(String marker, IResultCallback<String> callback);
    // // 获取所有点位(含坐标)
    // public void queryMarkerPoseListHandler(final IResultCallback<String>
    // callback);

    // 发任务(不自动返程，不打电话，任务结束才会返回)
   // void excuteTaskWithNoBackHandler(String marker, IResultCallback<String> callback);

    // 发任务(自动返程,带回调)
   // void excuteTaskWithCallbackHandler(String marker, String startCallback, String successCallback,
   //             String faildCallback, IResultCallback<String> callback);

    // 发任务(不返程,带回调)
  //  void excuteTaskNobackWithCallbackHandler(String marker, String startCallback, String successCallback,
  //              String faildCallback, IResultCallback<String> callback);

    // 发任务(目前只支持多点送物)
 //   void flowHandler(String taskType, JsonArray message, IResultCallback<String> callback);

    // 获取当前语言
   // public void getLangHandler(final IResultCallback<LangVO> callback);

    // 设置当前语言
    public void setLangHandler(String lang, IResultCallback<String> callback);

    // 获取当前酒店id
  //  public void getHotelIdHandler(final IResultCallback<String> callback);

    // 获取站定语音开关
  //  public void getAdEnableHandler(final IResultCallback<Boolean> callback);

    // ## 设置站定语音开关
    public void setAdEnableHandler(Boolean enable, IResultCallback<String> callback);

    // 获取机器人编号
  //  public void getRobotNoHandler(final IResultCallback<String> callback);

    // 获取声音
  //  public void getVolumeHandler(final IResultCallback<VolumeVO> callback);

    // 设置声音
    public void setVolumeHandler(String dayVolume, String nightVolume, IResultCallback<String> callback);

    // 获取版本
 //   public void getVersionHandler(final IResultCallback<String> callback);

    // 语音
    public void playHandler(String text, IResultCallback<String> callback);

    //获取机器人基本信息
    public void getRobotInfo(final IResultCallback<RobotInfoVO> callback);

    // 开始巡游
//    public void startCruiseHandler(String type, int time, int times, String name, String soundType, JsonArray targets,
//                IResultCallback<String> callback);

    // / subcribe API on Socket pattern////////////


    // 注册获取机器人状
    public void registerRobotStatusBroadCasts(String broadcastKey, ICallback<AllRobotStatusVO> callback);
    // 注册获取任务状态
    public  void registerTaskStatusBroadCasts(String broadcastKey, ICallback<TaskStatusVO> callback);
    // 注册获取门状态
    public  void registerDoorStatusBroadCasts(String broadcastKey, ICallback<DoorStatusVO> callback);
    // 注册获取电源状态
    public  void registerPowerStatusBroadCasts(String broadcastKey, ICallback<PowerStatusVO> callback);
    // 注册获取急停状态
    public  void registerEStopBroadCasts(String broadcastKey, ICallback<EStopVO> callback);

    // 取消注册获取机器人状态
    public void unRegisterRobotStatusBroadCasts(String broadcastKey);
    // 取消注册获取任务状态
    public  void unRegisterTaskStatusBroadCasts(String broadcastKey);
    // 取消注册获取门状态
    public  void unRegisterDoorStatusBroadCasts(String broadcastKey);
    // 取消注册获取电源状态
    public  void unRegisterPowerStatusBroadCasts(String broadcastKey);
    // 取消注册获取急停状态
    public  void unRegisterEStopBroadCasts(String broadcastKey);
    //根据TopicSubcribeVO配置订阅
    public void  unSubcribleHandler(TopicSubcribeVO topicSubcribeVO);
    //根据TopicSubcribeVO配置取消订阅
    public  void subcribeHandler(TopicSubcribeVO topicSubcribeVO);
}




