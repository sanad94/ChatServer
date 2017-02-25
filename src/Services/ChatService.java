package Services;

import DB.Operations;
import Model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by 2017 on 07/02/2017.
 */
@Path("ChatService")
public class ChatService
{
    private final  static String USER_DEFAULT_IMAGE = "C:\\Users\\2017\\intelijProject\\chatApp\\UsersImage\\default.jpg";
    private final  static String USER_IMAGE = "C:\\Users\\2017\\intelijProject\\chatApp\\UsersImage\\";
    private final  static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private final  static String AUTH_KEY_FCM = "AAAAv0wpI3I:APA91bGnomehpgdXdkoBVBo1BlEhF-OSKe2XDECDhj5M7pHmbGTi2i4kiV4M68v5l7adY71_An5YyGjGAs1Zqp7KeSrGS2kLOOUTBs-XrmPqnkaZvazxkqXWsmUsWPN1L21wF_ZHv7Dz";

    @GET
    public String defaults()
    {
        return "please specify which service you want" ;
    }

    @Path("/UsersTokens")
    @POST
    public void addUsersTokens(String usersTokens) throws SQLException
    {
        Gson gson = new Gson();
        UsersTokens userToken = gson.fromJson(usersTokens, UsersTokens.class);
        Operations.insertNewUser(userToken);
    }

    @Path("/DeleteUser/{phoneNumber}")
    @GET
    public void deleteUser(@PathParam("phoneNumber") String phoneNumber) throws SQLException
    {
        Operations.deleteUser(phoneNumber);
    }

    @Path("/UpdateUsersTokens")
    @POST
    public void UpdateUsersTokens(String usersTokens) throws SQLException
    {
        Gson gson = new Gson();
        UsersTokens userToken = gson.fromJson(usersTokens, UsersTokens.class);
        Operations.updateUserToken(userToken);
    }

    @Path("/PushMessage")
    @POST
    public void pushMessage(String messageOverNetwork)
    {
        try
        {
            Gson gson = new Gson();
            MessageOverNetwork message = gson.fromJson(messageOverNetwork, MessageOverNetwork.class);
            String userToken = Operations.getUserToken(message.getToPhoneNumber());
            Data data = new Data(message.getFromPhoneNumber(),message.getTime(),message.getMessage());
            Notify notify = new Notify(userToken,data);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Path("/SendContactList")
    @POST
    public String SendContactList(String contactList) throws SQLException
    {
        System.out.print(contactList);
        Gson gson = new Gson();
        ArrayList<MyContacts> contactsLinkedList = gson.fromJson(contactList, new TypeToken<ArrayList<MyContacts>>() {}.getType());
        ArrayList<MyContacts> validateContactList = Operations.validateContactList(contactsLinkedList);
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

    @POST
    @Path("/settImage/{phoneNumber}")
    public void settImage( @PathParam("phoneNumber") String phoneNumber , String imageByte ) throws IOException
    {


        Gson gson = new Gson();
        ImageByte image = gson.fromJson(imageByte, ImageByte.class);
        OutputStream imageFile = null;
        imageFile = new FileOutputStream(USER_IMAGE + phoneNumber + ".jpg");
        imageFile.write(image.getImage());
        imageFile.flush();
        imageFile.close();



    }
}
