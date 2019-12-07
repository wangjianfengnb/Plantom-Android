package com.phantom.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.message.Message;
import com.phantom.sample.App;
import com.phantom.sample.R;
import com.phantom.sample.api.ApiFactory;
import com.phantom.sample.api.ApiHelper;
import com.phantom.sample.api.RxSubscriber;
import com.phantom.sample.api.UserResponse;
import com.phantom.sample.constants.Data;
import com.phantom.sample.widget.GlideRoundTransform;

import rx.Observable;

public class TextMessage extends BaseMessage {

    TextMessage(Message message, Conversation conversation) {
        super(message, conversation);
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
            failureIv.setVisibility(message.getStatus() == Message.STATUS_SEND_FAILURE ? View.VISIBLE : View.GONE);
            sendPb.setVisibility(message.getStatus() == Message.STATUS_SENDING ? View.VISIBLE : View.GONE);
            TextView nameTv = view.findViewById(R.id.item_chat_send_name);
            if (conversation.getConversationType() == Conversation.TYPE_C2C) {
                nameTv.setText(Data.USER.getUserName());
                Glide.with(App.getContext())
                        .load(Data.USER.getAvatar())
                        .transform(new GlideRoundTransform(root.getContext(), 4))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.ic_avatar_default)
                        .into(avatarTv);
            } else {
                loadUserInfo(root, avatarTv, nameTv);
            }
        } else {
            View view = LayoutInflater.from(root.getContext()).inflate(R.layout.item_chat_receive_text,
                    root, false);
            root.addView(view);
            TextView tv = view.findViewById(R.id.item_chat_receive_text_message);
            ImageView avatarIv = view.findViewById(R.id.item_chat_avatar);
            String content = message.getContent();
            tv.setText(content);
            TextView nameTv = view.findViewById(R.id.item_chat_receive_name);
            if (conversation.getConversationType() == Conversation.TYPE_C2C) {
                nameTv.setText(conversation.getConversationName());
                Glide.with(App.getContext())
                        .load(conversation.getConversationAvatar())
                        .transform(new GlideRoundTransform(root.getContext(), 4))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.ic_avatar_default)
                        .into(avatarIv);
            } else {
                loadUserInfo(root, avatarIv, nameTv);
            }
        }
    }

    private void loadUserInfo(FrameLayout root, ImageView iv, TextView nameTv) {
        Observable<UserResponse> fromNetwork = ApiFactory.getApi().getUser(message.getSenderId())
                .compose(ApiHelper.handleResult());
        ApiHelper.load("userInfo-" + message.getSenderId(), fromNetwork, 30 * 24 * 60 * 100L, false)
                .subscribe(new RxSubscriber<UserResponse>(root.getContext(), false) {
                    @Override
                    protected void _onError(String msg) {

                    }

                    @Override
                    protected void _onNext(UserResponse data) {
                        nameTv.setText(data.getUserName());
                        Glide.with(App.getContext())
                                .load(data.getAvatar())
                                .transform(new GlideRoundTransform(root.getContext(), 4))
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .error(R.drawable.ic_avatar_default)
                                .into(iv);
                    }
                });
    }
}
