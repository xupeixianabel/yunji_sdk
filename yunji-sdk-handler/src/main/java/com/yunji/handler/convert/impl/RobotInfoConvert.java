package com.yunji.handler.convert.impl;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.RobotInfoVO;

import java.util.List;

import javax.json.JsonObject;

/**
 * Created by vincent on 2018/4/9.
 */

public class RobotInfoConvert implements IConverter<RobotInfoVO>
{

    @Override
    public RobotInfoVO convert(JsonObject result)
    {
        RobotInfoVO robotInfoVO = new RobotInfoVO();
        robotInfoVO.setDayVolume(result.getString("day_volume"));
        robotInfoVO.setLang(result.getString("lang"));
        robotInfoVO.setHotelId(result.getString("hotelid"));
        robotInfoVO.setPlayEnable(result.getBoolean("ad_paly_enable"));
        robotInfoVO.setNightVolume(result.getString("night_volume"));
        robotInfoVO.setSn(result.getString("sn"));
        robotInfoVO.setVersion(result.getString("version"));
        return robotInfoVO;
    }

    @Override
    public RobotInfoVO convert(RobotInfoVO robotInfoVO, JsonObject result)
    {
        robotInfoVO.setDayVolume(result.getString("day_volume"));
        robotInfoVO.setLang(result.getString("lang"));
        robotInfoVO.setHotelId(result.getString("hotelid"));
        robotInfoVO.setPlayEnable(result.getBoolean("ad_paly_enable"));
        robotInfoVO.setNightVolume(result.getString("night_volume"));
        robotInfoVO.setSn(result.getString("sn"));
        robotInfoVO.setVersion(result.getString("version"));
        return robotInfoVO;
    }

    @Override
    public List convertToList(JsonObject result)
    {
        return null;
    }
}
