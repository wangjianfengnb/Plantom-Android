package com.phantom.sample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.phantom.client.ImClient;
import com.phantom.client.model.Conversation;
import com.phantom.sample.adapter.ChatAdapter;

import java.util.ArrayList;

public class ChatActivity extends BaseActivity implements OnRefreshListener {

    private LRecyclerView mRecyclerView;

    private ChatAdapter mChatAdapter;

    private EditText mTextEt;

    private Button mSendBtn;

    @Override
    protected void init() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mTextEt = findViewById(R.id.chat_input_et);
        mSendBtn = findViewById(R.id.chat_send_message_tv);
        mChatAdapter = new ChatAdapter(this, new ArrayList<>());
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
            Conversation conversation = bundle.getParcelable("conversation");
            setTitle(conversation.getConversationName());
            ImClient.getInstance().chatManager().loadMessage(conversation, messages -> {
                mChatAdapter.addData(messages);
            });

        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void onRefresh() {
        showToast("加载更多");
    }
}
