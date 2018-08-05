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
    private static final String IP_ADDRESS = "10.0.0.13";
    public static final String BASE_URL_Online_SOCKET = "ws://"+IP_ADDRESS+":9090/OnlineSocket";
    public static final String BASE_URL_TYPING_SOCKET = "ws://"+IP_ADDRESS+":9090/TypingSocket";
    public static final String BASE_URL_SERVER = "http://"+IP_ADDRESS+":9090/ChatService/PushMessage";
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
            looper = 0 ;
        }
    }

    private void socketMessage(String str,WebsocketClientEndpoint clientEndPoint)
    {
        try {


            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            clientEndPoint.sendMessage("{'event':'addChannel','channel':'ok_btccny_ticker'}");

            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
    }
    }

    public void init()
    {
        try {
            typingSession = new WebsocketClientEndpoint(new URI(BASE_URL_Online_SOCKET));
            onliSession =  new WebsocketClientEndpoint(new URI(BASE_URL_TYPING_SOCKET));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        gson = new Gson();
        String [] task1 = new String[] {"Welcome On Board :) I am your Virtual Agent I am gonna help you " +
                "to tour the app !! how's that going ? you need to reply to  my message " +
                "it doesn't matter what you write. lets take a example " +
                "me : Welcome On ..." +
                "You: funy !" +
                "me : now Iam Typing check status in the top of chat Room under pohone number" +
                "are You ready ? okay here we go"};
        String[] task2 = new String[]{"Its the time for image message check the next message","ImageMessage:"};
        String[] task3 = new String[]{"Typing:true","Iam Typing Check that","Check last Seen in the top of the app under phone number"};
        String[] task4 = new String[] {"Typing:false","Online:false","ops i am busy , will chitchat again ","Check last Seen in the top of the app under phone number"};
        String[] task5 = new String[] {"Online:true","nice .. send me a message to show you that message delivered"};
        String[] task6 = new String[] {"Status:3"};
        String[] task7 = new String[] {"nice .. send me a message to show you that message read by me"};
        String[] task8 = new String[] {"Status:3"};
        String[] task9 = new String[] {"go back and clik on my pic it will open it .."};
        String[] task10 = new String[] {"go back and clik on search now you can search by name , even using a phone number .."};
        String[] task11 = new String[] {"You can do the same thing in contacts search and click pics "};
        String[] task12 = new String[] {"You can send image message by clicking image button , wait you need to give us a permision go " +
                                        "to the settings its will ask you for that but if you denied you need to do it manuualy from phone settings,"
                                        ,"ops i forgot to say you can share your pic with other friends go to settings and click on defailt image and chose"};
        String[] task13 = new String[] {"good , go to contacts and pull the ui down it will uplod your contact to validate them","that's it for now i hope You enjoy ... next message you send will start conversation (loop)"};
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
            senHttp(m);
        }
    }
    private void connectToSocket()
    {
        SocketsModel socketsModel = new SocketsModel();
        socketsModel.setService("Connect");
        socketsModel.setFromPhoneNumber(fromPhoneNumber);
        this.socketMessage(gson.toJson(socketsModel),onliSession);
        this.socketMessage(gson.toJson(socketsModel),typingSession);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            connectToSocket();
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
    public void senHttp(MessageOverNetwork message)
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
            String muuid = image.getUuid();
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
             messageOverNetwork = new MessageOverNetwork(fromPhoneNumber,toPhoneNumber,time,message,muuid,MessageOverNetwork.TOSERVER);
            }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return messageOverNetwork;
    }
}
