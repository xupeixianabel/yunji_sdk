package com.yunji.handler.vo;

import java.io.Serializable;

import edu.wpi.rail.jrosbridge.Topic;

/**
 * Created by vincent on 2018/4/27.
 */

public class TopicSubcribeVO implements Serializable
{

    private  boolean robotStatusSub;
    private  boolean taskStautsSub;
    private  boolean doorStatusSub;
    private  boolean powerStatusSub;
    private  boolean eStopSub;

    public boolean isRobotStatusSub()
    {
        return robotStatusSub;
    }

    public void setRobotStatusSub(boolean robotStatusSub)
    {
        this.robotStatusSub = robotStatusSub;
    }

    public boolean isTaskStautsSub()
    {
        return taskStautsSub;
    }

    public void setTaskStautsSub(boolean taskStautsSub)
    {
        this.taskStautsSub = taskStautsSub;
    }

    public boolean isDoorStatusSub()
    {
        return doorStatusSub;
    }

    public void setDoorStatusSub(boolean doorStatusSub)
    {
        this.doorStatusSub = doorStatusSub;
    }

    public boolean isPowerStatusSub()
    {
        return powerStatusSub;
    }

    public void setPowerStatusSub(boolean powerStatusSub)
    {
        this.powerStatusSub = powerStatusSub;
    }

    public boolean iseStopSub()
    {
        return eStopSub;
    }

    public void seteStopSub(boolean eStopSub)
    {
        this.eStopSub = eStopSub;
    }
}
