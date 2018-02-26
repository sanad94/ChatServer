package Model;

/**
 * Created by 2017 on 07/02/2017.
 */
public class UsersTokens
{
    private String phoneNumber;
    private String token;

    public UsersTokens()
    {
    }

    public UsersTokens(String phoneNumber, String token)
    {
        this.phoneNumber = phoneNumber;
        this.token = token;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
