package com.yunji.handler.convert.impl;

import java.util.List;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.AllRobotStatusVO;
import com.yunji.handler.vo.OrientationVO;
import com.yunji.handler.vo.PoseVO;
import com.yunji.handler.vo.PositionVO;
import com.yunji.handler.vo.PowerStatusVO;
/**
 * @author vincent
 *
 */
public class AllRobotStatusConvert implements IConverter<AllRobotStatusVO>
{

    public AllRobotStatusVO convert(JsonObject result)
    {

        AllRobotStatusVO allRobotStatusVO = new AllRobotStatusVO();
        allRobotStatusVO.setNotificationId(result.getString("notificationId"));
        allRobotStatusVO.setStatus(result.getString("status"));
        result.containsKey(result.getJsonNumber("notifyTime").longValue());
        allRobotStatusVO.setTotalDistance(result.getJsonNumber("total_distance").doubleValue());
        allRobotStatusVO.setUpTime(result.getJsonNumber("uptime").longValue());
        allRobotStatusVO.setTasks(result.getInt("tasks"));
        allRobotStatusVO.setDoorOpenCount(result.getInt("door_open_count"));
        allRobotStatusVO.setDoorCloseCount(result.getInt("door_close_count"));
        JsonObject pose = result.getJsonObject("pose");

        if (pose != null)
        {
            PoseVO poseVO = new PoseVO();
            allRobotStatusVO.setPoseVO(poseVO);
            JsonObject postionJson = pose.getJsonObject("position");
            if (postionJson != null)
            {
                PositionVO positionVO = new PositionVO(postionJson).get();
                poseVO.setPositionVO(positionVO);
            }

            JsonObject orientationJson = pose.getJsonObject("orientation");
            if (orientationJson != null)
            {
                OrientationVO orientationVO = new OrientationVO(orientationJson).get();
                poseVO.setOrientationVO(orientationVO);
            }
        }
        PowerStatusVO powerStatusVO = new PowerStatusVO(result).get();
        allRobotStatusVO.setPowerStatusVO(powerStatusVO);
        allRobotStatusVO.setCurrenFloor(result.getInt("current_floor"));
        return allRobotStatusVO;
    }

    @Override
    public List<AllRobotStatusVO> convertToList(JsonObject result)
    {
        return null;
    }

    public AllRobotStatusVO convert(AllRobotStatusVO allRobotStatusVO, JsonObject result)
    {
        allRobotStatusVO.setNotificationId(result.getString("notificationId"));
        allRobotStatusVO.setStatus(result.getString("status"));
        result.containsKey(result.getJsonNumber("notifyTime").longValue());
        allRobotStatusVO.setTotalDistance(result.getJsonNumber("total_distance").doubleValue());
        allRobotStatusVO.setUpTime(result.getJsonNumber("uptime").longValue());
        allRobotStatusVO.setTasks(result.getInt("tasks"));
        allRobotStatusVO.setDoorOpenCount(result.getInt("door_open_count"));
        allRobotStatusVO.setDoorCloseCount(result.getInt("door_close_count"));
        JsonObject pose = result.getJsonObject("pose");

        if (pose != null)
        {
            PoseVO poseVO = new PoseVO();
            allRobotStatusVO.setPoseVO(poseVO);
            JsonObject postionJson = pose.getJsonObject("position");
            if (postionJson != null)
            {
                PositionVO positionVO = new PositionVO(postionJson).get();
                poseVO.setPositionVO(positionVO);
            }

            JsonObject orientationJson = pose.getJsonObject("orientation");
            if (orientationJson != null)
            {
                OrientationVO orientationVO = new OrientationVO(orientationJson).get();
                poseVO.setOrientationVO(orientationVO);
            }
        }
        PowerStatusVO powerStatusVO = new PowerStatusVO(result).get();
        allRobotStatusVO.setPowerStatusVO(powerStatusVO);
        allRobotStatusVO.setCurrenFloor(result.getInt("current_floor"));
        return allRobotStatusVO;
    }

}
