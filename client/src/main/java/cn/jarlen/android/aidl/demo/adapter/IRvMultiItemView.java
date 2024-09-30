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

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * 多类型RecycleView item接口
 *
 * @param <D>
 */
public interface IRvMultiItemView<D> {

    boolean isForViewType(@NonNull D item, int position);

    RvViewHolder onCreateViewHolder(ViewGroup parent);

    void onBindViewHolder(@NonNull D item, int position,
                          @NonNull RvViewHolder holder, @NonNull List<Object> payloads);

    void onViewRecycled(@NonNull RvViewHolder viewHolder);

    Context getContext();

    @LayoutRes
    int getLayoutResId(D item);

    boolean onFailedToRecycleView(@NonNull RvViewHolder holder);


    void onViewAttachedToWindow(@NonNull RvViewHolder holder);


    void onViewDetachedFromWindow(RvViewHolder holder);
}
