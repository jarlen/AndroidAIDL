package cn.jarlen.android.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author jarlen
 * Create by 2024/9/29 16:51
 */
public class Message implements Parcelable {
    private String client;
    private String to;
    private String msg;
    private long time;

    public Message(String client, String msg, long time) {
        this.client = client;
        this.msg = msg;
        this.time = time;
    }

    protected Message(Parcel in) {
        client = in.readString();
        to = in.readString();
        msg = in.readString();
        time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(client);
        dest.writeString(to);
        dest.writeString(msg);
        dest.writeLong(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "client='" + client + '\'' +
                ", to='" + to + '\'' +
                ", msg='" + msg + '\'' +
                ", time=" + time +
                '}';
    }
}
