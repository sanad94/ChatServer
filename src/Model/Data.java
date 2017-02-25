package Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 2017 on 06/02/2017.
 */
public class Data
{
    @SerializedName("fromPhoneNumber")
    private String fromPhoneNumber;
    @SerializedName("time")
    private String time ;
    @SerializedName("message")
    private String message;

    public Data() {
    }

    public Data(String fromPhoneNumber, String time, String message) {
        this.fromPhoneNumber = fromPhoneNumber;
        this.time = time;
        this.message = message;
    }

    public String getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    public void setFromPhoneNumber(String fromPhoneNumber) {
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
}
