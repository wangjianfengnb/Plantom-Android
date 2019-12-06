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
import com.phantom.sample.api.UserResponse;

import java.util.List;

public class FriendAdapter extends BaseAdapter<UserResponse, FriendAdapter.FriendHolder> {

    public FriendAdapter(Context context, List<UserResponse> data) {
        super(context, data);
    }

    @Override
    public void bind(FriendHolder holder, int position) {
        UserResponse groupResponse = mData.get(position);
        holder.tv.setText(groupResponse.getUserName());
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent, false);
        return new FriendHolder(view);
    }

    static class FriendHolder extends RecyclerView.ViewHolder {
        TextView tv;

        FriendHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_friend_name);
        }
    }
}
