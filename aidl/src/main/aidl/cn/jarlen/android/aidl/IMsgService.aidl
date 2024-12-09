// IMsgService.aidl
package cn.jarlen.android.aidl;

// Declare any non-default types here with import statements

import cn.jarlen.android.aidl.IMsgReceiver;
import cn.jarlen.android.aidl.MsgData;

interface IMsgService {

    void registerClient(IMsgReceiver receiver);

    void sendMsg(in MsgData msg);

    void unRegisterClient(IMsgReceiver receiver);
}