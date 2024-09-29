package cn.jarlen.android.aidl.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import cn.jarlen.android.aidl.IMsgReceiver;
import cn.jarlen.android.aidl.IMsgService;
import cn.jarlen.android.aidl.Message;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatEditText etClient;


    private IMsgReceiver.Stub msgReceiver = new IMsgReceiver.Stub() {

        @Override
        public void onReceiver(Message msg) throws RemoteException {

        }
    };


    private IMsgService mUserService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("jarlen24", "ServiceConnection--->onServiceConnected:" + ThreadUtils.getThreadPrint());
            mUserService = IMsgService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_send_in_main).setOnClickListener(this);
        findViewById(R.id.btn_send_in_child).setOnClickListener(this);
        findViewById(R.id.btn_bind).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);
        etClient = findViewById(R.id.et_client_set);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_in_main:
                sendMessage(true);
                break;
            case R.id.btn_send_in_child:
                sendMessage(false);
                break;
            case R.id.btn_bind:
                bindMessageService();
                break;
            case R.id.btn_register:
                registerReceiver();
                break;
            case R.id.btn_unbind:
                unBindMessageService();
                break;
            default:
                break;
        }
    }

    private void sendMessage(boolean isMainThread) {

        String client = etClient.getText().toString();
        if (TextUtils.isEmpty(client)) {
            client = "client2";
        }

        Message msg = new Message("hello from client1", System.currentTimeMillis());
        if (isMainThread) {
            sendMessage(client, msg);
            return;
        }

        String finalClient = client;
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage(finalClient, msg);
            }
        }).start();

    }

    private void sendMessage(String client, Message msg) {

        Log.e("jarlen24", "sendMessage:" + ThreadUtils.getThreadPrint());

        if (mUserService == null) {
            bindMessageService();
            return;
        }

        try {
            mUserService.sendMsg(client, msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void registerReceiver() {
        if (mUserService == null) {
            bindMessageService();
            return;
        }
        try {
            mUserService.registerClient("client", msgReceiver);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindMessageService() {
        Intent bindIntent = new Intent();
        bindIntent.setClassName("cn.jarlen.android.aidl.server", "cn.jarlen.android.aidl.server.MessageService");
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    private void unBindMessageService() {
        unbindService(connection);
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }


}