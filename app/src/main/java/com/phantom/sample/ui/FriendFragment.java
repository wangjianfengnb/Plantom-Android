package com.phantom.sample.ui;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.phantom.client.ImClient;
import com.phantom.client.manager.OnConversationLoadListener;
import com.phantom.client.model.Conversation;
import com.phantom.sample.R;
import com.phantom.sample.adapter.FriendAdapter;
import com.phantom.sample.api.ApiFactory;
import com.phantom.sample.api.ApiHelper;
import com.phantom.sample.api.RxSubscriber;
import com.phantom.sample.api.UserResponse;
import com.phantom.sample.constants.Data;
import com.phantom.sample.widget.DividerItemDecoration;

import java.util.List;

public class FriendFragment extends BaseFragment implements OnItemClickListener, OnLoadMoreListener {

    private LRecyclerView mRecyclerView;

    private int mPage = 0;

    private int mPageCount = 10;

    private FriendAdapter mFriendAdapter;

    private LRecyclerViewAdapter mLAdapter;


    @Override
    public void init() {
        mRecyclerView = mContentView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mFriendAdapter = new FriendAdapter(mActivity, null);
        mLAdapter = new LRecyclerViewAdapter(mFriendAdapter);
        mRecyclerView.setAdapter(mLAdapter);
        mRecyclerView.setPullRefreshEnabled(false);
        mLAdapter.setOnItemClickListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity,
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
        loadData(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend;
    }

    private void loadData(boolean loadMore) {
        ApiFactory.getApi().listUser(Data.USER.getUserAccount(), mPage, mPageCount)
                .compose(ApiHelper.handleResult())
                .subscribe(new RxSubscriber<List<UserResponse>>(mActivity) {
                    @Override
                    protected void _onError(String msg) {
                        mActivity.showToast(msg);
                    }

                    @Override
                    protected void _onNext(List<UserResponse> data) {
                        onData(data, loadMore);
                    }
                });
    }


    private void onData(List<UserResponse> data, boolean isLoadMore) {
        if (isLoadMore) {
            if (data.size() < mPageCount) {
                RecyclerViewStateUtils.setFooterViewState(mActivity,
                        mRecyclerView, mPageCount, LoadingFooter.State.TheEnd, null);
            } else {
                RecyclerViewStateUtils.setFooterViewState(mActivity,
                        mRecyclerView, mPageCount, LoadingFooter.State.Normal, null);
            }
            mFriendAdapter.addData(data);
            mFriendAdapter.notifyDataSetChanged();
        } else {
            if (data.size() < mPageCount) {
                mRecyclerView.setLoadMoreEnabled(false);
            }
            mFriendAdapter.replaceData(data);
            mFriendAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        new SweetAlertDialog(mActivity, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("激情聊天")
                .setContentText("确定开启激情聊天")
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(sweetAlertDialog -> {
                    UserResponse item = mFriendAdapter.getItem(position);
                    ImClient.getInstance().chatManager()
                            .openConversation(Conversation.TYPE_C2C, item.getUserAccount(), conversation -> {
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("conversation", conversation);
                                mActivity.startActivity(ChatActivity.class, bundle);
                                mActivity.finish();
                            });
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    @Override
    public void onLoadMore() {
        if (RecyclerViewStateUtils.getFooterViewState(mRecyclerView) == LoadingFooter.State.Loading) {
            return;
        }
        mPage++;
        RecyclerViewStateUtils.setFooterViewState(mActivity,
                mRecyclerView, mPageCount, LoadingFooter.State.Loading, null);
        loadData(true);
    }
}
