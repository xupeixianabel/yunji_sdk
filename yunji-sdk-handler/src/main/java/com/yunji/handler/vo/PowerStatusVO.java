package com.yunji.handler.vo;

import javax.json.JsonObject;

import com.yunji.handler.convert.impl.PowerStatusConvert;

public class PowerStatusVO extends EntityIdVO<PowerStatusVO>
{

    private double voltage; // 电压

    private double current; // 电流

    private double powerPercent; // 电量

    private int chargeState; // 充电状态（0->未充电, 1->充电）

    public PowerStatusVO()
    {
    }

    public PowerStatusVO(JsonObject json)
    {
        super(json, new PowerStatusConvert());
        convertHandler(this);
    }

    public double getVoltage()
    {
        return voltage;
    }

    public void setVoltage(double voltage)
    {
        this.voltage = voltage;
    }

    public double getCurrent()
    {
        return current;
    }

    public void setCurrent(double current)
    {
        this.current = current;
    }

    public double getPowerPercent()
    {
        return powerPercent;
    }

    public void setPowerPercent(double powerPercent)
    {
        this.powerPercent = powerPercent;
    }

    public int getChargeState()
    {
        return chargeState;
    }

    public void setChargeState(int chargeState)
    {
        this.chargeState = chargeState;
    }
}
