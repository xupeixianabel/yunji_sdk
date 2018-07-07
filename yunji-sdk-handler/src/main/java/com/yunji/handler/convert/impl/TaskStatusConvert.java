package com.yunji.handler.convert.impl;

import java.util.List;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.TaskStatusVO;
/**
 * @author vincent
 *
 */
public class TaskStatusConvert implements IConverter<TaskStatusVO>
{

    public TaskStatusVO convert(JsonObject result)
    {
        TaskStatusVO taskStatusVO = new TaskStatusVO();
        taskStatusVO.setNotificationId(result.getString("notificationId"));
        taskStatusVO.setEventId(result.getString("eventId"));
        taskStatusVO.setTaskId(result.getString("taskId"));
        taskStatusVO.setParentTaskId(result.getString("parentTaskId"));
        taskStatusVO.setOccurTime(result.getJsonNumber("occurTime").doubleValue());
        taskStatusVO.setNotifyTime(result.getJsonNumber("notifyTime").doubleValue());
        taskStatusVO.setEventType(result.getString("eventType"));
        taskStatusVO.setTaskType(result.getString("taskType"));
        taskStatusVO.setCurrentTarget(result.getString("currentTarget"));
        taskStatusVO.setOtherData(result.getString("otherData"));
        taskStatusVO.setDistance(result.getInt("distance"));
        taskStatusVO.setBeginTime(result.getJsonNumber("beginTime").doubleValue());
        taskStatusVO.setEndTime(result.getJsonNumber("endTime").doubleValue());
        taskStatusVO.setRetryTimes(result.getInt("retryTimes"));
        taskStatusVO.setChargeResult(result.getInt("chargeResult"));
        taskStatusVO.setBackTask(result.getBoolean("backTask"));
        taskStatusVO.setLang(result.getString("lang"));
        taskStatusVO.setFlowId(result.getString("flowId"));
        taskStatusVO.setPoseAdjusting(result.getBoolean("poseAdjusting"));
        taskStatusVO.setLiftFrom(result.getInt("liftFrom"));
        taskStatusVO.setLiftTo(result.getInt("liftTo"));
        taskStatusVO.setCountdown(result.getInt("countdown"));
        return taskStatusVO;
    }

    @Override
    public List<TaskStatusVO> convertToList(JsonObject result)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TaskStatusVO convert(TaskStatusVO taskStatusVO, JsonObject result)
    {
        taskStatusVO.setNotificationId(result.getString("notificationId"));
        taskStatusVO.setEventId(result.getString("eventId"));
        taskStatusVO.setTaskId(result.getString("taskId"));
        taskStatusVO.setParentTaskId(result.getString("parentTaskId"));
        taskStatusVO.setOccurTime(result.getJsonNumber("occurTime").doubleValue());
        taskStatusVO.setNotifyTime(result.getJsonNumber("notifyTime").doubleValue());
        taskStatusVO.setEventType(result.getString("eventType"));
        taskStatusVO.setTaskType(result.getString("taskType"));
        taskStatusVO.setCurrentTarget(result.getString("currentTarget"));
        taskStatusVO.setOtherData(result.getString("otherData"));
        taskStatusVO.setDistance(result.getInt("distance"));
        taskStatusVO.setBeginTime(result.getJsonNumber("beginTime").doubleValue());
        taskStatusVO.setEndTime(result.getJsonNumber("endTime").doubleValue());
        taskStatusVO.setRetryTimes(result.getInt("retryTimes"));
        taskStatusVO.setChargeResult(result.getInt("chargeResult"));
        taskStatusVO.setBackTask(result.getBoolean("backTask"));
        taskStatusVO.setLang(result.getString("lang"));
        taskStatusVO.setFlowId(result.getString("flowId"));
        taskStatusVO.setPoseAdjusting(result.getBoolean("poseAdjusting"));
        taskStatusVO.setLiftFrom(result.getInt("liftFrom"));
        taskStatusVO.setLiftTo(result.getInt("liftTo"));
        taskStatusVO.setCountdown(result.getInt("countdown"));
        return taskStatusVO;
    }

}
