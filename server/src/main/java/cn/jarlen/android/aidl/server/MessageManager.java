package cn.jarlen.android.aidl.server;

import android.os.RemoteException;
import android.text.TextUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.jarlen.android.aidl.IMsgReceiver;
import cn.jarlen.android.aidl.Message;

/**
 * @author jarlen
 * Create by 2024/9/29 17:01
 */
public class MessageManager {

    private static Map<String, IMsgReceiver> receiverMap = new HashMap<>();

    public static void registerClient(String client, IMsgReceiver receiver) {
        receiverMap.put(client, receiver);
    }

    public static void unRegisterClient(String client) {
        receiverMap.remove(client);
    }

    public static void sendMsg(String client, Message msg) {
        if (TextUtils.isEmpty(client)) {
            sendMsg(msg);
            return;
        }

        if (receiverMap.containsKey(client)) {
            return;
        }
        IMsgReceiver receiver = receiverMap.get(client);
        try {
            receiver.onReceiver(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void sendMsg(Message msg) {
        Collection<IMsgReceiver> msgReceiverList = receiverMap.values();
        if (msgReceiverList == null) {
            return;
        }
        for (IMsgReceiver receiver : msgReceiverList) {
            try {
                receiver.onReceiver(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


}
