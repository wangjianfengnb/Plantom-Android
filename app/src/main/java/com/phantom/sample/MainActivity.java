package com.phantom.sample;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.phantom.client.model.Conversation;
import com.phantom.client.model.TextMessage;
import com.phantom.sample.adapter.ConversationAdapter;
import com.phantom.sample.widget.DividerItemDecoration;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener, OnLoadMoreListener {

    private LRecyclerView mRecyclerView;

    @Override
    protected void init() {
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
        ConversationAdapter conversationAdapter = new ConversationAdapter(this, createConversation());
        LRecyclerViewAdapter mLAdapter = new LRecyclerViewAdapter(conversationAdapter);
        mRecyclerView.setAdapter(mLAdapter);
        mRecyclerView.setPullRefreshEnabled(false);
        mLAdapter.setOnItemClickListener(this);
        mLAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
    }

    private List<Conversation> createConversation() {
        Conversation c1 = new Conversation();
        c1.setConversationId(1L);
        c1.setConversationName("1号玩家");
        TextMessage message = new TextMessage();
        message.setContent("吃饭了吗");
        c1.setLastMessage(message);
        c1.setTargetId("123");
        c1.setType(Conversation.TYPE_C2C);
        c1.setUnread(6);

        Conversation c2 = new Conversation();
        c2.setConversationId(1L);
        c2.setConversationName("2号玩家");
        TextMessage message2 = new TextMessage();
        message2.setContent("吃饭了吗");
        c2.setLastMessage(message2);
        c2.setTargetId("333");
        c2.setType(Conversation.TYPE_C2C);
        c2.setUnread(3);
        return Arrays.asList(c1, c2);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onLoadMore() {

    }
}
