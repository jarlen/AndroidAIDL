package cn.jarlen.android.aidl.server;

import cn.jarlen.android.aidl.Message;

public interface IClientMsgObserver {
    void onMsgArrived(Message message);
}
