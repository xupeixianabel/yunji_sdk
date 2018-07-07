package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.impl.TaskStatusConvert;

public class TaskStatusVO extends EntityIdVO<TaskStatusVO>
{

    private String notificationId;// unique id

    private String eventId; // 可忽略

    private String taskId;// 任务唯一id

    private String parentTaskId; // 上一次任务id

    private double occurTime; // 通知时间（秒）

    private double notifyTime; // 通知时间（秒）

    private String eventType;// 任务状态（"DELIVERY_END"->任务结束等待新任务,
                             // "BATTERY_CHARGING"->充电中,
                             // "ACTION_CANCELED"->任务取消, "DELIVERY_START"->任务开始,
                             // "ACTION_ABNORMAL"->任务失败,
                             // "ITEMS_SERVED_ON"->任务到达, "START_GO_BACK"->任务返回,
                             // "WAIT_OPEN_BOX"->等待开门, "WAIT_CLOSE_BOX" ->
                             // 等待关门,"LEAVE_CHARGE"->下桩, "EXIT_LIFT"->出电梯,
                             // "ENTER_LIFT"->进电梯, "WAIT_LIFT_OUTSIDE"->电梯外等电梯,
                             // "AVOID_LIFT"->让开等待下一部电梯,"WAIT_LIFT_INSIDE"->电梯内等电梯）

    private String taskType;// 任务类型（"goback"->返回充电桩, ""or"transport"->送物,
                            // "guide"->引领, "call"->召唤, "cruise"->巡游,
                            // "test"->测试, "multiple_transport"->多点送物,
                            // "call_to_transport"->召唤送物,
                            // "call_to_getitem"->召唤取物）

    private String currentTarget;// 当前移动目标点

    private String otherData; // 额外数据（一般为空）

    private double distance; // 当前任务里程

    private double beginTime; // 开始时间（秒）

    private double endTime;// 结束时间（秒）

    private int retryTimes; // 移动重试次数

    private int chargeResult;// 上庄结果

    private boolean backTask;// 是否返程任务

    private String lang; // 任务语言（"zh", "en", "ko_KR"）

    private String flowId; // 所属流程id

    private boolean poseAdjusting;// 是否校正中

    private int liftFrom;// 电梯流程中出发楼层

    private int liftTo;// 电梯流程中目标楼层

    private int countdown;// 倒计时（如等待开门，等待关门..的倒计时）

    public TaskStatusVO()
    {
    }

    public TaskStatusVO(JsonObject json)
    {
        super(json, new TaskStatusConvert());
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

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getParentTaskId()
    {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId)
    {
        this.parentTaskId = parentTaskId;
    }

    public double getOccurTime()
    {
        return occurTime;
    }

    public void setOccurTime(double occurTime)
    {
        this.occurTime = occurTime;
    }

    public double getNotifyTime()
    {
        return notifyTime;
    }

    public void setNotifyTime(double notifyTime)
    {
        this.notifyTime = notifyTime;
    }

    public String getEventType()
    {
        return eventType;
    }

    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    public String getTaskType()
    {
        return taskType;
    }

    public void setTaskType(String taskType)
    {
        this.taskType = taskType;
    }

    public String getCurrentTarget()
    {
        return currentTarget;
    }

    public void setCurrentTarget(String currentTarget)
    {
        this.currentTarget = currentTarget;
    }

    public String getOtherData()
    {
        return otherData;
    }

    public void setOtherData(String otherData)
    {
        this.otherData = otherData;
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    public double getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(double beginTime)
    {
        this.beginTime = beginTime;
    }

    public double getEndTime()
    {
        return endTime;
    }

    public void setEndTime(double endTime)
    {
        this.endTime = endTime;
    }

    public int getRetryTimes()
    {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes)
    {
        this.retryTimes = retryTimes;
    }

    public int getChargeResult()
    {
        return chargeResult;
    }

    public void setChargeResult(int chargeResult)
    {
        this.chargeResult = chargeResult;
    }

    public boolean isBackTask()
    {
        return backTask;
    }

    public void setBackTask(boolean backTask)
    {
        this.backTask = backTask;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getFlowId()
    {
        return flowId;
    }

    public void setFlowId(String flowId)
    {
        this.flowId = flowId;
    }

    public boolean isPoseAdjusting()
    {
        return poseAdjusting;
    }

    public void setPoseAdjusting(boolean poseAdjusting)
    {
        this.poseAdjusting = poseAdjusting;
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

    public int getCountdown()
    {
        return countdown;
    }

    public void setCountdown(int countdown)
    {
        this.countdown = countdown;
    }

}
