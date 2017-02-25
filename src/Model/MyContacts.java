package Model;


/**
 * Created by 2017 on 28/01/2017.
 */

public class MyContacts
{

    private String phoneNumber;
    private String name;

    public MyContacts()
    {

    }

    public MyContacts(String phoneNumber, String name)
    {
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

}
