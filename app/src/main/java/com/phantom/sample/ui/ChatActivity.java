package com.phantom.sample.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.phantom.client.PhantomClient;
import com.phantom.client.manager.OnMessageListener;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.message.Message;
import com.phantom.client.utils.RxHelper;
import com.phantom.sample.R;
import com.phantom.sample.adapter.ChatAdapter;

import java.util.LinkedList;

public class ChatActivity extends BaseActivity implements OnRefreshListener, OnMessageListener {

    private LRecyclerView mRecyclerView;

    private ChatAdapter mChatAdapter;

    private EditText mTextEt;

    private Button mSendBtn;

    private Conversation mConversation;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private int page = 0;

    @Override
    protected void init() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mTextEt = findViewById(R.id.chat_input_et);
        mSendBtn = findViewById(R.id.chat_send_message_tv);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mConversation = bundle.getParcelable("conversation");
            if (mConversation != null) {
                mChatAdapter = new ChatAdapter(this, new LinkedList<>(), mConversation);
                mLRecyclerViewAdapter = new LRecyclerViewAdapter(mChatAdapter);
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this,
                        RecyclerView.VERTICAL, false);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mRecyclerView.setAdapter(mLRecyclerViewAdapter);
                mRecyclerView.setLoadMoreEnabled(false);
                mRecyclerView.setPullRefreshEnabled(true);
                mRecyclerView.setOnRefreshListener(this);
                setTitle(mConversation.getConversationName());
                loadMessage();
            }
        }
        mSendBtn.setOnClickListener(v -> processSendMessage());
        PhantomClient.getInstance().chatManager().addOnMessageListener(this);
        listenSoftInputShow();
    }

    private void listenSoftInputShow() {
        final View rootView = findViewById(android.R.id.content);
        rootView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 800)) {
                scrollToBottom();
            }
        });
    }


    private void loadMessage() {
        Log.d(TAG, "page = " + page);
        PhantomClient.getInstance().chatManager().loadMessage(mConversation, messages -> {
            Log.d(TAG, "加载消息：" + messages);
            if (!messages.isEmpty()) {
                for (Message message : messages) {
                    mChatAdapter.addFirst(message);
                }
                mRecyclerView.refreshComplete();
                mChatAdapter.notifyDataSetChanged();
                if (page == 0) {
                    scrollToBottom();
                }
            } else {
                mRecyclerView.refreshComplete();
                showToast("没有更多数据了");
                mChatAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 滚动到底部
     */
    public void scrollToBottom() {
        mRecyclerView.scrollToPosition(mChatAdapter.getItemCount());
    }

    private void processSendMessage() {
        String text = mTextEt.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            showToast("发送内容不能为空");
        }
        Message message = mConversation.createTextMessage(text);
        message.setOnStatusChangeListener(() -> mChatAdapter.notifyDataSetChanged());
        mChatAdapter.addData(message);
        mChatAdapter.notifyItemChanged(0);
        PhantomClient.getInstance().chatManager().sendMessage(message);
        mTextEt.setText("");
        scrollToBottom();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void onRefresh() {
        showToast("加载更多");
        loadMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhantomClient.getInstance().chatManager().removeOnMessageListener(this);
        PhantomClient.getInstance().chatManager().closeConversation(mConversation.getConversationId());
    }

    @Override
    public void onMessage(Message message) {
        mChatAdapter.addData(message);
        mChatAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    @Override
    public Long conversationId() {
        return mConversation.getConversationId();
    }
}
