package com.phantom.sample.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.phantom.client.PhantomClient;
import com.phantom.client.manager.OnConversationChangeListener;
import com.phantom.client.model.Conversation;
import com.phantom.sample.R;
import com.phantom.sample.adapter.ConversationAdapter;
import com.phantom.sample.api.UserResponse;
import com.phantom.sample.constants.Data;
import com.phantom.sample.widget.DividerItemDecoration;


public class MainActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener, OnConversationChangeListener {

    private LRecyclerView mRecyclerView;

    private ConversationAdapter mConversationAdapter;

    @Override
    protected void init() {
        initTitle(0);
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String account = extras.getString("userInfo");
        UserResponse userResponse = JSONObject.parseObject(account, UserResponse.class);
        Data.USER = userResponse;
        assert userResponse != null;
        TextView name = findViewById(R.id.main_name_tv);
        name.setText(String.format("欢迎: %s (%s)", userResponse.getUserName(), userResponse.getUserAccount()));
        PhantomClient.getInstance().login(userResponse.getUserAccount(), userResponse.getUserPassword());
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                R.drawable.bg_list_divider) {
            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int childCount = parent.getChildCount();
                int right = parent.getWidth();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);

                    RecyclerView.LayoutParams params =
                            (RecyclerView.LayoutParams) child.getLayoutParams();
                    int left = (int) getResources().getDimension(R.dimen.x50);
                    int bottom = child.getTop() - params.topMargin;
                    int top = bottom - mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        });
        mRecyclerView.setPullRefreshEnabled(false);

        PhantomClient.getInstance().chatManager().loadConversation(1, 10, conversationList -> {
            mConversationAdapter = new ConversationAdapter(MainActivity.this, conversationList);
            LRecyclerViewAdapter mLAdapter = new LRecyclerViewAdapter(mConversationAdapter);
            mRecyclerView.setAdapter(mLAdapter);
            mRecyclerView.setLoadMoreEnabled(false);
            mLAdapter.setOnItemClickListener(MainActivity.this);
            mLAdapter.setOnItemLongClickListener(MainActivity.this);
        });

        PhantomClient.getInstance().chatManager().addOnConversationChangeListener(this);

        findViewById(R.id.open_conversation_btn)
                .setOnClickListener(v -> {
                    startActivity(CreateChatActivity.class);
                });
        findViewById(R.id.logout_btn)
                .setOnClickListener(v -> {
                    PhantomClient.getInstance().logout();
                    SharedPreferences sp = getSharedPreferences("phantom", Context.MODE_PRIVATE);
                    sp.edit().remove("userInfo").apply();
                    startActivity(LoginActivity.class);
                    finish();
                });
    }

    private void initTitle(int count) {
        if (count > 0) {
            setTitle("幻影(" + count + ")");
        } else {
            setTitle("幻影");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        Conversation conversation = mConversationAdapter.getItem(position);
        bundle.putParcelable("conversation", conversation);
        startActivity(ChatActivity.class, bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhantomClient.getInstance().chatManager().removeOnConversationChangeListener(this);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        showToast("长按了会话列表");
    }

    @Override
    public void onConversationChange(Conversation conversation, boolean isNew) {
        if (isNew) {
            mConversationAdapter.addFirst(conversation);
        } else {
            mConversationAdapter.update(conversation);
        }
        mConversationAdapter.notifyDataSetChanged();
        initTitle(mConversationAdapter.getTotalUnRead());
    }

}
