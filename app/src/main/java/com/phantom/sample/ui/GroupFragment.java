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
import com.phantom.client.PhantomClient;
import com.phantom.client.model.Conversation;
import com.phantom.sample.R;
import com.phantom.sample.adapter.GroupAdapter;
import com.phantom.sample.api.ApiFactory;
import com.phantom.sample.api.ApiHelper;
import com.phantom.sample.api.GroupResponse;
import com.phantom.sample.api.JoinGroupRequest;
import com.phantom.sample.api.RxSubscriber;
import com.phantom.sample.constants.Data;
import com.phantom.sample.widget.DividerItemDecoration;

import java.util.List;

public class GroupFragment extends BaseFragment implements OnItemClickListener, OnLoadMoreListener {

    private LRecyclerView mRecyclerView;

    private int mPage = 0;

    private int mPageCount = 10;

    private GroupAdapter mGroupAdapter;

    private LRecyclerViewAdapter mLAdapter;


    @Override
    public void init() {
        mRecyclerView = mContentView.findViewById(R.id.recyclerView);
        mGroupAdapter = new GroupAdapter(mActivity, null);
        mLAdapter = new LRecyclerViewAdapter(mGroupAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
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
        return R.layout.fragment_group;
    }

    private void loadData(boolean loadMore) {
        ApiFactory.getApi().listGroup(mPage, mPageCount)
                .compose(ApiHelper.handleResult())
                .subscribe(new RxSubscriber<List<GroupResponse>>(mActivity) {
                    @Override
                    protected void _onError(String msg) {
                        mActivity.showToast(msg);
                    }

                    @Override
                    protected void _onNext(List<GroupResponse> data) {
                        onData(data, loadMore);
                    }
                });
    }


    private void onData(List<GroupResponse> data, boolean isLoadMore) {
        if (isLoadMore) {
            if (data.size() < mPageCount) {
                RecyclerViewStateUtils.setFooterViewState(mActivity,
                        mRecyclerView, mPageCount, LoadingFooter.State.TheEnd, null);
            } else {
                RecyclerViewStateUtils.setFooterViewState(mActivity,
                        mRecyclerView, mPageCount, LoadingFooter.State.Normal, null);
            }
            mGroupAdapter.addData(data);
            mGroupAdapter.notifyDataSetChanged();
        } else {
            if (data.size() < mPageCount) {
                mRecyclerView.setLoadMoreEnabled(false);
            }
            mGroupAdapter.replaceData(data);
            mGroupAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        new SweetAlertDialog(mActivity, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("激情聊天")
                .setContentText("确定加入群聊？")
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(sweetAlertDialog -> {
                    GroupResponse item = mGroupAdapter.getItem(position);

                    JoinGroupRequest request = new JoinGroupRequest();
                    request.setGroupId(item.getGroupId());
                    request.setUserAccount(Data.USER.getUserAccount());
                    ApiFactory.getApi().joinGroup(request)
                            .compose(ApiHelper.handleResult())
                            .subscribe(new RxSubscriber<Boolean>(mActivity) {
                                @Override
                                protected void _onError(String msg) {
                                    showToast(msg);
                                }

                                @Override
                                protected void _onNext(Boolean data) {
                                    if (data) {
                                        showToast("加入成功");
                                        openChat(item.getGroupId());
                                    } else {
                                        showToast("加入失败");
                                    }
                                }
                            });
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    private void openChat(Long groupId) {
        PhantomClient.getInstance().chatManager()
                .openConversation(Conversation.TYPE_C2G, String.valueOf(groupId), conversation -> {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("conversation", conversation);
                    mActivity.startActivity(ChatActivity.class, bundle);
                    mActivity.finish();
                });


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
