package cn.jarlen.android.aidl.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import cn.jarlen.android.aidl.IMsgReceiver;
import cn.jarlen.android.aidl.IMsgService;
import cn.jarlen.android.aidl.Message;

public class MessageService extends Service {

    private IMsgService.Stub iMsgService = new IMsgService.Stub() {
        @Override
        public void registerClient(String client, IMsgReceiver receiver) throws RemoteException {
            MessageManager.registerClient(client, receiver);
        }

        @Override
        public void sendMsg(Message msg) throws RemoteException {
            MessageManager.sendMsg(msg);
        }

        @Override
        public void unRegisterClient(String client, IMsgReceiver receiver) throws RemoteException {
            MessageManager.unRegisterClient(client);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iMsgService;
    }
}