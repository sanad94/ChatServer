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
    private Data data ;

    public Notify(String to, Data data) {
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
