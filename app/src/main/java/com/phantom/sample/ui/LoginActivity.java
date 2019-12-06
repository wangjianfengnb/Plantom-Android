package com.phantom.sample.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.phantom.client.utils.StringUtils;
import com.phantom.sample.R;
import com.phantom.sample.api.ApiFactory;
import com.phantom.sample.api.ApiHelper;
import com.phantom.sample.api.RxSubscriber;
import com.phantom.sample.api.UserResponse;


public class LoginActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        setTitle("登录");
        EditText mAccount = findViewById(R.id.login_account);
        EditText mPassword = findViewById(R.id.login_password);
        findViewById(R.id.login_submit).setOnClickListener(v -> processLogin(mAccount, mPassword));
        findViewById(R.id.login_forget).setOnClickListener(v -> showToast("傻的吗？"));
        findViewById(R.id.login_register).setOnClickListener(v -> startActivity(RegisterActivity.class));
    }

    private void processLogin(EditText mAccount, EditText mPassword) {
        String account = mAccount.getText().toString().trim();
        if (TextUtils.isEmpty(account) || !StringUtils.matchPhone(account)) {
            showToast("电话号码不正确");
            return;
        }
        String password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            showToast("密码为空");
            return;
        }

        ApiFactory.getApi().getUser(account)
                .compose(ApiHelper.handleResult())
                .subscribe(new RxSubscriber<UserResponse>(this) {
                    @Override
                    protected void _onError(String msg) {
                        showToast(msg);
                    }

                    @Override
                    protected void _onNext(UserResponse data) {
                        if (data.getUserPassword() == null) {
                            showToast("账号不存在");
                            return;
                        }
                        if (data.getUserPassword().equals(password)) {
                            showToast("登录成功");
                            SharedPreferences sp = getSharedPreferences("phantom", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            String userInfo = JSONObject.toJSONString(data);
                            edit.putString("userInfo", userInfo);
                            edit.apply();
                            Bundle bundle = new Bundle();
                            bundle.putString("userInfo", userInfo);
                            startActivity(MainActivity.class, bundle);
                            finish();
                        } else {
                            showToast("密码错误");
                        }
                    }
                });
    }
}
