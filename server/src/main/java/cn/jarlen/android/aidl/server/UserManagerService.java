package cn.jarlen.android.aidl.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import cn.jarlen.android.aidl.ILoginCallBack;
import cn.jarlen.android.aidl.IUserService;
import cn.jarlen.android.aidl.User;

public class UserManagerService extends Service {

    private IUserService.Stub userService = new IUserService.Stub() {
        @Override
        public void onLogin(String name, String pwd, ILoginCallBack cb) throws RemoteException {
            if (cb == null) {
                return;
            }
            cb.onCallBack(0, new User(name, 10));
        }

        @Override
        public User getUser(String name) throws RemoteException {
            Log.e("jarlen24", "UserManagerService--->getUser:" + name + ", " + ThreadUtils.getThreadPrint());
            return new User(name, 12);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("jarlen24", "UserManagerService--->onCreate:" + ThreadUtils.getThreadPrint());
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("jarlen24", "UserManagerService--->onBind:" + ThreadUtils.getThreadPrint());
        return userService;
    }
}