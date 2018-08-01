package Model;

import com.google.gson.annotations.SerializedName;

public class SocketsModel
{
    @SerializedName("service")
    private String service ;
    @SerializedName("toPhoneNumber")
    private String toPhoneNumber ;
    @SerializedName("fromPhoneNumber")
    private String fromPhoneNumber ;
    @SerializedName("status")
    private String status ;

    // for connect service
    // for request disconnect
    public SocketsModel(String service, String fromPhoneNumber)
    {
        this.service = service;
        this.fromPhoneNumber = fromPhoneNumber;
    }

    // for request isconnect service
    public SocketsModel(String service, String toPhoneNumber, String fromPhoneNumber, String status)
    {
        this.service = service;
        this.toPhoneNumber = toPhoneNumber;
        this.fromPhoneNumber = fromPhoneNumber;
        this.status = status;
    }

    public SocketsModel()
    {
    }

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getToPhoneNumber()
    {
        return toPhoneNumber;
    }

    public void setToPhoneNumber(String toPhoneNumber)
    {
        this.toPhoneNumber = toPhoneNumber;
    }

    public String getFromPhoneNumber()
    {
        return fromPhoneNumber;
    }

    public void setFromPhoneNumber(String fromPhoneNumber)
    {
        this.fromPhoneNumber = fromPhoneNumber;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
