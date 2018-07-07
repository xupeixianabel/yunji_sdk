package com.yunji.handler.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * @author vincent
 *
 */
public class InflatingEntity extends HttpEntityWrapper
{

    public InflatingEntity(HttpEntity wrapped)
    {
        super(wrapped);
    }

    @Override
    public InputStream getContent() throws IOException
    {
        return new GZIPInputStream(wrappedEntity.getContent());
    }

    @Override
    public long getContentLength()
    {
        return -1;
    }
}
