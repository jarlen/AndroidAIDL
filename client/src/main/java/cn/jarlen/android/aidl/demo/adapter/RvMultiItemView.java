/*
 * Copyright (C) 2016 jarlen
 * fork form https://github.com/sockeqwe/AdapterDelegates
 * and modify
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.jarlen.android.aidl.demo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * DESCRIBE:
 * Created by jarlen on 2017/1/11.
 */

public abstract class RvMultiItemView<D> implements IRvMultiItemView<D> {

    protected Context mContext;
    private D data;
    private int position;

    public RvMultiItemView(Context context) {
        this.mContext = context;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent) {
        return RvViewHolder.getViewHolder(mContext, parent, getLayoutResId(data));
    }

    @Override
    public boolean isForViewType(@NonNull D item, int position) {
        boolean match = isForViewType(item);
        if (match) {
            this.data = item;
            this.position = position;
            return true;
        }
        return false;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull D item, int position, @NonNull RvViewHolder holder, @NonNull List<Object> payloads) {
        this.data = item;
        onBindView(holder, item, position);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    protected D getData() {
        return data;
    }

    protected abstract void onBindView(RvViewHolder viewHolder, D item, int position);

    protected abstract boolean isForViewType(@NonNull D item);

    @Override
    public boolean onFailedToRecycleView(@NonNull RvViewHolder holder) {
        return false;
    }

    @Override
    public void onViewRecycled(@NonNull RvViewHolder viewHolder) {

    }

    @Override
    public void onViewAttachedToWindow(@NonNull RvViewHolder holder) {

    }

    @Override
    public void onViewDetachedFromWindow(RvViewHolder holder) {

    }
}
