package com.yunji.handler.api;
/**
 * @author vincent
 *
 */
public interface IResultCallback<T> extends ICallback<T>
{

    public void onError(String msg);
}
