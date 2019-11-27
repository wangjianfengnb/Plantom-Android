package com.phantom.sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.phantom.client.ImClient;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText et = findViewById(R.id.username);
        Button btn = findViewById(R.id.login);
        btn.setOnClickListener(v -> {
            String username = et.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            ImClient.getInstance().initialize(BuildConfig.SERVER_URL);
            ImClient.getInstance().authenticate(username, "random");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        });
    }
}
