package com.phantom.sample.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.phantom.sample.R;
import com.phantom.sample.widget.CustomToolBar;

public abstract class BaseActivity extends AppCompatActivity {

    public String TAG = getClass().getSimpleName() + ": %s";

    protected Toast mToast;

    protected CustomToolBar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mToolbar = findViewById(R.id.custom_toolbar);
        init();
    }

    protected abstract void init();


    /**
     * 获取布局ID
     *
     * @return 布局id
     */
    public abstract int getLayoutId();

    /**
     * 设置标题
     */
    @Override
    public void setTitle(int resId) {
        setTitle(getString(resId));
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    /**
     * 开启Activity
     */
    public void startActivity(Class<? extends Context> clazz) {
        if (clazz != null) {
            startActivity(clazz, null);
        }
    }

    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }


    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
