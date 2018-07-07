package com.yunji.handler.convert.impl;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.CustTaskStatusVO;

import java.util.List;

import javax.json.JsonObject;

/**
 * Created by vincent on 2018/3/22.
 */

public class CustTaskStatusConvert implements IConverter<CustTaskStatusVO>
{
    @Override
    public CustTaskStatusVO convert(JsonObject result)
    {
        return null;
    }


    public CustTaskStatusVO convert(CustTaskStatusVO custTaskStatusVO, JsonObject result)
    {
        custTaskStatusVO.setOpenCloseFlag(result.getInt("OpenCloseFlag"));
        custTaskStatusVO.setChargeState(result.getInt("charge_state"));
        custTaskStatusVO.setCountdown(result.getInt("countdown"));
        custTaskStatusVO.setCurrentFloor(result.getInt("current_floor"));
        custTaskStatusVO.setDoorCloseCoun(result.getInt("door_close_count"));
        custTaskStatusVO.setDoorOpenCount(result.getInt("door_open_count"));
        custTaskStatusVO.setEstop(result.getInt("estop"));
        custTaskStatusVO.setHotelId(result.getString("hotelid"));
        custTaskStatusVO.setLiftFrom(result.getInt("liftFrom"));
        custTaskStatusVO.setLiftTo(result.getInt("liftTo"));
        custTaskStatusVO.setMessage(result.getString("message"));
        custTaskStatusVO.setNowTime(result.getJsonNumber("now_time").doubleValue());
        custTaskStatusVO.setPowerPercent(result.getJsonNumber("power_percent").doubleValue());
        custTaskStatusVO.setState(result.getString("state"));
        custTaskStatusVO.setStatus(result.getString("status"));
        custTaskStatusVO.setTakeOut(result.getInt("take_out"));
        custTaskStatusVO.setTarget(result.getString("target"));
        custTaskStatusVO.setTargetPassword(result.getString("target_password"));
        custTaskStatusVO.setTaskId(result.getString("taskId"));
        custTaskStatusVO.setTaskType(result.getString("taskType"));
        custTaskStatusVO.setTasks(result.getInt("tasks"));
        custTaskStatusVO.setTotalDistance(result.getJsonNumber("total_distance").doubleValue());
        custTaskStatusVO.setUpdateTime(result.getJsonNumber("update_time").doubleValue());
        return custTaskStatusVO;
    }


    //"update_time":1521643490.50917}
    @Override
    public List<CustTaskStatusVO> convertToList(JsonObject result)
    {
        return null;
    }
}
