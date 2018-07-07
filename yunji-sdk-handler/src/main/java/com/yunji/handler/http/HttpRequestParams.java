package com.yunji.handler.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.yunji.handler.http.entity.MultipartEntity;
import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;

/**
 * @author vincent
 *
 */
public class HttpRequestParams
{

    CommonLog logger = LogFactory.createLog("HttpRequestParams");

    private static String ENCODING = HTTP.UTF_8;

    protected ConcurrentMap<String, Object> urlParams;

    protected ConcurrentMap<String, FileWrapper> fileParams;

    public HttpRequestParams()
    {
        init();
    }

    public HttpRequestParams(Map<String, String> source)
    {
        init();

        for (Map.Entry<String, String> entry : source.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
    }

    public HttpRequestParams(String key, Object value)
    {
        init();
        put(key, value);
    }

    public Boolean containsKey(String key)
    {
        return urlParams.containsKey(key);
    }

    public HttpRequestParams(Object... keysAndValues)
    {
        init();
        int len = keysAndValues.length;
        if (len % 2 != 0)
            throw new IllegalArgumentException("Supplied arguments must be even");
        for (int i = 0; i < len; i += 2)
        {
            String key = String.valueOf(keysAndValues[i]);
            String val = String.valueOf(keysAndValues[i + 1]);
            put(key, val);
        }
    }

    public void put(String key, Object value)
    {
        if (key != null && value != null)
        {
            urlParams.put(key, value);
        }
    }

    public void put(String key, File file) throws FileNotFoundException
    {
        put(key, new FileInputStream(file), file.getName());
    }

    public void put(String key, File file, String contentType) throws FileNotFoundException
    {
        put(key, new FileInputStream(file), file.getName(), contentType);
    }

    public void put(String key, InputStream stream)
    {
        put(key, stream, null);
    }

    public void put(String key, InputStream stream, String fileName)
    {
        put(key, stream, fileName, null);
    }

    /**
     * 添加 inputStream 到请求中.
     * 
     * @param key
     *            the key name for the new param.
     * @param stream
     *            the input stream to add.
     * @param fileName
     *            the name of the file.
     * @param contentType
     *            the content type of the file, eg. application/json
     */
    public void put(String key, InputStream stream, String fileName, String contentType)
    {
        if (key != null && stream != null)
        {
            fileParams.put(key, new FileWrapper(stream, fileName, contentType));
        }
    }

    public void put(String key, String value, String contentType)
    {
        urlParams.put(key, new StringWrapper(value, contentType));

    }

    public void remove(String key)
    {
        urlParams.remove(key);
        fileParams.remove(key);
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, Object> entry : urlParams.entrySet())
        {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet())
        {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }

        return result.toString();
    }

    /**
     * 表单传参方式 Returns an HttpEntity containing all request parameters
     * 
     */
    public HttpEntity getEntity()
    {
        HttpEntity entity = null;

        if (!fileParams.isEmpty())
        {
            MultipartEntity multipartEntity = new MultipartEntity();
            for (ConcurrentHashMap.Entry<String, Object> entry : urlParams.entrySet())
            {
                if (entry.getValue() instanceof StringWrapper)
                {
                    StringWrapper wrapper = (StringWrapper) entry.getValue();
                    if (wrapper.contentType.equals(HttpFacade.MULTIPART_FORM_DATA))
                    {
                        multipartEntity.addPart(entry.getKey(), wrapper.value,
                                    multipartEntity.getContentType().getValue());
                    }
                }
                else
                {
                    multipartEntity.addPart(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            int currentIndex = 0;
            int lastIndex = fileParams.entrySet().size() - 1;
            for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet())
            {
                FileWrapper file = entry.getValue();
                if (file.inputStream != null)
                {
                    boolean isLast = currentIndex == lastIndex;
                    if (file.contentType != null)
                    {
                        if (file.contentType.equals(HttpFacade.MULTIPART_FORM_DATA))
                        {
                            multipartEntity.addPart(entry.getKey(), file.getFileName(), file.inputStream,
                                        multipartEntity.getContentType().getValue(), isLast);
                        }
                        else
                        {
                            multipartEntity.addPart(entry.getKey(), file.getFileName(), file.inputStream,
                                        file.contentType, isLast);
                        }
                    }
                    else
                    {
                        multipartEntity.addPart(entry.getKey(), file.getFileName(), file.inputStream, isLast);
                    }
                }
                currentIndex++;
            }
            return multipartEntity;
        }
        try
        {
            entity = new UrlEncodedFormEntity(getParamsList(), ENCODING);
            UrlEncodedFormEntity formEntity = (UrlEncodedFormEntity) entity;
            formEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, HttpFacade.X_WWW_FORM_URLENCODE));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return entity;
    }

    public MultipartEntity getMultipartEntity()
    {
        MultipartEntity multipartEntity = new MultipartEntity();

        for (ConcurrentHashMap.Entry<String, Object> entry : urlParams.entrySet())
        {
            multipartEntity.addPart(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return multipartEntity;
    }

    // json字符串方式
    public HttpEntity getStringEntity()
    {
        JSONObject jsonObject = new JSONObject(urlParams);
        StringEntity entity = null;
        String paramsStr = jsonObject.toString();
        try
        {
            entity = new StringEntity(paramsStr, HTTP.UTF_8);
            entity.setContentType("application/json");
            entity.setContentEncoding(HTTP.UTF_8);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return entity;
    }

    public HttpEntity getStringEntity(String jsonResult)
    {
        StringEntity entity = null;
        try
        {
            entity = new StringEntity(jsonResult, HTTP.UTF_8);
            entity.setContentType("application/json");
            entity.setContentEncoding(HTTP.UTF_8);
            // logger.e(jsonResult);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return entity;
    }

    public String getRequestString(String requestURL)
    {
        if (urlParams.isEmpty())
        {
            return requestURL;
        }
        StringBuffer result = new StringBuffer(requestURL);
        String lastChar = String.valueOf(requestURL.charAt(requestURL.length() - 1));
        if (!lastChar.equals("/"))
        {
            result.append("/");
        }

        for (String key : urlParams.keySet())
        {
            result.append(urlParams.get(key));
            result.append("/");
        }
        logger.d(result);
        return result.toString();
    }

    public String getUrlWithEncodeQueryString(String requestURL)
    {
        String paramString = getParamString();
        requestURL += "?" + paramString;
        logger.d(requestURL);
        return requestURL;
    }

    private void init()
    {
        urlParams = new ConcurrentHashMap<String, Object>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
    }

    protected List<BasicNameValuePair> getParamsList()
    {
        List<BasicNameValuePair> lparams = new ArrayList<BasicNameValuePair>();

        for (ConcurrentHashMap.Entry<String, Object> entry : urlParams.entrySet())
        {
            lparams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
        }

        return lparams;
    }

    public String getParamString()
    {
        return URLEncodedUtils.format(getParamsList(), ENCODING);
    }

    private static class StringWrapper
    {

        String value;

        String contentType;

        public StringWrapper(String value, String contentType)
        {
            this.value = value;
            this.contentType = contentType;
        }
    }

    private static class FileWrapper
    {

        public InputStream inputStream;

        public String fileName;

        public String contentType;

        public FileWrapper(InputStream inputStream, String fileName, String contentType)
        {
            this.inputStream = inputStream;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public String getFileName()
        {
            if (fileName != null)
            {
                return fileName;
            }
            return null;
        }
    }
}