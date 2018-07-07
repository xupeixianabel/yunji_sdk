package com.yunji.handler.socket;

import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;

import edu.wpi.rail.jrosbridge.Ros;

/**
 * Created by vincent on 2018/3/23.
 */

public class ReConnectionThread extends  Thread
{
    private Ros ros;

    private CommonLog commonLog = LogFactory.createLog(ReConnectionThread.class);

    private  int attempts = 0;

    public ReConnectionThread(Ros ros)
    {
        this.ros = ros;

    }

    public void run()
    {
        try
        {
            while (!isInterrupted())
            {
                if(ros != null && !ros.isConnected())
                {
                    commonLog.d("reconnection socket");
                    Thread.sleep((long)timeDelay()* 1000L);
                    ros.connect();
                    attempts++ ;
                }

            }

        }catch (InterruptedException e)
        {
            commonLog.d("reconnection failed");
        }


    }


    private int timeDelay()
    {
        if (attempts > 13)
        {
            return 60 * 5;      // 5 minutes
        }
        if (attempts > 7)
        {
            return 60;          // 1 minute
        }
        return 10;              // 10 seconds
    }
}
