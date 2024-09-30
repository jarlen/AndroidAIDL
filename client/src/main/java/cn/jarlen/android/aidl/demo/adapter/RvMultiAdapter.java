/*
 * Copyright (C) 2016 jarlen
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

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * DESCRIBE:
 * Created by jarlen on 2017/1/11.
 */

public abstract class RvMultiAdapter<D> extends RecyclerView.Adapter<RvViewHolder> {

    public Context mContext = null;

    private RvMultiItemManager<D> itemManager;

    protected List<D> listData = new ArrayList<D>();

    public RvMultiAdapter(Context context) {
        this.mContext = context;
        itemManager = new RvMultiItemManager<D>();
        preMultiItemView(itemManager);
    }

    @Override
    public int getItemViewType(int position) {
        if (listData == null || listData.isEmpty()) {
            return super.getItemViewType(position);
        }
        return itemManager.getItemViewType(listData.get(position), position);
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return itemManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        itemManager.onBindViewHolder(listData.get(position), position, holder);
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position, List payloads) {
        itemManager.onBindViewHolder(listData.get(position), position, holder, payloads);
    }

    @Override
    public int getItemCount() {
        if (listData == null) {
            return 0;
        }
        return listData.size();
    }

    public void setDataList(List<D> mList) {
        if (listData == null) {
            listData = new ArrayList<>();
        }
        listData.clear();
        listData.addAll(mList);
        this.notifyDataSetChanged();
    }

    public List<D> getAllData() {
        return listData;
    }

    public void addDataList(List<D> mList) {
        if (listData != null) {
            listData.addAll(mList);
        }
        this.notifyItemMoved(0, listData.size() - 1);
    }

    public void addData(D data) {
        if (listData != null) {
            listData.add(data);
        }
        this.notifyItemMoved(listData.size() - 2, listData.size() - 1);
    }

    public void addData(int position, D data) {
        if (listData != null) {
            listData.add(position, data);
        }
        this.notifyItemMoved(0, listData.size() - 1);
    }

    public D getData(int position) {
        return listData.get(position);
    }

    public void clearDataList() {
        if (listData != null) {
            listData.clear();
        }
        this.notifyDataSetChanged();
    }

    public void removeData(int position) {
        if (listData != null) {
            listData.remove(position);
        }
        this.notifyDataSetChanged();
    }

    public void removeData(D item) {
        if (listData != null) {
            listData.remove(item);
        }
        this.notifyDataSetChanged();
    }

    public void removeDatas() {
        if (listData != null) {
            listData.clear();
        }
        this.notifyDataSetChanged();
    }

    protected abstract void preMultiItemView(RvMultiItemManager itemManager);

}
