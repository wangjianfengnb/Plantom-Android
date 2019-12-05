package com.phantom.sample.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.phantom.client.model.message.Message;
import com.phantom.sample.App;
import com.phantom.sample.R;
import com.phantom.sample.constants.Data;
import com.phantom.sample.widget.GlideRoundTransform;

public class TextMessage extends BaseMessage {

    public TextMessage(Message message) {
        super(message);
    }

    @Override
    public void bindView(FrameLayout root) {
        root.removeAllViews();
        boolean self = message.getSenderId().equals(Data.USER.getUserAccount());
        if (self) {
            View view = LayoutInflater.from(root.getContext()).inflate(R.layout.item_chat_send_text,
                    root, false);
            root.addView(view);
            ImageView failureIv = view.findViewById(R.id.item_chat_send_failure);
            ProgressBar sendPb = view.findViewById(R.id.item_chat_send_progress);
            TextView textTv = view.findViewById(R.id.item_chat_send_text_message);
            ImageView avatarTv = view.findViewById(R.id.item_chat_avatar);
            String content = message.getContent();
            textTv.setText(content);
            Glide.with(App.getContext())
                    .load(Data.getAvatar())
                    .transform(new GlideRoundTransform(root.getContext(), 4))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .error(R.drawable.ic_avatar_default)
                    .into(avatarTv);
            failureIv.setVisibility(message.getStatus() == Message.STATUS_SEND_FAILURE ? View.VISIBLE : View.GONE);
            sendPb.setVisibility(message.getStatus() == Message.STATUS_SENDING ? View.VISIBLE : View.GONE);
        } else {
            View view = LayoutInflater.from(root.getContext()).inflate(R.layout.item_chat_receive_text,
                    root, false);
            root.addView(view);
            TextView tv = view.findViewById(R.id.item_chat_receive_text_message);
            ImageView iv = view.findViewById(R.id.item_chat_avatar);
            String content = message.getContent();
            tv.setText(content);
            Glide.with(App.getContext())
                    .load(Data.getAvatar())
                    .transform(new GlideRoundTransform(root.getContext(), 4))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .error(R.drawable.ic_avatar_default)
                    .into(iv);
        }
    }
}
