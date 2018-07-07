package com.yunji.handler.http;

import java.io.IOException;

/**
 * @author vincent
 *
 */
public class HttpException extends IOException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1710130286674567224L;

    public static final int HOST_CONNECT_EXCTPION_CODE = 1000;

    public static final int UNKNOW_HOST_EXCEPTION_CODE = 1001;

    public static final int TIMEOUT_EXCEPTION_CODE = 1002;

    public static final int IO_EXCEPTION_CODE = 1003;

    public static final int AUTH_EXCEPTION_CODE = 401;

    public static final int NULL_POINT_EXCEPTION_CODE = 1005;

    private int code;

    public HttpException(String msg)
    {
        super(msg);
    }

    public HttpException(String msg, int code, Throwable ex)
    {
        super(msg, ex);
        this.code = code;
    }

    public HttpException(String msg, int code)
    {
        super(msg);
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }
}
