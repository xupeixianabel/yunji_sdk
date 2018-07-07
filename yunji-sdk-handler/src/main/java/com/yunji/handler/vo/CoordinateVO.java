package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;

public class CoordinateVO<T> extends EntityIdVO<T>
{

    private double x;

    private double y;

    private double z;

    public CoordinateVO()
    {
    }

    public CoordinateVO(JsonObject json, IConverter<T> converter)
    {
        super(json, converter);

    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
    }

}
