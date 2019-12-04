package com.phantom.sample;

import android.text.TextUtils;
import android.widget.EditText;

import com.phantom.client.ImClient;
import com.phantom.sample.constants.Data;

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
        findViewById(R.id.login_submit)
                .setOnClickListener(v -> {
                    String accountName = mAccount.getText().toString().trim();
                    if (TextUtils.isEmpty(accountName)) {
                        showToast("账号为空");
                        return;
                    }
                    String password = mPassword.getText().toString().trim();
                    if (TextUtils.isEmpty(password)) {
                        showToast("密码为空");
                        return;
                    }
                    ImClient.getInstance().authenticate(accountName, password);
                    Data.USER_ID = accountName;
                    startActivity(MainActivity.class);
                });
        findViewById(R.id.login_forget).setOnClickListener(v -> showToast("傻的吗？"));
        findViewById(R.id.login_register).setOnClickListener(v -> showToast("傻的吗？"));
    }
}
