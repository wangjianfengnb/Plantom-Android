package com.phantom.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
        boolean self = message.getSenderId().equals(Data.USER_ID);
        if (self) {

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
