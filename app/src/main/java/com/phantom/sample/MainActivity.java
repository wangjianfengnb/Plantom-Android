package com.phantom.sample;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.phantom.client.ImClient;
import com.phantom.client.model.Conversation;
import com.phantom.sample.adapter.ConversationAdapter;
import com.phantom.sample.widget.DividerItemDecoration;

public class MainActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener, OnLoadMoreListener {

    private LRecyclerView mRecyclerView;

    private ConversationAdapter mConversationAdapter;

    @Override
    protected void init() {
        setTitle("幻影(15)");
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                R.drawable.bg_list_divider) {
            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
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
        mRecyclerView.setOnLoadMoreListener(this);
        ImClient.getInstance().chatManager().loadConversation(1, 10, conversationList -> {
            mConversationAdapter = new ConversationAdapter(MainActivity.this, conversationList);
            LRecyclerViewAdapter mLAdapter = new LRecyclerViewAdapter(mConversationAdapter);
            mRecyclerView.setAdapter(mLAdapter);
            mLAdapter.setOnItemClickListener(MainActivity.this);
            mLAdapter.setOnItemLongClickListener(MainActivity.this);
        });

        ImClient.getInstance().chatManager().addOnConversationChangeListener((conversation, isNew) -> {
            if (isNew) {
                mConversationAdapter.addFirst(conversation);
            } else {
                mConversationAdapter.update(conversation);
            }
            mConversationAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onItemClick(View view, int position) {
        showToast("点击了会话列表");
        Bundle bundle = new Bundle();
        Conversation conversation = mConversationAdapter.getItem(position);
        bundle.putParcelable("conversation", conversation);
        startActivity(ChatActivity.class, bundle);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        showToast("长按了会话列表");
    }

    @Override
    public void onLoadMore() {

    }
}
