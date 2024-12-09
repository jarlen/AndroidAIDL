package cn.jarlen.android.aidl.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import cn.jarlen.android.aidl.IMsgReceiver;
import cn.jarlen.android.aidl.IMsgService;
import cn.jarlen.android.aidl.MsgData;
import cn.jarlen.android.aidl.util.AIDLLog;
import cn.jarlen.android.aidl.util.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatEditText etInputMsg;
    private AppCompatTextView tvMsgContent;
    private CheckBox cbStartThread;

    private IMsgReceiver.Stub msgReceiver = new IMsgReceiver.Stub() {

        @Override
        public void onReceiver(MsgData msg) throws RemoteException {
            onReceiverMsg(msg);
        }
    };

    private IMsgService mUserService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AIDLLog.w("Client->onServiceConnected");
            mUserService = IMsgService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            AIDLLog.w("Client->onServiceDisconnected");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            /**
             * <p>当与该连接的绑定死亡时会调用此方法。</p>
             * 这意味着接口将永远不会收到另一个连接。应用程序需要取消绑定并重新绑定该连接以重新激活它。</p>
             * 例如，如果托管服务的应用程序已更新，则可能会发生这种情况。</p>
             */
            AIDLLog.w("Client->onBindingDied");
        }

        @Override
        public void onNullBinding(ComponentName name) {
            AIDLLog.w("Client->onNullBinding");
            /**
             * <p>当服务的 onBind() 方法返回 null 时会调用此方法。这表明尝试绑定的服务连接将永远不会变得可用。</p>
             * <p>应用程序必须仍然调用 Context.unbindService(ServiceConnection) 来释放与此 ServiceConnection 相关的跟踪资源;</p>
             * <p></>即使在 Context.bindService() 调用后此回调被触发。</p>
             */
            unBindMessageService();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cbStartThread = findViewById(R.id.cb_start_thread);
        findViewById(R.id.btn_send_msg).setOnClickListener(this);
        findViewById(R.id.btn_add_observer).setOnClickListener(this);
        findViewById(R.id.btn_bind).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);
        etInputMsg = findViewById(R.id.et_input_msg);
        tvMsgContent = findViewById(R.id.tv_msg_content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bind:
                bindMessageService();
                break;
            case R.id.btn_unbind:
                unBindMessageService();
                break;
            case R.id.btn_add_observer:
                addMsgObserver();
                break;
            case R.id.btn_send_msg:
                String msgContent = etInputMsg.getText().toString();
                if (TextUtils.isEmpty(msgContent)) {
                    Toast.makeText(this, "消息内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessage(msgContent, !cbStartThread.isChecked());
                break;
            default:
                break;
        }
    }

    private void addMsgObserver() {
        AIDLLog.w("Client->addMsgObserver");
        if (mUserService == null) {
            return;
        }
        try {
            mUserService.registerClient(msgReceiver);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String msgContent, boolean isMainThread) {
        MsgData msg = new MsgData(msgContent);
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

    private void sendMessage(MsgData msg) {
        AIDLLog.w("Client->发消息:" + msg.getMsg());
        if (mUserService == null) {
            bindMessageService();
            return;
        }

        try {
            AIDLLog.w("Client->发消息Before");
            mUserService.sendMsg(msg);
            AIDLLog.w("Client->发消息After");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindMessageService() {
        AIDLLog.w("Client->bindService");
        Intent bindIntent = new Intent();
        bindIntent.setClassName("cn.jarlen.android.aidl.server", "cn.jarlen.android.aidl.server.MessageService");
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    private void unBindMessageService() {
        AIDLLog.w("Client->unbindService");
        unbindService(connection);
    }

    @Override
    protected void onDestroy() {
        unBindMessageService();
        super.onDestroy();
    }

    private void onReceiverMsg(MsgData msg) {
        String msgContent = msg.getMsg();
        AIDLLog.w("Client->来消息啦:" + msgContent);
        updateMsgContent(msgContent);
    }

    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull android.os.Message handleMsg) {
            updateMsgContent(String.valueOf(handleMsg.obj));
        }
    };

    private void updateMsgContent(String msgContent) {
        if (Utils.isMainThread()) {
            tvMsgContent.setText(msgContent);
            return;
        }
        uiHandler.obtainMessage(0, msgContent).sendToTarget();
    }
}