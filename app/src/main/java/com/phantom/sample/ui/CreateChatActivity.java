package com.phantom.sample.ui;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.phantom.sample.R;
import com.phantom.sample.adapter.CreateChatPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateChatActivity extends BaseActivity {

    private ViewPager mViewPager;

    @Override
    protected void init() {
        setTitle("创建会话");
        mViewPager = findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FriendFragment());
        fragments.add(new GroupFragment());
        CreateChatPageAdapter adapter = new CreateChatPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_chat;
    }
}
