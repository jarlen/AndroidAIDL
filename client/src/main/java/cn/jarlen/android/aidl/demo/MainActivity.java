package cn.jarlen.android.aidl.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.jarlen.android.aidl.IMsgReceiver;
import cn.jarlen.android.aidl.IMsgService;
import cn.jarlen.android.aidl.Message;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatEditText etClient;

    private MessageAdapter msgAdapter;

    private IMsgReceiver.Stub msgReceiver = new IMsgReceiver.Stub() {

        @Override
        public void onReceiver(Message msg) throws RemoteException {
            onReceiverMsg(msg);
        }
    };

    private IMsgService mUserService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("jarlen24", "ServiceConnection--->onServiceConnected:" + Utils.getThreadPrint());
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

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MessageAdapter(this);
        recyclerView.setAdapter(msgAdapter);
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

        Message msg = new Message(client, "say hello", System.currentTimeMillis());
        if (isMainThread) {
            sendMessage(msg);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage(msg);
            }
        }).start();

    }

    private void sendMessage(Message msg) {

        Log.e("jarlen24", "sendMessage:" + Utils.getThreadPrint());

        if (mUserService == null) {
            bindMessageService();
            return;
        }

        try {
            mUserService.sendMsg(msg);
            msgAdapter.addData(0, msg);
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

    private void onReceiverMsg(Message msg) {
        Log.e("jarlen24", "onReceiverMsg:" + Utils.getThreadPrint());
        if (Utils.isMainThread()) {
            msgAdapter.addData(msg);
            return;
        }
        uiHandler.obtainMessage(0, msg).sendToTarget();
    }

    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull android.os.Message handleMsg) {
            msgAdapter.addData((Message) handleMsg.obj);
        }
    };


}