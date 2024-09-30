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

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;

import java.util.Collections;
import java.util.List;

public class RvMultiItemManager<T> {

    /**
     * This id is used internally to claim that the
     */
    private static final int FALLBACK_MULTI_VIEW_TYPE = Integer.MAX_VALUE - 1;

    private static final List<Object> PAYLOADS_EMPTY_LIST = Collections.emptyList();

    /**
     * Map for ViewType to multiItemView
     */
    protected SparseArrayCompat<IRvMultiItemView<T>> multiItemViews = new SparseArrayCompat();

    protected IRvMultiItemView<T> fallbackMultiItemView;

    public RvMultiItemManager<T> addMultiItemView(@NonNull IRvMultiItemView<T> multiItemView) {
        // algorithm could be improved since there could be holes,
        // but it's very unlikely that we reach Integer.MAX_VALUE and run out of unused indexes
        int viewType = multiItemViews.size();
        while (multiItemViews.get(viewType) != null) {
            viewType++;
            if (viewType == FALLBACK_MULTI_VIEW_TYPE) {
                throw new IllegalArgumentException(
                        "Oops, we are very close to Integer.MAX_VALUE. It seems that there are no more free and unused view type integers left to add another AdapterDelegate.");
            }
        }
        return addMultiItemView(viewType, false, multiItemView);
    }

    public RvMultiItemManager<T> addMultiItemView(int viewType,
                                                  @NonNull IRvMultiItemView<T> itemView) {
        return addMultiItemView(viewType, false, itemView);
    }

    public RvMultiItemManager<T> addMultiItemView(int viewType, boolean allowReplacingaItemView,
                                                  @NonNull IRvMultiItemView<T> multiItemView) {

        if (multiItemView == null) {
            throw new NullPointerException("multiItemView is null!");
        }

        if (viewType == FALLBACK_MULTI_VIEW_TYPE) {
            throw new IllegalArgumentException("The view type = "
                    + FALLBACK_MULTI_VIEW_TYPE
                    + " is reserved for fallback adapter itemView (see setFallbackMultiItemView() ). Please use another view type.");
        }

        if (!allowReplacingaItemView && multiItemViews.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An multiItemView is already registered for the viewType = "
                            + viewType
                            + ". Already registered multiItemView is "
                            + multiItemViews.get(viewType));
        }

        multiItemViews.put(viewType, multiItemView);

        return this;
    }

    public RvMultiItemManager<T> removeMultiItemView(@NonNull IRvMultiItemView<T> multiItemView) {

        if (multiItemView == null) {
            throw new NullPointerException("multiItemView is null");
        }

        int indexToRemove = multiItemViews.indexOfValue(multiItemView);

        if (indexToRemove >= 0) {
            multiItemViews.removeAt(indexToRemove);
        }
        return this;
    }

    public RvMultiItemManager<T> removeMultiItemView(int viewType) {
        multiItemViews.remove(viewType);
        return this;
    }

    public int getItemViewType(@NonNull T item, int position) {

        if (item == null) {
            throw new NullPointerException("Item data source is null!");
        }

        int itemViewCount = multiItemViews.size();
        for (int i = 0; i < itemViewCount; i++) {
            IRvMultiItemView<T> itemView = multiItemViews.valueAt(i);
            boolean match = itemView.isForViewType(item, position);
            if (match) {
                int resId = itemView.getLayoutResId(item);
                if (resId != i) {
                    multiItemViews.removeAt(i);
                    multiItemViews.put(resId, itemView);
                }
                return resId;
            }
        }

        if (fallbackMultiItemView != null) {
            return FALLBACK_MULTI_VIEW_TYPE;
        }

        throw new NullPointerException(
                "No multiItemView added that matches position=" + position + " in data source");
    }

    @NonNull
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IRvMultiItemView<T> multiItemView = getItemForViewType(viewType);
        if (multiItemView == null) {
            throw new NullPointerException("No multiItemView added for ViewType " + viewType);
        }

        RvViewHolder vh = multiItemView.onCreateViewHolder(parent);
        if (vh == null) {
            throw new NullPointerException("ViewHolder returned from multiItemView "
                    + multiItemView
                    + " for ViewType ="
                    + viewType
                    + " is null!");
        }
        return vh;
    }

    public void onBindViewHolder(@NonNull T item, int position,
                                 @NonNull RvViewHolder viewHolder, List payloads) {

        IRvMultiItemView<T> multiItemView = getItemForViewType(viewHolder.getItemViewType());
        if (multiItemView == null) {
            throw new NullPointerException("No multiItemView found for item at position = "
                    + position
                    + " for viewType = "
                    + viewHolder.getItemViewType());
        }
        multiItemView.onBindViewHolder(item, position, viewHolder,
                payloads != null ? payloads : PAYLOADS_EMPTY_LIST);
    }

    public void onBindViewHolder(@NonNull T item, int position,
                                 @NonNull RvViewHolder viewHolder) {
        onBindViewHolder(item, position, viewHolder, PAYLOADS_EMPTY_LIST);
    }

    public void onViewRecycled(@NonNull RvViewHolder viewHolder) {
        IRvMultiItemView<T> multiItemView = getItemForViewType(viewHolder.getItemViewType());
        if (multiItemView == null) {
            throw new NullPointerException("No multiItemView found for "
                    + viewHolder
                    + " for item at position = "
                    + viewHolder.getAdapterPosition()
                    + " for viewType = "
                    + viewHolder.getItemViewType());
        }
        multiItemView.onViewRecycled(viewHolder);
    }

    public boolean onFailedToRecycleView(@NonNull RvViewHolder viewHolder) {
        IRvMultiItemView<T> multiItemView = getItemForViewType(viewHolder.getItemViewType());
        if (multiItemView == null) {
            throw new NullPointerException("No multiItemView found for "
                    + viewHolder
                    + " for item at position = "
                    + viewHolder.getAdapterPosition()
                    + " for viewType = "
                    + viewHolder.getItemViewType());
        }
        return multiItemView.onFailedToRecycleView(viewHolder);
    }

    public void onViewAttachedToWindow(RvViewHolder viewHolder) {
        IRvMultiItemView<T> multiItemView = getItemForViewType(viewHolder.getItemViewType());
        if (multiItemView == null) {
            throw new NullPointerException("No multiItemView found for "
                    + viewHolder
                    + " for item at position = "
                    + viewHolder.getAdapterPosition()
                    + " for viewType = "
                    + viewHolder.getItemViewType());
        }
        multiItemView.onViewAttachedToWindow(viewHolder);
    }

    public void onViewMultiItemViewFromWindow(RvViewHolder viewHolder) {
        IRvMultiItemView<T> multiItemView = getItemForViewType(viewHolder.getItemViewType());
        if (multiItemView == null) {
            throw new NullPointerException("No multiItemView found for "
                    + viewHolder
                    + " for item at position = "
                    + viewHolder.getAdapterPosition()
                    + " for viewType = "
                    + viewHolder.getItemViewType());
        }
        multiItemView.onViewDetachedFromWindow(viewHolder);
    }


    public RvMultiItemManager<T> setFallbackMultiItemView(
            @Nullable IRvMultiItemView<T> multiItemView) {
        this.fallbackMultiItemView = multiItemView;
        return this;
    }


    public int getViewType(@NonNull IRvMultiItemView<T> multiItemView) {
        if (multiItemView == null) {
            throw new NullPointerException("multiItemView is null");
        }

        int index = multiItemViews.indexOfValue(multiItemView);
        if (index == -1) {
            return -1;
        }
        return multiItemViews.keyAt(index);
    }


    @Nullable
    public IRvMultiItemView<T> getItemForViewType(int viewType) {
        IRvMultiItemView<T> multiItemView = multiItemViews.get(viewType);
        if (multiItemView == null) {
            if (fallbackMultiItemView == null) {
                return null;
            } else {
                return fallbackMultiItemView;
            }
        }

        return multiItemView;
    }

    @Nullable
    public IRvMultiItemView<T> getFallbackMultiItemView() {
        return fallbackMultiItemView;
    }
}
