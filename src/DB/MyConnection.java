package DB;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 2017 on 16/02/2017.
 */
public class MyConnection
{
    private static Connection connection;
    private final  static String DB_URL = "jdbc:mysql://localhost:3306/CHATAPP";
    private final  static String USER = "root";
    private final  static String PASS = "1234";

    public static Connection getConnection() throws SQLException
    {
        if(connection == null )
        {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            return connection;
        }
        else
        {
            return connection;
        }
    }
}
