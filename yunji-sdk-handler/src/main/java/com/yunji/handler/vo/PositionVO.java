package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.impl.PositionConvert;

public class PositionVO extends CoordinateVO<PositionVO>
{

    public PositionVO()
    {
    }

    public PositionVO(JsonObject json)
    {
        super(json, new PositionConvert());
        convertHandler(this);
    }

}
