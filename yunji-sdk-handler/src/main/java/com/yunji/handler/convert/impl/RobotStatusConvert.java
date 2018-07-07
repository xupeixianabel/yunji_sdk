package com.yunji.handler.convert.impl;

import java.util.List;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.RobotStatusVO;
/**
 * @author vincent
 *
 */
public class RobotStatusConvert implements IConverter<RobotStatusVO>
{

    public RobotStatusVO convert(JsonObject result)
    {
        RobotStatusVO robotStatusVO = new RobotStatusVO();
        return robotStatusVO;
    }

    public List<RobotStatusVO> convertToList(JsonObject result)
    {

        return null;
    }

    @Override
    public RobotStatusVO convert(RobotStatusVO robotStatusVO, JsonObject result)
    {
        return robotStatusVO;
    }

}
