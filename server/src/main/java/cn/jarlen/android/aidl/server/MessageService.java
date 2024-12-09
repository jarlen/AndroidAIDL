package cn.jarlen.android.aidl.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import cn.jarlen.android.aidl.IMsgReceiver;
import cn.jarlen.android.aidl.IMsgService;
import cn.jarlen.android.aidl.MsgData;
import cn.jarlen.android.aidl.util.AIDLLog;

public class MessageService extends Service {

    private static IMsgReceiver client = null;

    @Override
    public void onCreate() {
        super.onCreate();
        AIDLLog.w("Server->onCreate");
    }

    @Override
    public void onRebind(Intent intent) {
        AIDLLog.w("Server->onRebind");
        super.onRebind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        AIDLLog.w("Server->onBind");
        return iMsgService;
    }

    @Override
    public void onDestroy() {
        AIDLLog.w("Server->onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        AIDLLog.w("Server->onUnbind");
        return super.onUnbind(intent);
    }

    public static void sendMsg(MsgData msg) {
        if (client == null) {
            AIDLLog.w("Server->sendMsg,client not found");
            return;
        }
        try {
            Thread.sleep(1000);
            client.onReceiver(msg);
        } catch (RemoteException e) {
            AIDLLog.e("Server->sendMsg,fail:" + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void processClientMsg(MsgData msg) {
        AIDLLog.w("Server->processClientMsg");
        sendMsg(msg);
    }


    private IMsgService.Stub iMsgService = new IMsgService.Stub() {
        @Override
        public void registerClient(IMsgReceiver receiver) throws RemoteException {
            client = receiver;
            AIDLLog.w("Server->registerClient");
        }

        @Override
        public void sendMsg(MsgData msg) throws RemoteException {
            AIDLLog.w("Server->sendMsg");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MsgManager.sendMsg(msg);
            MessageService.processClientMsg(new MsgData("I have received your message:" + msg.getMsg() + "。from Auto Reply by Server"));
        }

        @Override
        public void unRegisterClient(IMsgReceiver receiver) throws RemoteException {
            client = null;
        }

        @Override
        public void linkToDeath(@NonNull IBinder.DeathRecipient recipient, int flags) {
            super.linkToDeath(recipient, flags);
            AIDLLog.w("Server->linkToDeath");
        }

        @Override
        public boolean unlinkToDeath(@NonNull IBinder.DeathRecipient recipient, int flags) {
            AIDLLog.w("Server->unlinkToDeath");
            return super.unlinkToDeath(recipient, flags);
        }
    };

}