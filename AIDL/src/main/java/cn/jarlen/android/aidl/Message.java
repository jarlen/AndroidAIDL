package cn.jarlen.android.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author jarlen
 * Create by 2024/9/29 16:51
 */
public class Message implements Parcelable {
    private String msg;
    private long time;

    public Message(String msg, long time) {
        this.msg = msg;
        this.time = time;
    }

    protected Message(Parcel in) {
        msg = in.readString();
        time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
}
