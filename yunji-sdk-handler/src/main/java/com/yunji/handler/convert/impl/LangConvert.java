package com.yunji.handler.convert.impl;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.LangVO;

import java.util.List;

import javax.json.JsonObject;

/**
 * Created by vincent on 2018/3/22.
 */

public class LangConvert implements IConverter<LangVO>
{

    public LangVO convert(JsonObject result)
    {
        return null;
    }


    public LangVO convert(LangVO langVO, JsonObject result)
    {
        langVO.setLang(result.getString("lang"));
        return langVO;
    }


    public List<LangVO> convertToList(JsonObject result)
    {
        return null;
    }
}
