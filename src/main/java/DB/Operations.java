package DB;

import DB.MyConnection;
import Model.MyContacts;
import Model.UsersTokens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by 2017 on 16/02/2017.
 */
public class Operations
{
    public static void insertNewUser(UsersTokens userToken ) throws SQLException
    {
        Connection connection = MyConnection.getConnection();
        String sql = "INSERT INTO USERS(PHONE_NUMBER,USER_TOKEN) VALUES(?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,userToken.getPhoneNumber());
        preparedStatement.setString(2,userToken.getToken());
        preparedStatement.executeUpdate();
    }

    public static void updateUserToken(UsersTokens userToken ) throws SQLException
    {
        Connection connection = MyConnection.getConnection();
        String sql = "UPDATE USERS SET USER_TOKEN = ? WHERE PHONE_NUMBER = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,userToken.getToken());
        preparedStatement.setString(2,userToken.getPhoneNumber());
        preparedStatement.executeUpdate();

    }

    public static ArrayList<MyContacts> validateContactList(ArrayList<MyContacts> contacts) throws SQLException
    {
        ArrayList<MyContacts> validateContactList = new ArrayList<>();
        Connection connection = MyConnection.getConnection();
        String sql = "SELECT * FROM USERS WHERE PHONE_NUMBER = ?";
       for (int i = 0 ; i < contacts.size() ; i++ )
       {
            MyContacts myContacts=contacts.get(i);
            String number = myContacts.getPhoneNumber();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,number);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                myContacts.setPhoneNumber(number);
                validateContactList.add(myContacts);
            }
        }
        return validateContactList;
    }

    public static String getUserToken(String phoneNumber) throws SQLException
    {
        String token = "";
        Connection connection = MyConnection.getConnection();
        String sql = "SELECT USER_TOKEN FROM USERS WHERE PHONE_NUMBER = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,phoneNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {
            token = resultSet.getString("USER_TOKEN");
        }
        return token;
    }

    public static void  deleteUser(String phoneNumber) throws SQLException
    {
        String sql = "DELETE FROM USERS WHERE PHONE_NUMBER = ?";
        Connection connection = MyConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,phoneNumber);
        preparedStatement.executeUpdate();
    }
}
