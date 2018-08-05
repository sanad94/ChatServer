package VirtualAgent;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

public class TasksScahdule
{
    private static ConcurrentHashMap<String,Agent> userssAgent = new ConcurrentHashMap<>();

    public static void addAgent(Agent agent , String userPhoneNumber)
    {
        if(userssAgent.contains(userPhoneNumber))
        {
            return;
        }
        userssAgent.put(userPhoneNumber,agent);
    }

    public static Agent getAgent(String userPhoneNumber)
    {
        return userssAgent.get(userPhoneNumber);
    }


}
