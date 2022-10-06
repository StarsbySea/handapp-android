package com.example.text1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class SplashActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout mIn_ll;
    private List<View> mViewList;
    private ImageView mLight_dots;
    private int mDistance;
    private ImageView mOne_dot;
    private ImageView mTwo_dot;
    private ImageView mThree_dot;
    private Button mBtn_next;

    private Box<Note> notesBox;
    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;

    private String getTxtFromAssets(String fileName) {
        String result = "";
        try {
            InputStream is = getAssets().open(fileName);
            int lenght = is.available();
            byte[]  buffer = new byte[lenght];
            is.read(buffer);
            result = new String(buffer, "utf8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();
        viewPager.setAdapter(new ViewPagerAdapter(mViewList));
        addDots();
        moveDots();
        viewPager.setPageTransformer(true, new DepthPageTransformer());


        //趁机初始化并导入数据
//        ObjectBox.init(this);
        notesBox = ObjectBox.get().boxFor(Note.class);
        // query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
        notesQuery = notesBox.query().order(Note_.name).build();
        SharedPreferences setting = getSharedPreferences("com.example.text1", 0);
        Boolean user_first = setting.getBoolean("DB_FIRST", true);
        if (user_first) {// 第一次则导入数据
            setting.edit().putBoolean("DB_FIRST", false).commit();
            Gson gson = new Gson();
            String jsonString = getTxtFromAssets("name_explain_img.json");

            ArrayList<Note> list = new ArrayList<Note>();
            Type listType = new TypeToken<List<Note>>() {
            }.getType();
            ArrayList<Note> user = gson.fromJson(jsonString, listType);
            notesBox.put(user);
        }
        //数据导入结束
    }

    /**
     * 移动小圆点
     */
    private void moveDots() {
        mLight_dots.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获得两个圆点之间的距离
                mDistance = mIn_ll.getChildAt(1).getLeft() - mIn_ll.getChildAt(0).getLeft();
                mLight_dots.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });
        // 监听事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
                float leftMargin = mDistance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLight_dots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mLight_dots.setLayoutParams(params);
                if (position == 2) {
                    mBtn_next.setVisibility(View.VISIBLE);
                }
                if (position != 2 && mBtn_next.getVisibility() == View.VISIBLE) {
                    mBtn_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //页面跳转时，设置小圆点的margin
                float leftMargin = mDistance * position;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLight_dots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mLight_dots.setLayoutParams(params);
                if (position == 2) {
                    mBtn_next.setVisibility(View.VISIBLE);
                }
                if (position != 2 && mBtn_next.getVisibility() == View.VISIBLE) {
                    mBtn_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 添加3个小圆点
     */
    private void addDots() {
        // 初始化圆点view
        mOne_dot = new ImageView(this);
        mOne_dot.setImageResource(R.drawable.gray_dot);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 40, 0);
        // 将圆点view加到LinearLayout中
        mIn_ll.addView(mOne_dot, layoutParams);

        mTwo_dot = new ImageView(this);
        mTwo_dot.setImageResource(R.drawable.gray_dot);
        mIn_ll.addView(mTwo_dot, layoutParams);

        mThree_dot = new ImageView(this);
        mThree_dot.setImageResource(R.drawable.gray_dot);
        mIn_ll.addView(mThree_dot, layoutParams);
//        setClickListener();
    }

// --注释掉检查 START (2021/9/13 0:42):
//    private void setClickListener() {
//        mOne_dot.setOnClickListener(view -> viewPager.setCurrentItem(0));
//        mTwo_dot.setOnClickListener(view -> viewPager.setCurrentItem(1));
//        mThree_dot.setOnClickListener(view -> viewPager.setCurrentItem(2));
//    }
// --注释掉检查 STOP (2021/9/13 0:42)


    private void initView() {
        viewPager = findViewById(R.id.in_viewpager);
        mIn_ll = findViewById(R.id.in_ll);
        mLight_dots = findViewById(R.id.iv_light_dots);
        mBtn_next = findViewById(R.id.bt_next);

        mBtn_next.setOnClickListener(v -> {
//            ToastUtils.showShort("欢迎进入登录页面");
            Intent intent = new Intent(SplashActivity.this, Login.class);
            startActivity(intent);
            finish();
        });
    }

    private void initData() {
        mViewList = new ArrayList<>();
        View view1 = LayoutInflater.from(this).inflate(R.layout.we_indicator1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.we_indicator2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.we_indicator3, null);
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
    }


}
