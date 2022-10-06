package com.example.text1;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;


public class ItemView extends LinearLayout {
    private boolean isShowBottomLine = true;
    private boolean isShowLeftIcon = true;
    private boolean isShowRightArrow = true;
    private final ImageView leftIcon;//列表左侧的图标
    private final TextView leftTitle;//左侧的标题
    private final TextView rightDesc;//右侧描述
    private final ImageView rightArrow;//右侧图标
    private final ImageView bottomLine;//底部下划线图标
    private final RelativeLayout rootView;//整体item的view




    public interface itemClickListener{
        void itemClick(String text);
    }
    public itemClickListener listener;


    public ItemView(@NonNull Context context) {
        this(context, null);
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //添加布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_layout, null);
        addView(view);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        //找到控件
        leftIcon = findViewById(R.id.left_icon);
        leftTitle = findViewById(R.id.left_title);
        rightDesc = findViewById(R.id.right_desc);
        rightArrow = findViewById(R.id.right_arrow);
        bottomLine = findViewById(R.id.bottom_line);
        rootView= findViewById(R.id.root_item);
        //设置控件属性
        isShowBottomLine = ta.getBoolean(R.styleable.ItemView_show_bottom_line, true);//得到是否显示底部下划线属性
        isShowLeftIcon = ta.getBoolean(R.styleable.ItemView_show_left_icon, true);//得到是否显示左侧图标属性标识
        isShowRightArrow = ta.getBoolean(R.styleable.ItemView_show_right_arrow, true);//得到是否显示右侧图标属性标识

        leftIcon.setBackground(ta.getDrawable(R.styleable.ItemView_left_icon));//设置左侧图标
        leftIcon.setVisibility(isShowLeftIcon ? View.VISIBLE : View.INVISIBLE);//设置左侧箭头图标是否显示

        leftTitle.setText(ta.getString(R.styleable.ItemView_left_text));//设置左侧标题文字
        rightDesc.setText(ta.getString(R.styleable.ItemView_right_text));//设置右侧文字描述

        rightArrow.setVisibility(isShowRightArrow ? View.VISIBLE : View.INVISIBLE);//设置右侧箭头图标是否显示
        bottomLine.setVisibility(isShowBottomLine ? View.VISIBLE : View.INVISIBLE);//设置底部图标是否显示



        //设置点击事件
        //给整个item设置点击事件
       rootView.setOnClickListener(v -> {
           System.out.println(rightDesc.getText().toString());
           listener.itemClick(rightDesc.getText().toString());
       });

        //给最右侧的小箭头设置点击事件
        rightArrow.setOnClickListener(v -> listener.itemClick(rightDesc.getText().toString()));

        ta.recycle();
    }



// --注释掉检查 START (2021/9/13 0:42):
//    //设置左侧图标
//    public void setLeftIcon(int value) {
////        Drawable drawable = ResourcesCompat.getDrawable(getResources(),value,null);
//        Drawable drawable = getResources().getDrawable(value);
// --注释掉检查 START (2021/9/13 0:42):
////        leftIcon.setBackground(drawable);
////    }
// --注释掉检查 START (2021/9/13 0:42):
////// --注释掉检查 STOP (2021/9/13 0:42)
////
////    //设置左侧标题文字
// --注释掉检查 START (2021/9/13 0:42):
//////    public void setLeftTitle(String value) {
////// --注释掉检查 STOP (2021/9/13 0:42)
//// --注释掉检查 STOP (2021/9/13 0:42)
//        leftTitle.setText(value);
//    }
//
//    //设置右侧描述文字
//// --注释掉检查 START (2021/9/13 0:42):
// --注释掉检查 STOP (2021/9/13 0:42)
//    public void setRightDesc(String value) {
//        rightDesc.setText(value);
//    }
//    //设置右侧箭头
//    public void setShowRightArrow(boolean value) {
//        rightArrow.setVisibility(value ? View.VISIBLE : View.INVISIBLE);//设置右侧箭头图标是否显示
// --注释掉检查 STOP (2021/9/13 0:42)
//    }

    //设置是否显示下画线
    public void setShowBottomLine(boolean value) {
        bottomLine.setVisibility(value ? View.VISIBLE : View.INVISIBLE);//设置右侧箭头图标是否显示
    }




    //向外暴漏接口
    public void setItemClickListener(itemClickListener listener){
        this.listener=listener;
    }

}
