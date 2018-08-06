package VirtualAgent;

import DB.DataFetcher;
import Model.ImageMessageOverNetwork;
import Model.MessageOverNetwork;
import Model.Notify;
import Model.SocketsModel;
import Services.ChatService;
import Sockets.OnlineSocket;
import Sockets.WebsocketClientEndpoint;
import SocketsOperations.UsersOnline;
import com.google.gson.Gson;

import javax.websocket.Session;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class Tasks
{
    private ArrayList<String [] > tasks ;
    private int looper ;
    private String fromPhoneNumber;
    private String toPhoneNumber ;
    private WebsocketClientEndpoint typingSession;
    private WebsocketClientEndpoint onliSession;
    private Gson gson ;
    private MessageOverNetwork message ;
/*
    //local
    private static final String IP_ADDRESS = "10.0.0.14";
    public static final String BASE_URL_Online_SOCKET = "ws://"+IP_ADDRESS+":9090/OnlineSocket";
    public static final String BASE_URL_TYPING_SOCKET = "ws://"+IP_ADDRESS+":9090/TypingSocket";
*/
    private static final String IP_ADDRESS = "10.142.0.2";
    public static final String BASE_URL_Online_SOCKET = "ws://"+IP_ADDRESS+":8080/ChatServer-1.0-SNAPSHOT/OnlineSocket";
    public static final String BASE_URL_TYPING_SOCKET = "ws://"+IP_ADDRESS+":8080/ChatServer-1.0-SNAPSHOT/TypingSocket";
    private final  static String ROOM_IMAGE = "/Projects/JavaProjects/RoomImage/";
    private final  static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private final  static String AUTH_KEY_FCM = "AAAAv0wpI3I:APA91bGnomehpgdXdkoBVBo1BlEhF-OSKe2XDECDhj5M7pHmbGTi2i4kiV4M68v5l7adY71_An5YyGjGAs1Zqp7KeSrGS2kLOOUTBs-XrmPqnkaZvazxkqXWsmUsWPN1L21wF_ZHv7Dz";
    public Tasks(String fromPhoneNumber , String toPhoneNumber)
    {
        this.fromPhoneNumber = fromPhoneNumber ;
        this.toPhoneNumber = toPhoneNumber;
        tasks = new ArrayList<>();
        looper = 0 ;
    }

    private void checkLooper()
    {
        if(tasks.size()-1 == looper)
        {
           TasksScahdule.removeAgent(toPhoneNumber);
        }
    }

    private void socketMessage(String str,WebsocketClientEndpoint clientEndPoint)
    {
        try {


            // send message to websocket
            clientEndPoint.sendMessage(str);

            // wait 5 seconds for messages from websocket
            Thread.sleep(0);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
    }
    }

    private void connectToSocket() throws URISyntaxException {
        typingSession = new WebsocketClientEndpoint(new URI(BASE_URL_TYPING_SOCKET));
        onliSession =  new WebsocketClientEndpoint(new URI(BASE_URL_Online_SOCKET));
        typingSession.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
                System.out.println(message);
            }
        });
        onliSession.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
                System.out.println(message);
            }
        });
        SocketsModel socketsModel = new SocketsModel();
        socketsModel.setService("Connect");
        socketsModel.setFromPhoneNumber(fromPhoneNumber);
        String on = gson.toJson(socketsModel);
       try {
           socketMessage(on,onliSession);
           socketMessage(on,typingSession);
       }
       catch (Exception ex)
       {
           ex.printStackTrace();

       }
    }

    public void init()
    {
        gson = new Gson();
        try {
            connectToSocket();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String [] task1 = new String[] {"Hey,\n" +
                "I am your Virtual Agent" +
                "\n I am gonna help you " +
                "\nto tour the app !! " +
                "\nHow's that going ?" +
                "\nyou need to reply to my messages " +
                "\nIt doesn't matter what you write." +
                "\nlets take a example:" +
                "\nMe : Welcome On ..." +
                "\nYou: funy !" +
                "\nMe : Iam Typing check that at the top ot the App" +
                "\nAre You ready ? Okay here we go :) "};
        String[] task2 = new String[]{"Its time for image message, check the next message","ImageMessage:"};
        String[] task3 = new String[]{"Typing:true","Iam Typing Check that"};
        String[] task4 = new String[] {"Typing:false","Online:false","Ops I am a little bit busy , We will chitchat again ","Check last Seen at the top of the app"};
        String[] task5 = new String[] {"Online:true","Nice .. send me a message to show you that message delivered"};
        String[] task6 = new String[] {"Status:3","You saw that ? "};
        String[] task7 = new String[] {"Good .. send me a message to show you that message seen"};
        String[] task8 = new String[] {"Status:4","You saw tahat"};
        String[] task9 = new String[] {"You can see my photo .\n go back and click on it will"};
        String[] task10 = new String[] {"You can search !!,\n go back and click on search bar you can search by name , even by phone number"};
        String[] task11 = new String[] {"You can do the same thing for contacts, searching and showing  pics "};
        String[] task12 = new String[] {"You can send image message by clicking image button , wait you need to give us a permission go " +
                                        "to the settings it will ask you for that but if you denied you need to do it manually from phone settings,"
                                        ,"ops I forgot to say you can share your photo with other friends go to settings and click on default image and choose"};
        String[] task13 = new String[] {"good , go to contacts and pull the UI down it will uplod your contact to validate them","that's it for now i hope You enjoy ... next message you send will start conversation (loop)"};
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task5);
        tasks.add(task6);
        tasks.add(task7);
        tasks.add(task8);
        tasks.add(task9);
        tasks.add(task10);
        tasks.add(task11);
        tasks.add(task12);
        tasks.add(task13);

    }

    private String[] getTask()
    {
        checkLooper();
        String[] task = tasks.get(looper);
        looper++;
        return task;
    }

    private MessageOverNetwork[] prepareTask()
    {
        String[] task  = getTask();
        MessageOverNetwork[] tasks_message = new MessageOverNetwork[task.length];
        int i = 0 ;
        for (String t: task)
        {
            if(t.contains("Typing:"))
            {
                boolean typing = Boolean.parseBoolean(t.replace("Typing:",""));
                enableOrdisableTyping(typing);
            }
            else if (t.contains("Online:"))
            {
                boolean online = Boolean.parseBoolean(t.replace("Online:",""));
                enableOrdisableObline(online);
            }
            else if(t.contains("Status:"))
            {
                this.message.setStatus(Integer.valueOf(t.replace("Status:","")));
                tasks_message[i] = this.message;
                i++;
            }
            else if(t.contains("ImageMessage:"))
            {
                tasks_message[i] = madeImage();
                i++;
            }
            else
            {
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
                Date date = new Date();
                String time = dateformat.format(date);
                MessageOverNetwork message = new MessageOverNetwork(fromPhoneNumber,toPhoneNumber,time,t,UUID.randomUUID().toString(),MessageOverNetwork.SENT);
                tasks_message[i] = message;
                i++;

            }

        }

        return tasks_message;
    }

    public void executeTask()
    {
        MessageOverNetwork[] task_message = prepareTask();
        for (MessageOverNetwork m: task_message)
        {
            System.out.println("from agent "+ gson.toJson(m));
            sendHttp(m);
        }
    }

    private void enableOrdisableTyping(boolean typing)
    {
        SocketsModel socketsModel = new SocketsModel();
        if(!typing)
        {
            socketsModel.setStatus("false");
        }
        else
        {
            socketsModel.setStatus("true");
        }
        socketsModel.setService("typing");
        socketsModel.setFromPhoneNumber(fromPhoneNumber);
        socketsModel.setToPhoneNumber(toPhoneNumber);
        this.socketMessage(gson.toJson(socketsModel),typingSession);
    }

    private void enableOrdisableObline(boolean online)
    {
        if(online)
        {
            try {
                typingSession.getUserSession().close();
            connectToSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                onliSession.getUserSession().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendHttp(MessageOverNetwork message)
    {

        try
        {
            Gson gson = new Gson();
            String userToken = DataFetcher.getUserToken(message.getToPhoneNumber());
            Notify notify = new Notify(userToken,message);
            String write = gson.toJson(notify);
            URL url = new URL(API_URL_FCM);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Authorization","key="+AUTH_KEY_FCM);
            connection.setRequestProperty("Content-Type","application/json; charset=utf-8");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(),"utf-8");
            outputStreamWriter.write(write);
            outputStreamWriter.flush();
            connection.getInputStream();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    public MessageOverNetwork getMessage() {
        return message;
    }

    public void setMessage(MessageOverNetwork message) {
        this.message = message;
    }
    private String readImageFile() throws FileNotFoundException
    {
        Scanner scanner = new Scanner( new File(ROOM_IMAGE+"Image.txt"));
        String imageMessage = "";
        while (scanner.hasNext())
        {
            imageMessage += scanner.next();
        }
        return imageMessage;
    }
    private MessageOverNetwork madeImage()
    {
        MessageOverNetwork messageOverNetwork = null;

        try {
            String imageMessage = readImageFile();
            Gson gson = new Gson();
            ImageMessageOverNetwork image = gson.fromJson(imageMessage,ImageMessageOverNetwork.class);
            image.setFromPhoneNumber(fromPhoneNumber);
            image.setToPhoneNumber(toPhoneNumber);
            String time = image.getTime();
            String fromPhoneNumber = image.getFromPhoneNumber();
            String toPhoneNumber = image.getToPhoneNumber();
            String path = ROOM_IMAGE + image.ImageHashCode();
            File dir = new File(path);
            if(!dir.exists())
            {
                dir.mkdir();
            }
            UUID uuid = UUID.randomUUID();
            OutputStream imageFile = null;
            imageFile = new FileOutputStream(path +"/"+ uuid.toString()+ ".jpg");
            imageFile.write(image.getImageByte());
            imageFile.flush();
            imageFile.close();
            String message = "ImageMessage:"+uuid.toString();
             messageOverNetwork = new MessageOverNetwork(fromPhoneNumber,toPhoneNumber,time,message,uuid.toString(),MessageOverNetwork.SENT);
            }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return messageOverNetwork;
    }
}
