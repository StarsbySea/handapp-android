package com.example.text1;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.objectbox.Box;
import io.objectbox.query.Query;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity0 extends AppCompatActivity {
    private Box<Note> notesBox;
    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;
/*
    //    手势数组
    private final List<com.example.text1.Fruit1> fruitList = new ArrayList<>();
    //    自动填充文本框数据源
    @NonNull
    final
    List<String> list1=new ArrayList<>();
    @NonNull
    final
    List<String> list2=new ArrayList<>();
    @NonNull
    final
    List<Bitmap> list3=new ArrayList<>();
    @NonNull
    final
    List<String> list4=new ArrayList<>();
*/
private void updateNotes(List<Note> notes) {

    notesAdapter.setNotes(notes);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fruitpage);

        notesBox = ObjectBox.get().boxFor(Note.class);
        // query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
        notesQuery = notesBox.query().order(Note_.name).build();
        ListView listView = findViewById(R.id.listViewHand);
        notesAdapter = new NotesAdapter();
        listView.setAdapter(notesAdapter);

        //获取当前的分类名
        Intent intent = getIntent();
        String category_name = intent.getStringExtra("category_name");

        setTitle(category_name);

        List<Note> all_result = new LinkedList<>();
        Query<Note> query = notesBox.query(Note_.category.equal(category_name)).build();
//        Query<Note> query = notesBox.query().equal(Note_.category, category_name).build();
        all_result.addAll(query.find());
        updateNotes(all_result);
        //开启返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//            text = text + ","+word;
    }
//        ToastUtils.showToast(context,text);
//
//        Query<Note> query =notesBox.query().equal(Note_.name, "课程").build();
//        List<Note> result =query.find();
//        ToastUtils.showToast(context,result.get(0).explain);

/*        Thread t1=new Thread(() -> {
            try{
                //创建OkHttpClien对象
                OkHttpClient client = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(3, TimeUnit.SECONDS)
                        .build();


                //创建请求对象
                Request request = new Request.Builder()

                        .url("http://192.168.43.148:8000/hand/education_show")
                        .get()
                        .build();
                //提交请求
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                JSONArray jsonArray = new JSONArray(responseData);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("id",""+jsonObject.getInt("id"));
                    Log.d("title",jsonObject.getString("title"));
                    Log.d("explanation",jsonObject.getString("explanation"));
                    Log.d("img_add",jsonObject.getString("img"));
                    Log.d("img_add1", String.valueOf(jsonObject.put("img1","sc")));

                    byte[] decodedString= Base64.decode(jsonObject.getString("img"),Base64.DEFAULT);
                    Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                    list1.add(jsonObject.getString("title"));
                    list2.add(jsonObject.getString("explanation"));
                    list3.add(decodeByte);
                    list4.add(jsonObject.getString("img1"));
                    System.out.println(jsonObject.getString("img1"));
                }
                System.out.println(list1);
                System.out.println(list2);
                System.out.println(list3);
                System.out.println(list1.size());
                runOnUiThread(() -> Toast.makeText(Activity0.this, "发送成功!", Toast.LENGTH_SHORT).show());
            }catch (Exception e){
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Activity0.this, "网络连接失败!", Toast.LENGTH_SHORT).show());

            }
        });

        try {
            t1.start();
            t1.join();
        }catch (InterruptedException ex)
        {
            System.out.println("出现异常");
        }

        super.onCreate(savedInstanceState);

//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        自动填充文本框相关设置
        AutoCompleteTextView autotext = findViewById(R.id.auto_complete);
        ArrayAdapter textadapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list1);
        autotext.setAdapter(textadapter);
//        初始化手势数据
        System.out.println("********************************************************************************");
        initFruits();
//        ListView相关设置
        com.example.text1.FruitAdapter1 adapter = new com.example.text1.FruitAdapter1(Activity0.this, R.layout.fruit_item, fruitList);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
//        点击显示手势名称
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Fruit1 fruit1 = fruitList.get(position);
            Toast.makeText(Activity0.this, fruit1.getName(), Toast.LENGTH_SHORT).show();
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

    //    手势列表初始化函数定义
/*    private void initFruits() {
        System.out.println("***********************");
        System.out.println(list1.size());
        for(int i=0;i<list1.size();i++){

            com.example.text1.Fruit1 apple = new com.example.text1.Fruit1(list1.get(i), list3.get(i),list2.get(i));
            fruitList.add(apple);
        }



    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮逻辑
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

