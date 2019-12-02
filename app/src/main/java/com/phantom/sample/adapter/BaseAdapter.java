package com.phantom.sample.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, V extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<V> {

    protected List<T> mData = new ArrayList<>();

    protected Context mContext;

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public BaseAdapter(Context context, List<T> data) {
        if (data != null) {
            this.mData = data;
        }
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(final V holder, int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder, getRealPosition(holder.getAdapterPosition()));
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(holder, getRealPosition(holder.getAdapterPosition()));
                    return true;
                }
            });
        }
        bind(holder, position);
    }

    /**
     * 相当于 onBindViewHolder
     *
     * @param holder   viewHolder
     * @param position position
     */
    public abstract void bind(V holder, int position);

    /**
     * 修正position 适用要多Item布局
     *
     * @param position position
     * @return 数据的下标
     */
    private int getRealPosition(int position) {
        return position;
    }

    /**
     * 全部数据刷新
     *
     * @param data data
     */
    public void replaceData(List<T> data) {
        if (data != null) {
            this.mData.clear();
            mData.addAll(data);
        }
    }

    public void addData(List<T> data) {
        if (data != null) {
            mData.addAll(data);
        }
    }

    public void addData(T data) {
        if (data != null) {
            mData.add(data);
        }
    }


    public T getItem(int position) {
        if (position < 0) {
            return null;
        }
        return position > mData.size() - 1 ? null : mData.get(position);
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
