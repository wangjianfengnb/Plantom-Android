package com.phantom.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.phantom.client.model.Conversation;
import com.phantom.sample.App;
import com.phantom.sample.R;
import com.phantom.sample.widget.GlideRoundTransform;

import java.util.List;


public class ConversationAdapter extends BaseAdapter<Conversation, ConversationAdapter.ConversationHolder> {

    public ConversationAdapter(Context context, List<Conversation> data) {
        super(context, data);
    }

    @Override
    public void bind(ConversationHolder holder, int position) {
        Conversation conversation = mData.get(position);
        Glide.with(App.getContext())
                .load(getAvatar())
                .transform(new GlideRoundTransform(mContext, 4))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.ic_avatar_default)
                .into(holder.avatar);

        holder.badge.setText(String.valueOf(conversation.getUnread()));
        holder.message.setText("哈哈");
        holder.name.setText(conversation.getConversationName());
        holder.time.setText("12月30日");

    }

    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_conversation, parent, false);
        return new ConversationHolder(view);
    }

    static final class ConversationHolder extends RecyclerView.ViewHolder {

        ImageView avatar;

        TextView badge;

        TextView name;

        TextView time;

        TextView message;

        ConversationHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.conversation_avatar);
            badge = itemView.findViewById(R.id.conversation_badge);
            name = itemView.findViewById(R.id.conversation_name);
            time = itemView.findViewById(R.id.conversation_time);
            message = itemView.findViewById(R.id.conversation_message);
        }
    }

    private String getAvatar() {
        return "http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg";
    }


}
