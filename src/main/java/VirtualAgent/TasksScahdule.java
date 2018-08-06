package VirtualAgent;

import DB.DataFetcher;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

public class TasksScahdule
{
    private static ConcurrentHashMap<String,Agent> userssAgent = new ConcurrentHashMap<>();

    private static void addAgent(String userPhoneNumber)
    {
        Agent agent = new Agent(userPhoneNumber,0,"");
        DataFetcher.insertVirtualAgent(agent.getAgentPhonrNumber(),userPhoneNumber);
        userssAgent.put(userPhoneNumber,agent);
    }

    private static void loadAgentFromDb( String userPhoneNumber)
    {
        String agentphoneNumber = DataFetcher.getAgentNumber(userPhoneNumber);
        Agent agent = new Agent(userPhoneNumber,Agent.LOAD_FROM_DB,agentphoneNumber);
        userssAgent.put(userPhoneNumber,agent);
    }

    public static boolean isToAgent(String userphoneNumber , String toPhoneNumber)
    {

        if(userssAgent.containsKey(userphoneNumber))
        {
            Agent a = userssAgent.get(userphoneNumber);
            if(a.getAgentPhonrNumber().equals(toPhoneNumber))
            {
                return true;
            }

        }
         if(DataFetcher.getAgentNumber(userphoneNumber).equals(toPhoneNumber))
        {
            return true;
        }

        return false;

    }

    public static Agent getAgent(String userPhoneNumber , int state)
    {
        if(state==Agent.FIRST_RUN_STATE)
        {
            addAgent(userPhoneNumber);
        }
        else if(!userssAgent.containsKey(userPhoneNumber))
        {
            loadAgentFromDb(userPhoneNumber);
        }
        return userssAgent.get(userPhoneNumber);
    }

    public static void removeAgent(String number)
    {
        userssAgent.remove(number);
    }


}
