package com.example.text1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MyOpenHelper extends SQLiteOpenHelper{
    public MyOpenHelper(Context context) {
        super(context, "lol13.db", null, 1);
    }
    @NonNull
    final
    List<String> list1=new ArrayList<>();
    @NonNull
    final
    List<String> list2=new ArrayList<>();
    @NonNull
    final
    List<String> list3=new ArrayList<>();
    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        Thread t1=new Thread(() -> {
            try{
                //创建OkHttpClien对象
                OkHttpClient client = new OkHttpClient();
                //创建请求对象
                Request request = new Request.Builder()
                        .url("http://192.168.155.29:8000/hand/education_show")
                        .get()
                        .build();
                //提交请求

                Response response = client.newCall(request).execute();
//                    System.out.println(response.body().string());
                String responseData = response.body().string();
                JSONArray jsonArray = new JSONArray(responseData);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("id",""+jsonObject.getInt("id"));
                    Log.d("title",jsonObject.getString("title"));
                    Log.d("explanation",jsonObject.getString("explanation"));
                    Log.d("img_add",jsonObject.getString("img"));
//                        byte[] decodedString= Base64.decode(jsonObject.getString("img"),Base64.DEFAULT);
//                        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                    list1.add(jsonObject.getString("title"));
                    list2.add(jsonObject.getString("explanation"));
                    list3.add(jsonObject.getString("img"));
//                        System.out.println(decodedString);
                }
                System.out.println(list1);
                System.out.println(list2);
                System.out.println(list3);
                System.out.println(list1.size());

            }catch (@NonNull IOException | JSONException ex)
            {   ex.printStackTrace();
                System.out.println("***************出现异常***************");
            }
        });

        try {
            t1.start();
            t1.join();
        }catch (InterruptedException ex)
        {
            System.out.println("$$$$$$$$$$$$$出现异常$$$$$$$$$$$$$$$$$");
        }


        db.execSQL("create table if not exists lol13(_id integer primary key autoincrement,name varchar(20),explanation varchar(100),image vachar(10000000))");
//        db.execSQL(" ALTER TABLE lol4 ADD explanation VARCHAR(20) ");
        for (int x=0;x<list1.size();x++){
            db.execSQL("insert into lol13(name,explanation,image) values(?,?,?)",new String[]{list1.get(x),list2.get(x),list3.get(x)});

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
