package com.phantom.sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.phantom.client.ImClient;
import com.phantom.client.manager.OnMessageListener;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.message.Message;
import com.phantom.sample.adapter.ChatAdapter;

import java.util.LinkedList;

public class ChatActivity extends BaseActivity implements OnRefreshListener, OnMessageListener {

    private LRecyclerView mRecyclerView;

    private ChatAdapter mChatAdapter;

    private EditText mTextEt;

    private Button mSendBtn;

    private Conversation mConversation;

    @Override
    protected void init() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mTextEt = findViewById(R.id.chat_input_et);
        mSendBtn = findViewById(R.id.chat_send_message_tv);
        mChatAdapter = new ChatAdapter(this, new LinkedList<>());
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mChatAdapter);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLoadMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setOnRefreshListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mConversation = bundle.getParcelable("conversation");
            if (mConversation != null) {
                setTitle(mConversation.getConversationName());
                ImClient.getInstance().chatManager().loadMessage(mConversation, messages -> mChatAdapter.addData(messages));
            }
        }
        mSendBtn.setOnClickListener(v -> processSendMessage());
        ImClient.getInstance().chatManager().addOnMessageListener(this);

    }

    private void processSendMessage() {
        String text = mTextEt.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            showToast("发送内容不能为空");
        }
        Message message = mConversation.createTextMessage(text);
        message.setOnStatusChangeListener(() -> mChatAdapter.notifyDataSetChanged());
        mChatAdapter.addFirst(message);
        mChatAdapter.notifyItemChanged(0);
        ImClient.getInstance().chatManager().sendMessage(message);
        mTextEt.setText("");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void onRefresh() {
        showToast("加载更多");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImClient.getInstance().chatManager().closeConversation(mConversation.getConversationId());
    }

    @Override
    public void onMessage(Message message) {
        mChatAdapter.addFirst(message);
        mChatAdapter.notifyItemChanged(0);
    }

    @Override
    public Long conversationId() {
        return mConversation.getConversationId();
    }
}
