package com.phantom.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phantom.client.model.message.Message;
import com.phantom.sample.R;

import java.util.List;

public class ChatAdapter extends BaseAdapter<Message, ChatAdapter.ChatHolder> {

    public ChatAdapter(Context context, List<Message> data) {
        super(context, data);
    }

    @Override
    public void bind(ChatHolder chatHolder, int position) {
        BaseMessage message = MessageFactory.getMessage(mData.get(position));
        if (message != null) {
            message.bindView(chatHolder.root);
        }
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item, parent, false);
        return new ChatHolder(view);
    }

    public void addFirst(Message message) {
        mData.add(0, message);
    }

    static class ChatHolder extends RecyclerView.ViewHolder {

        FrameLayout root;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.chat_item_root);
        }
    }
}
