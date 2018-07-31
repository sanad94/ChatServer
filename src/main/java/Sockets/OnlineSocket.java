package Sockets;

import Model.OnlineModel;
import SocketsOperations.UsersOnline;
import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/echo")
public class OnlineSocket
{
    private static UsersOnline usersOnline = new UsersOnline();
    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session)
    {

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException
    {
        OnlineModel requestOnline = gson.fromJson(message,OnlineModel.class);
        OnlineModel responceOnline = new OnlineModel();
        String key = requestOnline.getService();

        if(key.equals("Connect"))
        {
            usersOnline.connectUser(requestOnline.getFromPhoneNumber(),session);
        }
        else if(key.equals("IsConnected"))
        {
            String myNumber = requestOnline.getFromPhoneNumber();
            String hisNumber =  requestOnline.getToPhoneNumber();
            String status = usersOnline.isUserConnected(myNumber,hisNumber);
            responceOnline.setService("IsConnected");
            responceOnline.setStatus(status);
            session.getBasicRemote().sendText(gson.toJson(responceOnline));
            System.out.println("Message retrive " + session.getId() + ": " + status);
        }
/*        else if(key.equals("OffLine"))
        {
            String number = requestOnline.getFromPhoneNumber();
            usersOnline.disConnectUser(number);
        }*/

        System.out.println("Message from " + session.getId() + ": " + message);
    }

    @OnClose
    public void onClose(Session session) throws IOException
    {
        usersOnline.disConnectUser(session);
        System.out.println("Session " +session.getId()+" has ended");
    }

    @OnError
    public void onError(Session session, Throwable thr)
    {
        System.out.println("Session " +session.getId()+" have error");
    }

}