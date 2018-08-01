package Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 2017 on 06/02/2017.
 */
public class MessageOverNetwork
{
    @SerializedName("fromPhoneNumber")
    private String fromPhoneNumber;
    @SerializedName("toPhoneNumber")
    private String toPhoneNumber;
    @SerializedName("time")
    private String time ;
    @SerializedName("message")
    private String message;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("status")
    private String status;

    public MessageOverNetwork()
    {
    }

    public MessageOverNetwork(String fromPhoneNumber, String toPhoneNumber, String time, String message, String uuid,String status) {
        this.fromPhoneNumber = fromPhoneNumber;
        this.toPhoneNumber = toPhoneNumber;
        this.time = time;
        this.message = message;
        this.uuid = uuid;
        this.status = status;
    }

    public String getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    public void setFromPhoneNumber(String fromPhoneNumber)
    {
        this.fromPhoneNumber = fromPhoneNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToPhoneNumber()
    {
        return toPhoneNumber;
    }

    public void setToPhoneNumber(String toPhoneNumber)
    {
        this.toPhoneNumber = toPhoneNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
