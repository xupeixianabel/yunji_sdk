package com.yunji.handler.convert.impl;

import java.util.List;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.DoorStatusVO;
/**
 * @author vincent
 *
 */
public class DoorStatusConvert implements IConverter<DoorStatusVO>
{

    @Override
    public DoorStatusVO convert(JsonObject result)
    {
        DoorStatusVO doorStatusVO = new DoorStatusVO();
        doorStatusVO.setErrorCode(result.getInt("Error_code"));
        doorStatusVO.setTakeOut(result.getInt("take_out"));
        doorStatusVO.setOpenCloseFlag(result.getInt("OpenCloseFlag"));
        return doorStatusVO;
    }

    @Override
    public List<DoorStatusVO> convertToList(JsonObject result)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public DoorStatusVO convert(DoorStatusVO doorStatusVO, JsonObject result)
    {
        doorStatusVO.setErrorCode(result.getInt("Error_code"));
        doorStatusVO.setTakeOut(result.getInt("take_out"));
        doorStatusVO.setOpenCloseFlag(result.getInt("OpenCloseFlag"));
        return doorStatusVO;
    }

}
