package com.yunji.handler.vo;

import com.yunji.handler.convert.impl.CustTaskStatusConvert;

import javax.json.JsonObject;

/**
 * Created by vincent on 2018/3/22.
 */

public class CustTaskStatusVO extends  EntityIdVO<CustTaskStatusVO>
{
    private int openCloseFlag;
    private int chargeState;
    private int countdown;
    private int currentFloor;
    private int doorCloseCoun;
    private int doorOpenCount;
    private int estop;
    private String hotelId;
    private int liftFrom;
    private int liftTo;
    private String message;
    private double nowTime;
    private double powerPercent;
    private String state;
    private String status;
    private int takeOut;
    private String target;
    private String targetPassword;
    private String taskId;
    private String taskType;
    private int tasks;
    private double totalDistance;
    private double updateTime;

    public CustTaskStatusVO()
    {

    }

    public CustTaskStatusVO(JsonObject jsonObject)
    {
        super(jsonObject,new CustTaskStatusConvert());
        convertHandler(this);

    }
    public int getOpenCloseFlag()
    {
        return openCloseFlag;
    }

    public void setOpenCloseFlag(int openCloseFlag)
    {
        this.openCloseFlag = openCloseFlag;
    }

    public int getChargeState()
    {
        return chargeState;
    }

    public void setChargeState(int chargeState)
    {
        this.chargeState = chargeState;
    }

    public int getCountdown()
    {
        return countdown;
    }

    public void setCountdown(int countdown)
    {
        this.countdown = countdown;
    }

    public int getCurrentFloor()
    {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor)
    {
        this.currentFloor = currentFloor;
    }

    public int getDoorCloseCoun()
    {
        return doorCloseCoun;
    }

    public void setDoorCloseCoun(int doorCloseCoun)
    {
        this.doorCloseCoun = doorCloseCoun;
    }

    public int getDoorOpenCount()
    {
        return doorOpenCount;
    }

    public void setDoorOpenCount(int doorOpenCount)
    {
        this.doorOpenCount = doorOpenCount;
    }

    public int getEstop()
    {
        return estop;
    }

    public void setEstop(int estop)
    {
        this.estop = estop;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public int getLiftFrom()
    {
        return liftFrom;
    }

    public void setLiftFrom(int liftFrom)
    {
        this.liftFrom = liftFrom;
    }

    public int getLiftTo()
    {
        return liftTo;
    }

    public void setLiftTo(int liftTo)
    {
        this.liftTo = liftTo;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public double getNowTime()
    {
        return nowTime;
    }

    public void setNowTime(double nowTime)
    {
        this.nowTime = nowTime;
    }



    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public int getTakeOut()
    {
        return takeOut;
    }

    public void setTakeOut(int takeOut)
    {
        this.takeOut = takeOut;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getTargetPassword()
    {
        return targetPassword;
    }

    public void setTargetPassword(String targetPassword)
    {
        this.targetPassword = targetPassword;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskType()
    {
        return taskType;
    }

    public void setTaskType(String taskType)
    {
        this.taskType = taskType;
    }

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks)
    {
        this.tasks = tasks;
    }

    public double getTotalDistance()
    {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance)
    {
        this.totalDistance = totalDistance;
    }

    public double getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(double updateTime)
    {
        this.updateTime = updateTime;
    }

    public double getPowerPercent()
    {
        return powerPercent;
    }

    public void setPowerPercent(double powerPercent)
    {
        this.powerPercent = powerPercent;
    }
}
