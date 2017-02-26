package com.kyaracter.kiiannotation.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kii.cloud.storage.Kii;
import com.kyaracter.kiiannotation.example.entity.AppData;
import com.kyaracter.kiiannotation.example.entity.GroupBucket;
import com.kyaracter.kiiannotation.example.entity.UserBucket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppData appBucket = AppData.create();

        GroupBucket groupBucket = new GroupBucket.Builder(Kii.group("aaa"))
                .name("bbb")
                .build();

        UserBucket userBucket = UserBucket.create();
    }
}
