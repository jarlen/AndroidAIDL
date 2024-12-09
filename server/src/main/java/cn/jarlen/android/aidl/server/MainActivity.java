package cn.jarlen.android.aidl.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import cn.jarlen.android.aidl.MsgData;
import cn.jarlen.android.aidl.util.AIDLLog;
import cn.jarlen.android.aidl.util.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MsgManager.MsgObserver {

    private AppCompatTextView tvMsg;
    private CheckBox cbThread;
    private int msgContentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMsg = findViewById(R.id.tv_msg_content);
        cbThread = findViewById(R.id.cb_thread_type);
        findViewById(R.id.btn_send_msg).setOnClickListener(this);

        MsgManager.registerObserver(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_msg:
                sendMessage(!cbThread.isChecked());
                break;
            default:
                break;
        }
    }

    private void sendMessage(boolean isMainThread) {
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
        AIDLLog.w("Server->sendMessage");
        MsgData msg = new MsgData("say hell" + msgContentIndex + " from server");
        MessageService.sendMsg(msg);
        msgContentIndex++;
    }

    @Override
    public void onMsgArrived(MsgData msgData) {
        onReceiverMsg(msgData);
    }

    private void onReceiverMsg(MsgData msg) {
        String msgContent = msg.getMsg();
        AIDLLog.w("Server->来消息啦:" + msgContent + ", " + Utils.getThreadPrint());
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
            tvMsg.setText(msgContent);
            return;
        }
        uiHandler.obtainMessage(0, msgContent).sendToTarget();
    }
}