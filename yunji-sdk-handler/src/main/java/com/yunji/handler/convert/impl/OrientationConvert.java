package com.yunji.handler.convert.impl;

import java.util.List;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.OrientationVO;
/**
 * @author vincent
 *
 */
public class OrientationConvert implements IConverter<OrientationVO>
{

    @Override
    public OrientationVO convert(JsonObject result)
    {
        OrientationVO orientationVO = new OrientationVO();
        orientationVO.setX(result.getJsonNumber("x").doubleValue());
        orientationVO.setY(result.getJsonNumber("y").doubleValue());
        orientationVO.setZ(result.getJsonNumber("z").doubleValue());
        orientationVO.setW(result.getJsonNumber("w").doubleValue());
        return orientationVO;
    }

    @Override
    public List<OrientationVO> convertToList(JsonObject result)
    {
        return null;
    }

    @Override
    public OrientationVO convert(OrientationVO orientationVO, JsonObject result)
    {
        orientationVO.setX(result.getJsonNumber("x").doubleValue());
        orientationVO.setY(result.getJsonNumber("y").doubleValue());
        orientationVO.setZ(result.getJsonNumber("z").doubleValue());
        orientationVO.setW(result.getJsonNumber("w").doubleValue());
        return orientationVO;
    }

}
