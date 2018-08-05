package Services;

import DB.DataFetcher;
import DB.Operations;
import Model.*;
import VirtualAgent.Agent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by 2017 on 07/02/2017.
 */
@Path("ChatService")
public class ChatService
{
    private final  static String USER_DEFAULT_IMAGE = "/Projects/JavaProjects/ChatServer/UsersImage/default.jpg";
    private final  static String USER_IMAGE = "/Projects/JavaProjects/ChatServer/UsersImage/";
    private final  static String ROOM_IMAGE = "/Projects/JavaProjects/RoomImage/";
    private final  static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private final  static String AUTH_KEY_FCM = "AAAAv0wpI3I:APA91bGnomehpgdXdkoBVBo1BlEhF-OSKe2XDECDhj5M7pHmbGTi2i4kiV4M68v5l7adY71_An5YyGjGAs1Zqp7KeSrGS2kLOOUTBs-XrmPqnkaZvazxkqXWsmUsWPN1L21wF_ZHv7Dz";

    @GET
    public String defaults()
    {System.out.print("so");
        return "please specify which service you want" ;

    }

    @Path("/UsersTokens")
    @POST
    public void addUsersTokens(String usersTokens) throws Exception
    {
        Gson gson = new Gson();
        UsersTokens userToken = gson.fromJson(usersTokens, UsersTokens.class);
        DataFetcher.insertNewUser(userToken);
        MessageOverNetwork message = new MessageOverNetwork();
        message.setToPhoneNumber(userToken.getPhoneNumber());
        message.setFromPhoneNumber(userToken.getPhoneNumber());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Agent.run(message,Agent.FIRST_RUN_STATE);
            }
        });
        t.start();
    }

    @Path("/DeleteUser/{phoneNumber}")
    @GET
    public void deleteUser(@PathParam("phoneNumber") String phoneNumber) throws SQLException
    {
        DataFetcher.deleteUser(phoneNumber);
    }

    @Path("/UpdateUsersTokens")
    @POST
    public void UpdateUsersTokens(String usersTokens) throws SQLException
    {
        Gson gson = new Gson();
        UsersTokens userToken = gson.fromJson(usersTokens, UsersTokens.class);
        DataFetcher.updateUserToken(userToken);
    }

    @Path("/PushMessage")
    @POST
    public void pushMessage(String messageOverNetwork)
    {

        System.out.println(messageOverNetwork);
        Gson gson = new Gson();
        MessageOverNetwork message = gson.fromJson(messageOverNetwork, MessageOverNetwork.class);
        if(Agent.isToAgent(message))
        {
            Agent.run(message,Agent.NORMAL_STATE);
        }
        else
        {
            if(message.getStatus()==MessageOverNetwork.TOSERVER)
            {
                System.out.println("status sent");
                message.setStatus(MessageOverNetwork.SENT);
                String fromNumper = message.getToPhoneNumber();
                String toNumber = message.getFromPhoneNumber();
                message.setFromPhoneNumber(fromNumper);
                message.setToPhoneNumber(toNumber);
                System.out.println(gson.toJson(message));
                sendMessage(gson.toJson(message));
            }
            sendMessage(messageOverNetwork);
        }
    }

    public void sendMessage(String messageOverNetwork)
    {
        try
        {
            Gson gson = new Gson();
            MessageOverNetwork message = gson.fromJson(messageOverNetwork, MessageOverNetwork.class);
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

    @Path("/SendContactList/{userphoneNumber}")
    @POST
    public String SendContactList(@PathParam("userphoneNumber") String userphoneNumber,String contactList) throws SQLException
    {
        System.out.print(contactList);
        Gson gson = new Gson();
        ArrayList<MyContacts> contactsLinkedList = gson.fromJson(contactList, new TypeToken<ArrayList<MyContacts>>() {}.getType());
        ArrayList<MyContacts> validateContactList = DataFetcher.validateContactList(userphoneNumber,contactsLinkedList);
        String json = gson.toJson(validateContactList);
        System.out.print(json);
        return json;
    }

    @GET
    @Path("/getImage/{phoneNumber}")
    @Produces("image/jpg")
    public File getImage( @PathParam("phoneNumber") String phoneNumber )
    {
        File image = new File(USER_IMAGE + phoneNumber + ".jpg");
        if(!image.exists())
        {
            image = new File(USER_DEFAULT_IMAGE);
        }
        return image;
    }

    @GET
    @Path("/getRoomImage/{uuid}/{fromPhoneNumber}/{toPhoneNumber}")
    @Produces("image/jpg")
    public File getRoomImage( @PathParam("uuid") String uuid ,@PathParam("fromPhoneNumber") String fromPhoneNumber ,@PathParam("toPhoneNumber") String toPhoneNumber  )
    {
        ImageMessageOverNetwork image = new ImageMessageOverNetwork();
        image.setToPhoneNumber(toPhoneNumber);
        image.setFromPhoneNumber(fromPhoneNumber);
        String path = ROOM_IMAGE + image.ImageHashCode()+"/"+uuid+".jpg";
        File imageFile = new File(path);
        if(!imageFile.exists())
        {
            imageFile = new File(USER_DEFAULT_IMAGE);
        }
        return imageFile;
    }

    @POST
    @Path("/settImage/{phoneNumber}")
    public void settImage( @PathParam("phoneNumber") String phoneNumber , String imageByte ) throws IOException
    {


        Gson gson = new Gson();
        System.out.print(imageByte);
        ImageByte image = gson.fromJson(imageByte, ImageByte.class);
        OutputStream imageFile = null;
        imageFile = new FileOutputStream(USER_IMAGE + phoneNumber + ".jpg");
        imageFile.write(image.getImage());
        imageFile.flush();
        imageFile.close();
    }

    @POST
    @Path("/sendImageMessage")
    public void sendImageMessage( String imageMessage ) throws IOException
    {

        try {
            Gson gson = new Gson();
            ImageMessageOverNetwork image = gson.fromJson(imageMessage,ImageMessageOverNetwork.class);
            String time = image.getTime();
            String fromPhoneNumber = image.getFromPhoneNumber();
            String toPhoneNumber = image.getToPhoneNumber();
            String muuid = image.getUuid();
            String path = ROOM_IMAGE + image.hashCode();
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
            MessageOverNetwork messageOverNetwork = new MessageOverNetwork(fromPhoneNumber,toPhoneNumber,time,message,muuid,MessageOverNetwork.TOSERVER);
            pushMessage(gson.toJson(messageOverNetwork));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
