package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.impl.RobotStatusConvert;

public class RobotStatusVO extends AbsRobotStatusVO<RobotStatusVO>
{

    public RobotStatusVO()
    {

    }

    public RobotStatusVO(JsonObject json)
    {
        super(json, new RobotStatusConvert());
        convertHandler(this);
    }

}
