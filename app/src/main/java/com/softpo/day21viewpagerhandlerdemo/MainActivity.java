package com.softpo.day21viewpagerhandlerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //动态添加Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,new ViewPagerHandlerFragment(),"myFragment")
                .commit();
    }
}
