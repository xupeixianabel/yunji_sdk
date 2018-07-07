package com.yunji.handler.convert.impl;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.VolumeVO;

import java.util.List;

import javax.json.JsonObject;

/**
 * Created by vincent on 2018/3/22.
 */

public class VolumeConvert implements IConverter<VolumeVO>
{

    public VolumeVO convert(JsonObject result)
    {
        return null;
    }


    public VolumeVO convert(VolumeVO volumeVO, JsonObject result)
    {
        volumeVO.setDayVolume(result.getString("day_volume"));
        volumeVO.setNightVolume(result.getString("night_volume"));
        return volumeVO;
    }


    public List<VolumeVO> convertToList(JsonObject result)
    {
        return null;
    }
}
