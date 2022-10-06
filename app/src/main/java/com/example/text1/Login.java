package com.example.text1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Login extends Activity {                 //登录界面活动

    public int pwdresetFlag=0;
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                   //注册按钮
    private Button mLoginButton;                      //登录按钮
    private Button mCancleButton;                     //注销按钮
    private CheckBox mRememberCheck;

private ImageView mImageView;

private SharedPreferences login_sp;

    private String userNameValue,passwordValue;

    private View loginView;                           //登录
    private View loginSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
    @Nullable
    private UserDataManager mUserDataManager;         //用户数据管理类


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //通过id找到相应的控件
        mImageView = findViewById(R.id.logo);
//        mImageView.getBackground().setAlpha(10);
        mAccount = findViewById(R.id.login_edit_account);
        mPwd = findViewById(R.id.login_edit_pwd);
        mRegisterButton = findViewById(R.id.login_btn_register);
//        mRegisterButton.getBackground().setAlpha(30);
        mLoginButton = findViewById(R.id.login_btn_login);
//        mLoginButton.getBackground().setAlpha(150);
        mCancleButton = findViewById(R.id.login_btn_cancle);
        loginView=findViewById(R.id.login_view);
//        loginSuccessView=findViewById(R.id.login_success_view);
//        loginSuccessShow= findViewById(R.id.login_success_show);

        mChangepwdText = findViewById(R.id.login_text_change_pwd);

        mRememberCheck = findViewById(R.id.Login_Remember);

        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }
         //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mRegisterButton.setOnClickListener(mListener);    // 设置监听事件的注册按钮
        mLoginButton.setOnClickListener(mListener);  //设置监听事件的登录按钮
        mCancleButton.setOnClickListener(mListener);   //设置监听事件的注销按钮
        mChangepwdText.setOnClickListener(mListener);  //设置监听事件的修改密码按钮

        ImageView image = findViewById(R.id.logo);             //使用ImageView显示logo
        image.setImageResource(R.drawable.logo1);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }
    //不同按钮按下的监听事件选择
    @NonNull
    final
    OnClickListener mListener = v -> {
        switch (v.getId()) {
            case R.id.login_btn_register:                            //登录界面的注册按钮
                Intent intent_Login_to_Register = new Intent(Login.this,Register.class) ;    //切换Login Activity至User Activity
                startActivity(intent_Login_to_Register);
                break;
            case R.id.login_btn_login:                              //登录界面的登录按钮
                login();
                break;
            case R.id.login_btn_cancle:                             //登录界面的注销按钮
                cancel();
                break;
            case R.id.login_text_change_pwd:                             //登录界面的修改密码按钮
                Intent intent_Login_to_reset = new Intent(Login.this,Resetpwd.class) ;    //切换Login Activity至User Activity
                startActivity(intent_Login_to_reset);
                break;
        }
    };

    public void login() {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            SharedPreferences.Editor editor =login_sp.edit();
            int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if(result==1){                                             //返回1说明用户名和密码均正确
                //保存用户名和密码
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", userPwd);

                //是否记住密码
                editor.putBoolean("mRememberCheck", mRememberCheck.isChecked());
                editor.commit();//提交操作

                Intent intent = new Intent(com.example.text1.Login.this, MainActivity.class) ;    //切换Login Activity至User Activity
                startActivity(intent);
                finish();
//                Toast.makeText(this, getString(R.string.login_success),Toast.LENGTH_SHORT).show();//登录成功提示
            }else if(result==0){
                Toast.makeText(this, getString(R.string.login_fail),Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }
    }
    public void cancel() {           //注销
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if(result==1){                                             //返回1说明用户名和密码均正确
//              Intent intent = new Intent(Login.this,User.class) ;    //切换Login Activity至User Activity
//              startActivity(intent);//为什么这里不运行 不跳转
                Toast.makeText(this, getString(R.string.cancel_success),Toast.LENGTH_SHORT).show();//注销成功提示
                mPwd.setText("");
                mAccount.setText("");
                mUserDataManager.deleteUserDatabyname(userName);
            }else if(result==0){
                Toast.makeText(this, getString(R.string.cancel_fail),Toast.LENGTH_SHORT).show();  //注销失败提示
            }
        }

    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }


}
