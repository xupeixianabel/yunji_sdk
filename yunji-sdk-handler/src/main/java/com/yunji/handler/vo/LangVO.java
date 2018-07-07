package com.yunji.handler.vo;

import com.yunji.handler.convert.impl.LangConvert;

import javax.json.JsonObject;

/**
 * Created by vincent on 2018/3/22.
 */

public class LangVO extends EntityIdVO<LangVO>
{
    private String lang;


    public LangVO()
    {

    }

    public LangVO(JsonObject jsonObject)
    {
        super(jsonObject,new LangConvert());
        convertHandler(this);
    }


    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }


}
