package com.yunji.handler.model;

/**
 * @author vincent
 *
 */
public class YunJiModel
{

    private static YunJiModel instance;

    public static void init()
    {
        if (instance == null)
            instance = new YunJiModel();
    }

    public static YunJiModel getInstance()
    {
        if (instance == null)
        {
            throw new Error("请初始化YunJiModel实例");
        }
        return instance;
    }
}
