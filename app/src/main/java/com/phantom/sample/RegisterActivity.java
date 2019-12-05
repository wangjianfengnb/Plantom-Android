package com.phantom.sample;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.phantom.client.utils.StringUtils;
import com.phantom.sample.api.ApiFactory;
import com.phantom.sample.api.ApiHelper;
import com.phantom.sample.api.CreateUserRequest;
import com.phantom.sample.api.RxSubscriber;
import com.phantom.sample.constants.Data;

public class RegisterActivity extends BaseActivity {

    private EditText userName;
    private EditText userAccount;
    private EditText userPassword;

    @Override
    protected void init() {
        setTitle("注册");
        Button btn = findViewById(R.id.register_btn);
        userName = findViewById(R.id.register_user_name);
        userAccount = findViewById(R.id.register_user_account);
        userPassword = findViewById(R.id.register_user_password);
        btn.setOnClickListener(v -> processRegister());
    }

    private void processRegister() {
        String name = userName.getText().toString().trim();
        String account = userAccount.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showToast("用户名为空");
            return;
        }
        if (TextUtils.isEmpty(account) || !StringUtils.matchPhone(account)) {
            showToast("用户账号错误，电话号码不正确");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            showToast("密码不能小于6位");
            return;
        }
        CreateUserRequest request = new CreateUserRequest();
        request.setUserAccount(account);
        request.setUserName(name);
        request.setUserPassword(password);
        request.setAvatar(Data.getAvatar());
        ApiFactory.getApi().createUser(request)
                .compose(ApiHelper.handleResult())
                .subscribe(new RxSubscriber<Boolean>(this) {
                    @Override
                    protected void _onError(String msg) {
                        showToast(msg);
                    }

                    @Override
                    protected void _onNext(Boolean data) {
                        if (data) {
                            showToast("创建成功");
                            finish();
                        } else {
                            showToast("创建失败");
                        }
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }
}
