package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.impl.OrientationConvert;

public class OrientationVO extends CoordinateVO<OrientationVO>
{

    public OrientationVO()
    {
    }

    public OrientationVO(JsonObject json)
    {
        super(json, new OrientationConvert());
        convertHandler(this);
    }

    private double w;

    public double getW()
    {
        return w;
    }

    public void setW(double w)
    {
        this.w = w;
    }
}
