package Model;

import com.google.gson.annotations.SerializedName;
/**
 * Created by 2017 on 04/02/2017.
 */
public class Notify
{
    @SerializedName("to")
    private String to ;
    @SerializedName("data")
    private MessageOverNetwork data ;

    public Notify(String to, MessageOverNetwork data) {
        this.to = to;
        this.data = data;
    }

    public Notify()
    {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MessageOverNetwork getData() {
        return data;
    }

    public void setData(MessageOverNetwork data) {
        this.data = data;
    }
}
