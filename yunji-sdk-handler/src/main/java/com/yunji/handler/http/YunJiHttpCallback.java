package com.yunji.handler.http;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;

import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;
import com.yunji.handler.utils.YunJiUtils;

public abstract class YunJiHttpCallback extends HttpCallBack<String>
{

    private CommonLog commonLog = LogFactory.createLog(YunJiHttpCallback.class);

    public void onSuccess(String result)
    {
        commonLog.d(result);
        JsonObject resultObject = Json.createReader(new StringReader(result)).readObject();
        int code = 0;
        if (resultObject.containsKey("code"))
        {
            code = resultObject.getInt("code");
        }
        else if (result.contains("errcode"))
        {
            code = resultObject.getInt("errcode");
        }
        if (code == 0)
        {
            if (resultObject.containsKey("data"))
            {
                String data =  resultObject.get("data").toString();
                successHandler(YunJiUtils.isBlank(data) ? "0" : data);
                return;
            }
            successHandler("0");
            return;
        }
        errorHandler(resultObject.getString("message"));
    }

    abstract public void successHandler(String result);

    abstract public void errorHandler(String message);





}
