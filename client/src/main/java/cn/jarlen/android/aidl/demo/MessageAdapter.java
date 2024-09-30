package cn.jarlen.android.aidl.demo;

import android.content.Context;

import cn.jarlen.android.aidl.Message;
import cn.jarlen.android.aidl.demo.adapter.RvMultiAdapter;
import cn.jarlen.android.aidl.demo.adapter.RvMultiItemManager;

public class MessageAdapter extends RvMultiAdapter<Message> {
    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected void preMultiItemView(RvMultiItemManager itemManager) {

    }
}
