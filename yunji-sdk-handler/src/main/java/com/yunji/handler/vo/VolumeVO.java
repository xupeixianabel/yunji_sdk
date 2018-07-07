package com.yunji.handler.vo;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.convert.impl.VolumeConvert;

import java.util.List;

import javax.json.JsonObject;

/**
 * Created by vincent on 2018/3/22.
 */

public class VolumeVO extends  EntityIdVO<VolumeVO>
{
    private String dayVolume;

    private String nightVolume;


    public  VolumeVO()
    {

    }

    public  VolumeVO(JsonObject jsonObject)
    {
        super(jsonObject,new VolumeConvert());
        convertHandler(this);

    }

    public String getDayVolume()
    {
        return dayVolume;
    }

    public void setDayVolume(String dayVolume)
    {
        this.dayVolume = dayVolume;
    }

    public String getNightVolume() {
        return nightVolume;
    }

    public void setNightVolume(String nightVolume)
    {
        this.nightVolume = nightVolume;
    }
}
