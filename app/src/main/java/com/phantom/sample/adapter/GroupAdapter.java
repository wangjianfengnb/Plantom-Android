package com.phantom.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phantom.sample.R;
import com.phantom.sample.api.GroupResponse;

import java.util.List;

public class GroupAdapter extends BaseAdapter<GroupResponse, GroupAdapter.GroupHolder> {

    public GroupAdapter(Context context, List<GroupResponse> data) {
        super(context, data);
    }

    @Override
    public void bind(GroupHolder holder, int position) {
        GroupResponse groupResponse = mData.get(position);
        holder.tv.setText(groupResponse.getGroupName());
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_group, parent, false);
        return new GroupAdapter.GroupHolder(view);
    }

    static class GroupHolder extends RecyclerView.ViewHolder {
        TextView tv;
        GroupHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_group_name);
        }
    }
}
