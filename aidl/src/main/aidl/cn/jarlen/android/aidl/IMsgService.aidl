// IMsgService.aidl
package cn.jarlen.android.aidl;

// Declare any non-default types here with import statements

import cn.jarlen.android.aidl.IMsgReceiver;
import cn.jarlen.android.aidl.Message;

interface IMsgService {
    void registerClient(String client,IMsgReceiver receiver);

    void sendMsg(in Message msg);

    void unRegisterClient(String client,IMsgReceiver receiver);
}