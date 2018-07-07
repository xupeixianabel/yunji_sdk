package com.yunji.handler.convert.impl;

import java.util.List;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.EStopVO;
/**
 * @author vincent
 *
 */
public class EStopConvert implements IConverter<EStopVO>
{

    public EStopVO convert(JsonObject result)
    {
        EStopVO eStopVO = new EStopVO();
        eStopVO.setStatus(result.getInt("data"));
        return eStopVO;
    }

    @Override
    public List<EStopVO> convertToList(JsonObject result)
    {
        return null;
    }

    public EStopVO convert(EStopVO eStopVO, JsonObject result)
    {
        eStopVO.setStatus(result.getInt("data"));
        return eStopVO;
    }

}
