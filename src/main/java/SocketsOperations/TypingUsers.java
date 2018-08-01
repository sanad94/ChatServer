package SocketsOperations;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TypingUsers
{
    private static ConcurrentHashMap<String,Session> usersSession = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,String> sessionKey = new ConcurrentHashMap<>();

    public void connectUser(String phoneNumber , Session session)
    {
       // onlineUsers.put(phoneNumber,"typing");
        usersSession.put(phoneNumber,session);
        sessionKey.put(session.getId(),phoneNumber);
    }

    public Session getSession(String phoneNumber)
    {
         return usersSession.get(phoneNumber);
    }

    public void disConnectUser(Session session) throws IOException
    {
        String phoneNumber = sessionKey.get(session.getId());
        usersSession.remove(phoneNumber);
        sessionKey.remove(session.getId());
    }





}
