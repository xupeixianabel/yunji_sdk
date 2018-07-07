package com.yunji.handler.vo;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;

public class EntityIdVO<T> implements Serializable
{

    protected String id;

    private IConverter<T> converter;

    private JsonObject json;

    public EntityIdVO()
    {
    }

    public EntityIdVO(JsonObject json, IConverter<T> converter)
    {
        this.converter = converter;
        this.json = json;

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public T get()
    {
        if (json == null || converter == null)
            return null;
        return converter.convert(json);
    }

    public void convertHandler(T t)
    {
        if (json == null || converter == null)
            return;
        converter.convert(t, json);
    }

    public List<T> getList()
    {
        return converter.convertToList(json);
    }
}
