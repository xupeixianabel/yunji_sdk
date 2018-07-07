package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.impl.EStopConvert;

public class EStopVO extends EntityIdVO<EStopVO>
{
    private int status; //// 持续收到2表示急停中，收不到表示没有急停

    public EStopVO()
    {

    }

    public EStopVO(JsonObject jsonObject)
    {
        super(jsonObject, new EStopConvert());
        convertHandler(this);
    }



    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
