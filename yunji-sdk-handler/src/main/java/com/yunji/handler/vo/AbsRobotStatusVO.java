package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;

public class AbsRobotStatusVO<T> extends EntityIdVO<T>
{

    // 开门状态 0->开门中 1->门已开 2-> 关门中, 3->已关门
    private int openCloseFlag;

    // 充电状态
    private int chargeState;

    // 关门次数
    private int doorCloseCount;

    // 开门次数
    private int doorOpenCount;

    // 是否急停
    private boolean estop;

    //
    private String message;

    // 剩余电量
    private String powerPercent;

    // 任务状态/
    private String state;

    // 运营状态 before_transport等待任务 in_transport 任务中 complate_transport任务完成 back返回中
    // task_failed任务失败或取消
    private String status;

    // 当前任务目标
    private String target;

    // 总任务数
    private int tasks;

    // 总里程
    private double totalDistance;

    public AbsRobotStatusVO()
    {
    }

    public AbsRobotStatusVO(JsonObject json, IConverter<T> converter)
    {
        super(json, converter);
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

    public int getDoorCloseCount()
    {
        return doorCloseCount;
    }

    public void setDoorCloseCount(int doorCloseCount)
    {
        this.doorCloseCount = doorCloseCount;
    }

    public int getDoorOpenCount()
    {
        return doorOpenCount;
    }

    public void setDoorOpenCount(int doorOpenCount)
    {
        this.doorOpenCount = doorOpenCount;
    }

    public boolean isEstop()
    {
        return estop;
    }

    public void setEstop(boolean estop)
    {
        this.estop = estop;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getPowerPercent()
    {
        return powerPercent;
    }

    public void setPowerPercent(String powerPercent)
    {
        this.powerPercent = powerPercent;
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

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public int getTasks()
    {
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
}
