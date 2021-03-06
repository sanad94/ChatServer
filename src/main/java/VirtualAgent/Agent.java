package VirtualAgent;

import Model.MessageOverNetwork;

import java.util.Random;

public class Agent
{
    private String agentPhonrNumber ;
    private String toPhoneNumber ;
    private Tasks tasks;
    public static int FIRST_RUN_STATE = 1 ;
    public static int NORMAL_STATE = 2 ;
    public static int LOAD_FROM_DB = 1;
    public Agent(String toPhoneNumber , int load , String agentPhonrNumber)
    {
        if(load==LOAD_FROM_DB)
        {
            this.agentPhonrNumber = agentPhonrNumber;
        }
        else
        {
            this.agentPhonrNumber = this.generateAgentNumber();
        }
        this.toPhoneNumber = toPhoneNumber;
        tasks = new Tasks(this.agentPhonrNumber,toPhoneNumber);
        tasks.init();
    }

    public static boolean isToAgent(MessageOverNetwork message)
    {
//        Agent check = TasksScahdule.getAgent(message.getFromPhoneNumber());
//
//        if(check!=null && check.getAgentPhonrNumber().equals(message.getToPhoneNumber()))
//        {
//            return true;
//        }
//        else if(check!=null && check.getAgentPhonrNumber().equals(message.getFromPhoneNumber()))
//        {
//            return true;
//        }
//        return false;
        return TasksScahdule.isToAgent(message.getFromPhoneNumber(),message.getToPhoneNumber());
    }

    public static void run(MessageOverNetwork message,int flag)
    {
        Agent agent ;

        agent = TasksScahdule.getAgent(message.getFromPhoneNumber(),flag);

        if (agent == null)
            return;

        agent.start(message , flag);
    }

    private String generateAgentNumber()
    {
        String number = "";
        Random r = new Random();
        for (int i = 0; i <10 ; i++)
        {
                int temp = r.nextInt(9) ;
                number += String.valueOf(temp);
        }
        number = number.substring(0, 3) + "-" + number.substring(3, 6) + "-" + number.substring(6, number.length());
        return number.toString();
    }

    public void start(MessageOverNetwork message,int flag)
    {
        if(message.getToPhoneNumber().equals(agentPhonrNumber) && message.getStatus() == MessageOverNetwork.TOSERVER|| flag == FIRST_RUN_STATE )
        {
            //send that masge have been sent

            //just swap the agent take message ready to sent
            message.setStatus(MessageOverNetwork.TOSERVER);
            String to = message.getFromPhoneNumber();
            String from = agentPhonrNumber;
            message.setToPhoneNumber(to);
            message.setFromPhoneNumber(from);
            if(NORMAL_STATE==flag)
            {
                message.setStatus(MessageOverNetwork.SENT);
                tasks.sendHttp(message);
            }
            tasks.setMessage(message);
            tasks.executeTask();
        }


    }

    public String getAgentPhonrNumber()
    {
        return agentPhonrNumber;
    }

    public void setAgentPhonrNumber(String agentPhonrNumber) {
        this.agentPhonrNumber = agentPhonrNumber;
    }

    public String getToPhoneNumber() {
        return toPhoneNumber;
    }

    public void setToPhoneNumber(String toPhoneNumber) {
        this.toPhoneNumber = toPhoneNumber;
    }

}
