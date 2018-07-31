package DB;

import Model.MyContacts;
import Model.UsersTokens;
import MongoCollections.Users;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;

import java.util.ArrayList;

public class DataFetcher
{

    public static void insertNewUser(UsersTokens userToken )
    {
        DB database = MongoConnection.getDB();
        DBCollection collection = database.getCollection(Users.Collection.toString());
        Gson gson = new Gson();
        String json = gson.toJson(userToken);
        DBObject dbObject = (DBObject)JSON.parse(json);
        collection.insert(dbObject);
    }

    public static void updateUserToken(UsersTokens userToken )
    {
        DB database = MongoConnection.getDB();
        DBCollection collection = database.getCollection(Users.Collection.toString());
        BasicDBObject update = new BasicDBObject();
        update.append("$set", new BasicDBObject().append(Users.Token.toString(), userToken.getPhoneNumber()));
        BasicDBObject searchQuery = new BasicDBObject().append(Users.Token.toString(), userToken.getToken());
        collection.update(searchQuery, update);

    }

    public static String getUserToken(String phoneNumber)
    {
        DB database = MongoConnection.getDB();
        DBCollection collection = database.getCollection(Users.Collection.toString());
        BasicDBObject fields = new BasicDBObject();
        fields.put(Users.PhoneNumber.toString(), phoneNumber);
        DBObject doc = collection.findOne(fields);
        Gson gson = new Gson();
        UsersTokens usersToken = gson.fromJson(doc.toString(),UsersTokens.class);
        return usersToken.getToken();
    }

    public static void  deleteUser(String phoneNumber)
    {
        DB database = MongoConnection.getDB();
        DBCollection collection = database.getCollection(Users.Collection.toString());
        BasicDBObject fields = new BasicDBObject();
        fields.put(Users.PhoneNumber.toString(), phoneNumber);
        DBObject doc = collection.findOne(fields);
        collection.remove(doc);
    }


    public static ArrayList<MyContacts> validateContactList(ArrayList<MyContacts> contacts) 
    {
        ArrayList<MyContacts> validateContactList = new ArrayList<>();
        DB database = MongoConnection.getDB();
        DBCollection collection = database.getCollection(Users.Collection.toString());

        for (int i = 0 ; i < contacts.size() ; i++ )
        {
            MyContacts myContacts=contacts.get(i);
            String number = myContacts.getPhoneNumber();
            BasicDBObject fields = new BasicDBObject();
            fields.put(Users.PhoneNumber.toString(),number );
            DBObject doc = collection.findOne(fields);
            if(doc==null)
            {
                myContacts.setPhoneNumber(number);
                validateContactList.add(myContacts);
            }
        }
        return validateContactList;
    }


}
