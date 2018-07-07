package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.impl.DoorStatusConvert;

public class DoorStatusVO extends EntityIdVO<DoorStatusVO>
{

    private int openCloseFlag;// 当前门状态（0->开门中,1->开,2->关门中,3->关）

    private int errorCode;

    private int takeOut;

    public DoorStatusVO()
    {
    }

    public DoorStatusVO(JsonObject json)
    {
        super(json, new DoorStatusConvert());
        convertHandler(this);
    }

    public int getOpenCloseFlag()
    {
        return openCloseFlag;
    }

    public void setOpenCloseFlag(int openCloseFlag)
    {
        this.openCloseFlag = openCloseFlag;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public int getTakeOut()
    {
        return takeOut;
    }

    public void setTakeOut(int takeOut)
    {
        this.takeOut = takeOut;
    }

}
