package com.phantom.sample.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.phantom.sample.R;


public class CustomToolBar extends FrameLayout {

    private Toolbar mToolbar;

    private TextView mToolbarTitle;

    private ProgressBar mProgressBar;

    public CustomToolBar(Context context) {
        this(context, null);
    }

    public CustomToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.include_toolbar, this);
        if (!isInEditMode()) {
            mToolbar = findViewById(R.id.toolbar);
            mToolbarTitle = findViewById(R.id.toolbar_title);
            mProgressBar = findViewById(R.id.toolbar_pb);
        }
    }


    /**
     * 设置标题
     *
     * @param title title
     */
    public void setTitle(String title) {
        mToolbarTitle.setText(title);
    }

    /**
     * @return 获取标题
     */
    public String getTitle() {
        return mToolbarTitle.getText().toString().trim();
    }

}
