package Sockets;

import Model.SocketsModel;
import SocketsOperations.TypingUsers;
import com.google.gson.Gson;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/TypingSocket")
public class TypingSocket
{
    private static TypingUsers typingUsers = new TypingUsers();
    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session)
    {

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException
    {
        SocketsModel typingRequest = gson.fromJson(message, SocketsModel.class);
        SocketsModel typingResponce = new SocketsModel();
        String key = typingRequest.getService();
        boolean typing = Boolean.parseBoolean(typingRequest.getStatus());
        String myNumber = typingRequest.getFromPhoneNumber();
        String hisNumber =  typingRequest.getToPhoneNumber();

        if(key.equals("Connect"))
        {
            typingUsers.connectUser(typingRequest.getFromPhoneNumber(),session);
        }
        else if(key.equals("typing")&&typing)
        {
            Session hisSession = typingUsers.getSession(hisNumber);
            typingResponce.setService("typing");
            typingResponce.setStatus("true");
            typingResponce.setFromPhoneNumber(myNumber);
            typingResponce.setToPhoneNumber(hisNumber);
            hisSession.getBasicRemote().sendText(gson.toJson(typingResponce));
        }
       else if(key.equals("typing")&&!typing)
        {
            Session hisSession = typingUsers.getSession(hisNumber);
            typingResponce.setFromPhoneNumber(myNumber);
            typingResponce.setToPhoneNumber(hisNumber);
            typingResponce.setService("typing");
            typingResponce.setStatus("false");
            hisSession.getBasicRemote().sendText(gson.toJson(typingResponce));
        }

        System.out.println("Message from " + session.getId() + ": " + message);
    }

    @OnClose
    public void onClose(Session session) throws IOException
    {
        typingUsers.disConnectUser(session);
        System.out.println("Session " +session.getId()+" has ended");
    }

    @OnError
    public void onError(Session session, Throwable thr)
    {
        System.out.println("Session " +session.getId()+" have error");
        System.out.println(thr.getMessage());

    }

}
