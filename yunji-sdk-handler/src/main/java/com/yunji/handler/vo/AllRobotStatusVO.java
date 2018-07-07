package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.impl.AllRobotStatusConvert;

public class AllRobotStatusVO extends AbsRobotStatusVO<AllRobotStatusVO>
{

    private String notificationId;

    private long notifyTime;

    private long upTime;

    private PoseVO poseVO;

    private PowerStatusVO powerStatusVO;

    private int currenFloor;

    public AllRobotStatusVO()
    {
    }

    public AllRobotStatusVO(JsonObject json)
    {
        super(json, new AllRobotStatusConvert());
        convertHandler(this);
    }

    public String getNotificationId()
    {
        return notificationId;
    }

    public void setNotificationId(String notificationId)
    {
        this.notificationId = notificationId;
    }

    public long getNotifyTime()
    {
        return notifyTime;
    }

    public void setNotifyTime(long notifyTime)
    {
        this.notifyTime = notifyTime;
    }

    public long getUpTime()
    {
        return upTime;
    }

    public void setUpTime(long upTime)
    {
        this.upTime = upTime;
    }

    public PoseVO getPoseVO()
    {
        return poseVO;
    }

    public void setPoseVO(PoseVO poseVO)
    {
        this.poseVO = poseVO;
    }

    public PowerStatusVO getPowerStatusVO()
    {
        return powerStatusVO;
    }

    public void setPowerStatusVO(PowerStatusVO powerStatusVO)
    {
        this.powerStatusVO = powerStatusVO;
    }

    public int getCurrenFloor()
    {
        return currenFloor;
    }

    public void setCurrenFloor(int currenFloor)
    {
        this.currenFloor = currenFloor;
    }
}
