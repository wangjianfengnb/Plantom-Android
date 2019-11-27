package com.phantom.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.phantom.client.ImClient;
import com.phantom.client.MessageListener;
import com.phantom.client.model.request.OfflineMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv  = findViewById(R.id.content);
    }
}
