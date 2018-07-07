package com.yunji.handler.convert;

import java.util.List;

import javax.json.JsonObject;
/**
 * @author vincent
 *
 */
public interface IConverter<T>
{

    T convert(JsonObject result);

    T convert(T t, JsonObject result);

    List<T> convertToList(JsonObject result);
}
