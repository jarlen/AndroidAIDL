package cn.jarlen.android.aidl.server;

import cn.jarlen.android.aidl.MsgData;

public class MsgManager {

    public static MsgObserver mObserver;

    public static void registerObserver(MsgObserver observer) {
        mObserver = observer;
    }

    public static void sendMsg(MsgData msg) {
        if (mObserver == null) {
            return;
        }
        mObserver.onMsgArrived(msg);
    }

    public interface MsgObserver {
        public void onMsgArrived(MsgData msgData);
    }
}
