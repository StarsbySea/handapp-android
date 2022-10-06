package com.example.text1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class launcher extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences setting = getSharedPreferences("com.example.text1", 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {// 第一次则跳转到欢迎页面
            setting.edit().putBoolean("FIRST", false).commit();


            Intent intent=new Intent(this, SplashActivity.class);
            startActivity(intent);

        } else {//如果是第二次启动则直接跳转到主页面

            Intent intent=new Intent(this, Login.class);
            startActivity(intent);
        }
        ObjectBox.init(this);
        finish();
    }


}

