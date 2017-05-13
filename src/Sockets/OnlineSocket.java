package Sockets;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 2017 on 13/05/2017.
 */

@ServerEndpoint("/echo")
public class OnlineSocket
{
    private Set<String> onlineUsers = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session)
    {

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException
    {
        if(message.contains("Connect:"))
        {
            onlineUsers.add(message.replace("Connect:",""));
        }
        else if(message.contains("IsConnected:"))
        {
          boolean isConnected =  onlineUsers.contains(message.replace("IsConnected:",""));
          session.getBasicRemote().sendText("IsConnected:"+String.valueOf(isConnected));
          System.out.println("Message retrive " + session.getId() + ": " + String.valueOf(isConnected));
        }
        System.out.println("Message from " + session.getId() + ": " + message);
    }

    @OnClose
    public void onClose(Session session)
    {
        System.out.println("Session " +session.getId()+" has ended");
    }

    @OnError
    public void onError(Session session, Throwable thr)
    {
        System.out.println("Session " +session.getId()+" have error");
    }

}
