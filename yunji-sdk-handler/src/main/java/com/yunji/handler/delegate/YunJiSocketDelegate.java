package com.yunji.handler.delegate;

import android.content.Context;

import com.yunji.handler.log.CommonLog;
import com.yunji.handler.log.LogFactory;
import com.yunji.handler.model.CookieModel;
import com.yunji.handler.socket.ReConnectionThread;
import com.yunji.handler.socket.YunJiRos;
import com.yunji.handler.utils.CommonConstants;
import com.yunji.handler.utils.YunJiUtils;
import com.yunji.handler.vo.TopicSubcribeVO;

import javax.websocket.Session;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.handler.RosHandler;
/**
 * @author vincent
 *
 */
public class YunJiSocketDelegate
{

    private Ros ros;

    private Topic robotStatusTopic;

    private Topic taskStautsTopic;

    private Topic doorStatusTopic;

    private Topic powerStatusTopic;

    private Topic eStopTopic;

    private TopicSubcribeVO topicSubcribeVO;

    protected CommonLog logger = LogFactory.createLog(getClass());

    public YunJiSocketDelegate(Context context) throws  Exception
    {
        String hostName = YunJiUtils.getPropertiesValue(context, CommonConstants.Config.SOCKET_URL);
        String port = YunJiUtils.getPropertiesValue(context, CommonConstants.Config.SOCKET_PORT);
        if (YunJiUtils.isBlank(hostName) || YunJiUtils.isBlank(port))
        {
            throw new Exception("Please config hostName and port in the assets/config.properties file");
        }
        ros = new Ros(hostName, Integer.valueOf(port));
        ros.addRosHandler(rosHandler);
        logger.d("Ros connection start......");
        ros.connect();
        initTopicHandler();

    }


    RosHandler rosHandler = new RosHandler()
    {
        public void handleConnection(Session session)
        {
            logger.d(">>Ros server connected success!");
        }


        public void handleDisconnection(Session session)
        {
            new ReConnectionThread(ros).start();
            logger.d(">>Ros server disconnection");
        }


        public void handleError(Session session, Throwable t)
        {
            logger.d(">>handleError");
        }
    };

    private void initTopicHandler()
    {
        robotStatusTopic = new Topic(ros, "/peter_task_action/robot_status", "peter_msgs/RobotStatus");
        taskStautsTopic = new Topic(ros, "/peter_task_action/task_status", "peter_msgs/TaskStatus");
        doorStatusTopic = new Topic(ros, "/peter_door_ctrl/door_status", "peter_msgs/DoorStatus");
        powerStatusTopic = new Topic(ros, "/peter_power_core/power_status", "peter_msgs/peter_msgs/PowerStatus");
        eStopTopic = new Topic(ros, "/peter_motor_core/estop", "std_msgs/Int32");

    }


    public void subRobotStatusTopic(TopicCallback callback)
    {
        if(robotStatusTopic.isSubscribed())
        {
            return;
        }
        robotStatusTopic.subscribe(callback);
    }

    public void subTaskStatusTopic(TopicCallback callback)
    {

        if(taskStautsTopic.isSubscribed())
        {
            return;
        }
        taskStautsTopic.subscribe(callback);
    }

    public void subDoorStatusTopic(TopicCallback callback)
    {

        if(doorStatusTopic.isSubscribed())
        {
            return;
        }
        doorStatusTopic.subscribe(callback);
    }

    public void subPowerStatusTopic(TopicCallback callback)
    {

        if(powerStatusTopic.isSubscribed())
        {
            return;
        }
        powerStatusTopic.subscribe(callback);
    }

    public void subEStopStatusTopic(TopicCallback callback)
    {
        if(eStopTopic.isSubscribed())
        {
            return;
        }
        eStopTopic.subscribe(callback);
    }

//un Subcribe

    public void unSubRobotStatusTopic()
    {
        if (robotStatusTopic.isSubscribed())
        {
            robotStatusTopic.unsubscribe();
            return;
        }
    }

    public void unSubTaskStatusTopic()
    {
        if (taskStautsTopic.isSubscribed())
        {
            taskStautsTopic.unsubscribe();
        }
    }

    public void unSubDoorStatusTopic()
    {
        if ( doorStatusTopic.isSubscribed())
        {
            doorStatusTopic.unsubscribe();
        }

    }

    public void unSubPowerStatusTopic()
    {
        if ( powerStatusTopic.isSubscribed())
        {
            powerStatusTopic.unsubscribe();
        }
    }

    public void unSubEStopStatusTopic()
    {
        if (eStopTopic.isSubscribed())
        {
            eStopTopic.unsubscribe();
        }
    }


}