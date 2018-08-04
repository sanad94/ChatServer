package SocketsOperations;

import DB.DataFetcher;
import Model.MyContacts;
import Model.SocketsModel;
import com.google.gson.Gson;
import javax.websocket.Session;
import java.awt.image.AreaAveragingScaleFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsersOnline
{

    private static ConcurrentHashMap<String,String> onlineUsers = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,ArrayList<String>> updateUsers = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,Session> usersSession = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,String> sessionKey = new ConcurrentHashMap<>();

    public void connectUser(String phoneNumber , Session session)
    {
        onlineUsers.put(phoneNumber,"online");
        usersSession.put(phoneNumber,session);
        sessionKey.put(session.getId(),phoneNumber);
    }

    public String isUserConnected(String myNumber , String hisNumber)
    {
        String status = onlineUsers.get(hisNumber);
        if (status==null)
        {
            status = " ";
        }
        return status;
    }

    public void disConnectUser(Session session) throws IOException
    {
        String phoneNumber = sessionKey.get(session.getId());
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
        Date date = new Date();
        String status = dateformat.format(date);
        onlineUsers.put(phoneNumber,status);
        //ArrayList<String> relatedUsers = updateUsers.get(phoneNumber);
        SocketsModel responceOnline = new SocketsModel();
        responceOnline.setService("OffLine");
        responceOnline.setStatus(status);
        responceOnline.setFromPhoneNumber(phoneNumber);
        Gson gson = new Gson();
        ArrayList<MyContacts> contacts = DataFetcher.getContactList(phoneNumber);
        for(MyContacts c : contacts)
        {
           Session contactSession =  usersSession.get(c.getPhoneNumber());
           if(contactSession!=null && session.isOpen())
           {
               contactSession.getBasicRemote().sendText(gson.toJson(responceOnline));
           }
        }
        usersSession.remove(phoneNumber);
        sessionKey.remove(session.getId());
    }
}
