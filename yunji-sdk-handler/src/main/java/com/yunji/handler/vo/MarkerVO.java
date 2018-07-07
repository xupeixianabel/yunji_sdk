package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.impl.MarkerConvert;

public class MarkerVO extends EntityIdVO<MarkerVO>
{

    private String avatar;

    private int floor;

    private int key;

    private String markerName;

    private PoseVO poseVO;

    public MarkerVO()
    {
    }

    public MarkerVO(JsonObject json)
    {
        super(json, new MarkerConvert());

    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public int getFloor()
    {
        return floor;
    }

    public void setFloor(int floor)
    {
        this.floor = floor;
    }

    public int getKey()
    {
        return key;
    }

    public void setKey(int key)
    {
        this.key = key;
    }

    public String getMarkerName()
    {
        return markerName;
    }

    public void setMarkerName(String markerName)
    {
        this.markerName = markerName;
    }

    public PoseVO getPoseVO()
    {
        return poseVO;
    }

    public void setPoseVO(PoseVO poseVO)
    {
        this.poseVO = poseVO;
    }

}
