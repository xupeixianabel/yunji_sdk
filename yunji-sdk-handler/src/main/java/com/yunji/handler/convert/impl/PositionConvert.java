package com.yunji.handler.convert.impl;

import java.util.List;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.PositionVO;
/**
 * @author vincent
 *
 */
public class PositionConvert implements IConverter<PositionVO>
{

    public PositionVO convert(JsonObject result)
    {
        PositionVO positionVO = new PositionVO();
        positionVO.setX(result.getJsonNumber("x").doubleValue());
        positionVO.setY(result.getJsonNumber("y").doubleValue());
        positionVO.setZ(result.getJsonNumber("z").doubleValue());
        return positionVO;
    }

    @Override
    public List<PositionVO> convertToList(JsonObject result)
    {
        return null;
    }

    public PositionVO convert(PositionVO positionVO, JsonObject result)
    {
        positionVO.setX(result.getJsonNumber("x").doubleValue());
        positionVO.setY(result.getJsonNumber("y").doubleValue());
        positionVO.setZ(result.getJsonNumber("z").doubleValue());
        return positionVO;
    }

}
