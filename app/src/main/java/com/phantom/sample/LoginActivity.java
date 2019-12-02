package com.phantom.sample;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.phantom.client.ImClient;

public class LoginActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        setTitle("登录");
        EditText et = findViewById(R.id.login_username);
        Button btn = findViewById(R.id.login_btn);
        btn.setOnClickListener(v -> {
            String username = et.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                showToast("用户名不能为空");
                return;
            }
            ImClient.getInstance().initialize(this, BuildConfig.SERVER_URL);
            ImClient.getInstance().authenticate(username, "random");
            startActivity(MainActivity.class);
        });
    }
}
