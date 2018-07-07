package com.yunji.handler.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class YunJiUtils
{

    private static final String LOG_TAG = "YunJiUtils";

    private static CommonLog logger = LogFactory.createLog(LOG_TAG);

    public static boolean validateAPK(Context context, String archiveFilePath)
    {
        boolean result = false;
        try
        {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
            if (info != null)
            {
                return true;
            }
        } catch (Exception e)
        {
            return false;
        }
        return result;
    }

    public static String getMetaDataValue(String name, Context context)
    {
        Object value = null;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try
        {

            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null)
            {
                value = applicationInfo.metaData.get(name);
            }
        } catch (NameNotFoundException e)
        {
            throw new RuntimeException("Could not read the name in the manifest file.", e);
        }
        if (value == null)
        {
            return "";
        }

        return value.toString();
    }

    public static  boolean isBlank(final CharSequence cs)
    {
        int strLen;
        if (cs == null || cs.equals ("\"\"") || (strLen = cs.length()) == 0)
        {
            return true;
        }
        for (int i = 0; i < strLen; i++)
        {
            if (Character.isWhitespace(cs.charAt(i)) == false)
            {
                return false;
            }
        }
        return true;
    }

    public static String getPropertiesValue(Context c, String key)
    {
        String value = null;
        Properties properties = new Properties();
        try
        {
            properties.load(c.getAssets().open(CommonConstants.Config.CONFIG_PROPERTIES));
            value = properties.getProperty(key);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return value;
    }


    public static String Md5(String str)
    {
        if (str != null && !str.equals(""))
        {
            try
            {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < md5Byte.length; i++)
                {
                    sb.append(HEX[(int) (md5Byte[i] & 0xff) / 16]);
                    sb.append(HEX[(int) (md5Byte[i] & 0xff) % 16]);
                }
                str = sb.toString();
            } catch (NoSuchAlgorithmException e)
            {
            } catch (Exception e)
            {
            }
        }
        return str;
    }

}
