package cn.jarlen.android.aidl.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import cn.jarlen.android.aidl.IMsgReceiver;
import cn.jarlen.android.aidl.Message;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMsg = findViewById(R.id.tv_msg_from_client);
        findViewById(R.id.btn_send_in_main).setOnClickListener(this);
        findViewById(R.id.btn_send_in_child).setOnClickListener(this);
        MessageManager.registerClient("server", new IMsgReceiver.Stub() {
            @Override
            public void onReceiver(Message msg) throws RemoteException {
                Log.e("jarlen24", "onReceiver at server,msg:" + msg.toString() + ",thread: " + Utils.getThreadPrint());
                onReceiverMsg(msg);
            }
        });
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
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        MessageManager.unRegisterClient("server");
        super.onDestroy();
    }

    private void sendMessage(boolean isMainThread) {
        Toast.makeText(this, "say hi at server to client", Toast.LENGTH_SHORT).show();
        if (isMainThread) {
            sendMessage();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage();
            }
        }).start();
    }

    private void sendMessage() {
        Log.e("jarlen24", "sendMessage at server:" + Utils.getThreadPrint());
        Message msg = new Message("", "say hi from server", System.currentTimeMillis());
        MessageManager.sendMsg(msg);
    }

    private void onReceiverMsg(Message msg) {
        if (Utils.isMainThread()) {
            tvMsg.setText(msg.getMsg());
            return;
        }
        uiHandler.obtainMessage(0, msg).sendToTarget();
    }

    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull android.os.Message handleMsg) {
            tvMsg.setText(((Message) handleMsg.obj).getMsg());
        }
    };

}