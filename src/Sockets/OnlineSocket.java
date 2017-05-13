package Sockets;

import Model.Data;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 2017 on 13/05/2017.
 */

@ServerEndpoint("/echo")
public class OnlineSocket
{
    private ConcurrentHashMap<String,String> onlineUsers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Set<String>> updateUsers = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session)
    {

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException
    {
        onlineUsers.put("059-965-1075","13-מאי-2017 11:27 PM");
        String key = message.substring(0,message.indexOf(':')+1);
        if(key.equals("Connect:"))
        {
            onlineUsers.put(message.replace("Connect:",""),"online");
        }
        else if(key.equals("IsConnected:"))
        {
            String myNumber = message.substring(message.indexOf('/')+1,message.length());
            String hisNumber =  message.substring(message.indexOf(':')+1,message.indexOf(','));
          String status = onlineUsers.get(hisNumber);
          if(updateUsers.containsKey(myNumber))
          {
              Set<String> relatedUsers = updateUsers.get(myNumber);
              relatedUsers.add(hisNumber);
              updateUsers.put(myNumber,relatedUsers);
          }
          else
          {
              Set<String> relatedUsers = Collections.synchronizedSet(new HashSet<>());
              relatedUsers.add(hisNumber);
              updateUsers.put(myNumber,relatedUsers);
          }
          session.getBasicRemote().sendText("IsConnected:"+status);
          System.out.println("Message retrive " + session.getId() + ": " + status);
        }
        else if(key.equals("DisConnect:"))
        {
            String number = message.replace("DisConnect:","");
            String status1 = onlineUsers.get(number);
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
            Date date = new Date();
            onlineUsers.put(number,dateformat.format(date));
            Set<String> relatedUsers1=    updateUsers.get(number);
            for(String k : relatedUsers1)
            {
                session.getBasicRemote().sendText("DisConnect:"+status1);
            }
            System.out.println("Message retrive " + session.getId() + ": " + dateformat.format(date));
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
