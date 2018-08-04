package DB;

import Model.MyContacts;
import Model.UsersTokens;
import MongoCollections.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.*;
import com.mongodb.util.JSON;

import java.util.ArrayList;

public class DataFetcher
{

    public static void insertNewUser(UsersTokens userToken ) throws Exception
    {
        DB database = MongoConnection.getDB();
        DBCollection collection = database.getCollection(Users.Collection.toString());
        BasicDBObject fields = new BasicDBObject();
        fields.put(Users.PhoneNumber.toString(), userToken.getPhoneNumber());
        DBObject doc = collection.findOne(fields);

        if(doc==null)
        {
            Gson gson = new Gson();
            String json = gson.toJson(userToken);
            DBObject dbObject = (DBObject)JSON.parse(json);
            collection.insert(dbObject);
        }
        else
        {
            throw new Exception("user exist");
        }
    }

    public static void updateUserToken(UsersTokens userToken )
    {
        DB database = MongoConnection.getDB();
        DBCollection collection = database.getCollection(Users.Collection.toString());
        BasicDBObject update = new BasicDBObject();
        update.append("$set", new BasicDBObject().append(Users.Token.toString(), userToken.getToken()));
        BasicDBObject searchQuery = new BasicDBObject().append(Users.Token.toString(), userToken.getPhoneNumber());
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


    public static ArrayList<MyContacts> validateContactList(String userphoneNumber,ArrayList<MyContacts> contacts)
    {
        ArrayList<MyContacts> validateContactList = new ArrayList<>();
        DB database = MongoConnection.getDB();
        DBCollection collection = database.getCollection(Users.Collection.toString());
        ArrayList<DBObject> listTosave = new ArrayList<>();
        for (int i = 0 ; i < contacts.size() ; i++ )
        {
            MyContacts myContacts=contacts.get(i);
            String number = myContacts.getPhoneNumber();
            BasicDBObject fields = new BasicDBObject();
            fields.put(Users.PhoneNumber.toString(),number );
            DBObject doc = collection.findOne(fields);
            if(doc!=null)
            {
                listTosave.add(new BasicDBObject().append(Users.PhoneNumber.toString(),number));
                myContacts.setPhoneNumber(number);
                validateContactList.add(myContacts);
            }
        }
        BasicDBObject update = new BasicDBObject();
        update.append("$set", new BasicDBObject().append(Users.ContactList.toString(),listTosave));
        BasicDBObject searchQuery = new BasicDBObject().append(Users.PhoneNumber.toString(),userphoneNumber);
        collection.update(searchQuery, update);
        return validateContactList;
    }

    public static ArrayList<MyContacts> getContactList(String phoneNumber)
    {
        DB database = MongoConnection.getDB();
        DBCollection collection = database.getCollection(Users.Collection.toString());
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(Users.PhoneNumber.toString(), phoneNumber);
        DBObject doc = collection.findOne(searchQuery);
        Gson gson = new Gson();
        if(!doc.containsField(Users.ContactList.toString()))
        {
            return null;
        }
        ArrayList<MyContacts> contacts= gson.fromJson(doc.get(Users.ContactList.toString()).toString(), new TypeToken<ArrayList<MyContacts>>() {}.getType());

        return contacts;
    }
}
