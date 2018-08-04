package Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 2017 on 14/05/2017.
 */
public class ImageMessageOverNetwork
{
    @SerializedName("fromPhoneNumber")
    private String fromPhoneNumber;
    @SerializedName("toPhoneNumber")
    private String toPhoneNumber;
    @SerializedName("time")
    private String time ;
    @SerializedName("imageByte")
    private byte [] imageByte;
    @SerializedName("uuid")
    private String uuid;

    public ImageMessageOverNetwork(String fromPhoneNumber, String toPhoneNumber, String time, byte[] imageByte, String uuid) {
        this.fromPhoneNumber = fromPhoneNumber;
        this.toPhoneNumber = toPhoneNumber;
        this.time = time;
        this.imageByte = imageByte;
        this.uuid = uuid;
    }

    public ImageMessageOverNetwork() {
    }

    public String getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    public void setFromPhoneNumber(String fromPhoneNumber) {
        this.fromPhoneNumber = fromPhoneNumber;
    }

    public String getToPhoneNumber() {
        return toPhoneNumber;
    }

    public void setToPhoneNumber(String toPhoneNumber) {
        this.toPhoneNumber = toPhoneNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        String from = fromPhoneNumber.replace("-","");
        String to = toPhoneNumber.replace("-","");
        return Integer.valueOf(from) * Integer.valueOf(to);
    }
}
