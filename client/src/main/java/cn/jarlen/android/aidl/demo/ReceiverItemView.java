package cn.jarlen.android.aidl.demo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import cn.jarlen.android.aidl.Message;
import cn.jarlen.android.aidl.demo.adapter.RvMultiItemView;
import cn.jarlen.android.aidl.demo.adapter.RvViewHolder;

public class ReceiverItemView extends RvMultiItemView<Message> {

    public ReceiverItemView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResId(Message item) {
        return R.layout.layout_msg_send;
    }

    @Override
    protected void onBindView(RvViewHolder viewHolder, Message item, int position) {
        ((AppCompatTextView) viewHolder.getView(R.id.tv_client)).setText(item.getClient());
        ((AppCompatTextView) viewHolder.getView(R.id.tv_msg)).setText(item.getMsg());
        ((AppCompatTextView) viewHolder.getView(R.id.tv_time)).setText(Utils.convertTime2String(item.getTime()));
    }

    @Override
    protected boolean isForViewType(@NonNull Message item) {
        return !"client".equals(item.getClient());
    }
}
