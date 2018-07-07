package com.yunji.handler.socket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.container.grizzly.client.GrizzlyClientContainer;

import edu.wpi.rail.jrosbridge.Ros;

public class YunJiRos extends Ros
{

    public YunJiRos(String hostName, int port)
    {
        super(hostName, port);
    }

    public boolean connect()
    {
        try
        {
            // create a WebSocket connection here
            URI uri = new URI(this.getURL());
            //ClientManager clientManager = ClientManager.createClient();
            ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
            return true;
        } catch (DeploymentException | URISyntaxException | IOException e)
        {
            // failed connection, return false
            System.err.println("[ERROR]: Could not create WebSocket: " + e.getMessage());
            return false;
        }
    }
}
