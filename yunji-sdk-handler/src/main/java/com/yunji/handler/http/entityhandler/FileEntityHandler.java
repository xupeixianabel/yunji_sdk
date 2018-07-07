package com.yunji.handler.http.entityhandler;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

/**
 * @author vincent
 *
 */
public class FileEntityHandler
{

    private boolean mStop = false;

    public boolean isStop()
    {
        return mStop;
    }

    public void setStop(boolean stop)
    {
        this.mStop = stop;
    }

    @SuppressWarnings("resource")
    public Object handleEntity(HttpEntity entity, EntityCallBack callback, String target, boolean isResume,
                String chartSet) throws IOException
    {
        Header contentType = entity.getContentType();
        if (TextUtils.isEmpty(target) || target.trim().length() == 0)
            return null;
        File targetFile = new File(target);
        if (!targetFile.exists())
        {
            targetFile.createNewFile();
        }
        if (mStop)
        {
            return targetFile;
        }
        if (entity.isChunked())
        {
            byte[] content = EntityUtils.toByteArray(entity);
            return contentHandler(content, targetFile);
        }
        if (contentType != null)
        {
            String typeValue = contentType.getValue();
            if (typeValue.equals("text/plain") || typeValue.equals("text/html") || typeValue.equals("text/xml"))
            {
                if (typeValue.equals("text/html"))
                {
                    chartSet = "utf-8";
                }
                StringEntityHandler entityHandler = new StringEntityHandler();
                String result = (String) entityHandler.handleEntity(entity, callback, chartSet);
                return saveHandler(result, targetFile);
            }
        }

        long current = 0;
        FileOutputStream os = null;
        current = isResume ? targetFile.length() : 0;
        os = isResume ? new FileOutputStream(target, true) : new FileOutputStream(target);
        if (mStop)
        {
            return targetFile;
        }
        InputStream input = entity.getContent();

        long count = entity.getContentLength() + current;
        if (current >= count || mStop)
        {
            return targetFile;
        }
        int readLen = 0;
        byte[] buffer = new byte[1024];
        while (!mStop && !(current >= count) && ((readLen = input.read(buffer, 0, 1024)) > 0))
        {
            os.write(buffer, 0, readLen);
            current += readLen;
            callback.callBack(count, current, false);
        }
        callback.callBack(count, current, true);
        if (mStop && current < count)
        {
            throw new IOException("user stop download thread");
        }
        return targetFile;
    }

    private File saveHandler(String content, File targetFile)
    {
        byte[] b = content.getBytes();
        return contentHandler(b, targetFile);
    }

    private File contentHandler1(ByteArrayOutputStream arrayOutputStream, File targetFile)
    {
        try
        {
            FileOutputStream fstream = new FileOutputStream(targetFile);
            arrayOutputStream.writeTo(fstream);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (arrayOutputStream != null)
            {
                try
                {
                    arrayOutputStream.close();
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
        return targetFile;
    }

    private File contentHandler(byte[] b, File targetFile)
    {
        BufferedOutputStream stream = null;
        try
        {
            FileOutputStream fstream = new FileOutputStream(targetFile);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
        return targetFile;
    }

    public String convertCodeAndGetText(String content)
    {
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(content.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osr;
        try
        {
            osr = new OutputStreamWriter(baos, "unicode");
            int temp = 0;
            while ((temp = isr.read()) != -1)
            {
                osr.write(temp);
            }
            osr.flush();
            osr.close();
            isr.close();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return baos.toString();
    }

}
