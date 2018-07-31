package DB;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.MongoClientURI;

public class MongoConnection
{
    private final  static String DB_URL = "mongodb://172.17.0.2:27017";
    private final static String DB_NAME = "ChatApp";
    private static MongoClient mongoClient = new MongoClient(new MongoClientURI(DB_URL));
    private static DB database = mongoClient.getDB("ChatApp");

    public static DB getDB()
    {
        return database;
    }
}
