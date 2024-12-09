package cn.jarlen.android.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author jarlen
 * Create by 2024/9/29 16:51
 */
public class MsgData implements Parcelable {
    private String msg;

    public MsgData(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    protected MsgData(Parcel in) {
        msg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MsgData> CREATOR = new Creator<MsgData>() {
        @Override
        public MsgData createFromParcel(Parcel in) {
            return new MsgData(in);
        }

        @Override
        public MsgData[] newArray(int size) {
            return new MsgData[size];
        }
    };
}
