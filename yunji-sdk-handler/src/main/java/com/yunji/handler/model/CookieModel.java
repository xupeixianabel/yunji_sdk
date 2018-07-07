package com.yunji.handler.model;

import com.yunji.handler.utils.CommonConstants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author vincent
 *
 */
public class CookieModel
{

    private SharedPreferences sharedPreferences;

    private static CookieModel instance;

    private CookieModel(Context context)
    {
        if (sharedPreferences == null)
        {
            sharedPreferences = context.getSharedPreferences(CommonConstants.COOKIE_NAME, Context.MODE_PRIVATE);
        }
    }

    synchronized public static CookieModel init(Context context)
    {
        if (instance == null)
            instance = new CookieModel(context);
        return instance;
    }

    public static CookieModel getInstance()
    {
        return instance;
    }

    public void addCookie(String key, String value)
    {
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 添加cookie
     *
     * @param key
     * @param value
     */
    public void addCookie(String key, int value)
    {
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 添加cookie
     *
     * @param key
     * @param value
     */
    public void addCookie(String key, Boolean value)
    {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 清除所有 Cookie
     */
    public void cleanAllCookie()
    {
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getValueByKey(String key)
    {
        return sharedPreferences.getString(key, null);
    }

    public String getValueByKey(String key, String defaultValue)
    {
        return sharedPreferences.getString(key, defaultValue);
    }

    public int getIntValueByKey(String key)
    {
        return sharedPreferences.getInt(key, 0);
    }

    public Boolean getBooleanValueByKey(String key)
    {
        return sharedPreferences.getBoolean(key, false);
    }

    public Boolean getBooleanValueByKey(String key, boolean defaultValue)
    {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 根据key清除对应value
     *
     * @param key
     */
    public void cleanByKey(String key)
    {
        Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }


    private String object2String(Object object)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try
        {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;

        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private Object string2Object(String objectString)
    {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try
        {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 使用SharedPreference保存对象
     *
     * @param key        储存对象的key
     * @param saveObject 储存的对象
     */
    public void save(String key, Object saveObject)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String string = object2String(saveObject);
        editor.putString(key, string);
        editor.commit();
    }

    /**
     * 获取SharedPreference保存的对象
     * @param key     储存对象的key
     * @return object 返回根据key得到的对象
     */
    public Object get(String key)
    {
        String string = sharedPreferences.getString(key, null);
        if (string != null)
        {
            Object object = string2Object(string);
            return object;
        }
        return null;
    }



}
