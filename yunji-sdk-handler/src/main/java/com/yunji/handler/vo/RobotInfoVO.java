package com.yunji.handler.vo;

import com.yunji.handler.convert.impl.PowerStatusConvert;
import com.yunji.handler.convert.impl.RobotInfoConvert;

import javax.json.JsonObject;

/**
 * Created by vincent on 2018/4/9.
 */

public class RobotInfoVO extends EntityIdVO<RobotInfoVO>
{

    private String lang; ////语言（zh, en, ko_KR）

    private String hotelId; ////酒店id

    private Boolean playEnable; //// 定语音开关

    private String sn; ////机器人序列号

    private String dayVolume; ////白天音量（20:00:00-07:59:59）

    private String nightVolume; // //晚上音量(08:00:00-19:59:59)

    private String version; ////机器人软件版本号


    public RobotInfoVO()
    {

    }

    public RobotInfoVO(JsonObject json)
    {
        super(json, new RobotInfoConvert());
        convertHandler(this);
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public Boolean getPlayEnable() {
        return playEnable;
    }

    public void setPlayEnable(Boolean playEnable)
    {
        this.playEnable = playEnable;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public String getDayVolume()
    {
        return dayVolume;
    }

    public void setDayVolume(String dayVolume)
    {
        this.dayVolume = dayVolume;
    }

    public String getNightVolume()
    {
        return nightVolume;
    }

    public void setNightVolume(String nightVolume)
    {
        this.nightVolume = nightVolume;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }
}
