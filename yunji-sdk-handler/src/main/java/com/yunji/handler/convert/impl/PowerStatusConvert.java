package com.yunji.handler.convert.impl;

import java.util.List;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.PowerStatusVO;
/**
 * @author vincent
 *
 */
public class PowerStatusConvert implements IConverter<PowerStatusVO>
{

    @Override
    public PowerStatusVO convert(JsonObject result)
    {
        PowerStatusVO powerStatusVO = new PowerStatusVO();
        powerStatusVO.setVoltage(result.getJsonNumber("voltage").doubleValue());
        powerStatusVO.setCurrent(result.getJsonNumber("current").doubleValue());
        powerStatusVO.setPowerPercent(result.getJsonNumber("power_percent").doubleValue());
        return powerStatusVO;
    }

    @Override
    public List<PowerStatusVO> convertToList(JsonObject result)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PowerStatusVO convert(PowerStatusVO powerStatusVO, JsonObject result)
    {
        powerStatusVO.setVoltage(result.getJsonNumber("voltage").doubleValue());
        powerStatusVO.setCurrent(result.getJsonNumber("current").doubleValue());
        powerStatusVO.setPowerPercent(result.getJsonNumber("power_percent").doubleValue());
        return powerStatusVO;
    }

}
