package com.example.text1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class WordTest extends AppCompatActivity {
    private Box<Note> notesBox;
    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;

    private TextView option1,option2,option3,option4,maxTestNum,passed,previous;
    private ImageView quest;
    //    四个选项中的Word类
    private Note A,B,C,D;
    //    四个选项在数据库中的id数组
    int[] num =new int[]{0, 0, 0, 0};
    //    已通过的单词数量
    int passedNum = 0;
    int hasTestNum = 0;
    int maxNum = 0;
    //    错误单词的列表
    public Queue<String> wrongWords = new LinkedList<>();

    //    Word temp = DataSupport.findLast(Word.class);
    private int maxSize = 0;

    //    private int maxSize = temp.getId(),
    private        int answer = 0;
    public Random random = new Random();

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
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
//        ObjectBox.init(this);
        notesBox = ObjectBox.get().boxFor(Note.class);

        // query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
        notesQuery = notesBox.query().order(Note_.name).build();
        /*SharedPreferences setting = getSharedPreferences("com.example.text1", 0);
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
        }*/

        maxSize = Math.toIntExact(notesBox.count());

        setContentView(R.layout.activity_word_test);

        initComponent();
        Intent intent = getIntent();
        maxNum = intent.getIntExtra("maxNum",0);
        maxTestNum.setText("总词数: "+ maxNum);
        passed.setText("已通过: "+ passedNum);

        getOptions();
        setClick();
        //启用返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
/*        notesAdapter = new NotesAdapter();
        listView.setAdapter(notesAdapter);
        updateNotes();*/
    }
    /*    private void updateNotes() {
            List<Note> notes = notesQuery.find();
            notesAdapter.setNotes(notes);
        }*/
    private void initComponent(){
        option1 = findViewById(R.id.optionA);
        option2 = findViewById(R.id.optionB);
        option3 = findViewById(R.id.optionC);
        option4 = findViewById(R.id.optionD);
        quest = findViewById(R.id.question);
        maxTestNum = findViewById(R.id.under_tested);
        passed = findViewById(R.id.has_passed);
        previous = findViewById(R.id.previous);
    }

    //    获得每次的四个选项
    private void getOptions(){
        String temp;
//        由随机数来决定下一组单词是没做过的还是做错过的,当错题队列的长度达到10时,下一个必为错题.
        System.out.println((Math.pow(random.nextInt(10),2)) < Math.pow(wrongWords.size(),2));
        if ((Math.pow(random.nextInt(10),2)) < Math.pow(wrongWords.size(),2) || hasTestNum == maxNum){
            System.out.println(wrongWords);
            String[] list = wrongWords.poll().split(",");
            for (int i=0;i<list.length-1;i++){
                num[i] = Integer.parseInt(list[i]);
            }
            answer = Integer.parseInt(list[list.length-1]);
//            Toast.makeText(this,"这次选的是错题",Toast.LENGTH_LONG).show();
        }else {
            for (int i=0;i<4;i++){
                int t = random.nextInt(maxSize) + 1;
                temp = null;
                while (isInArray(num,t) || temp == null){
                    t = random.nextInt(maxSize) + 1;
                    try {
                        temp = notesBox.get(t).getExplain();
                    } catch (NullPointerException e) {
                        temp = null;
                    }
                }
                num[i] = t;
            }
            answer = random.nextInt(4);
            hasTestNum++;
        }

        quest.setImageDrawable(notesBox.get(num[answer]).getImage(false)); //设置正确答案

        A = notesBox.get(num[0]);
        option1.setText(A.getName());
        B = notesBox.get(num[1]);
        option2.setText(B.getName());
        C = notesBox.get(num[2]);
        option3.setText(C.getName());
        D = notesBox.get(num[3]);
        option4.setText(D.getName());
    }

/*
//    通过getOption获得的随机数组从数据库取得相应的Word对象
    public Word getDBData(int i){
        Word word = DataSupport.find(Word.class,i);
        return word;
    }
*/

    //    判断随机的序号是否已在选项中出现
    private boolean isInArray(int[] array,int num){
        for (int value : array) {
            if (value == num) {
                return true;
            }
        }
        return false;
    }

    //    判断用户所选择的选项是否为正确答案
    private boolean isQuest(Note word){
        return word.getId() == num[answer];
    }

    //    刷新背景设置,并跳转到下一题
    private void setBackground(final View view, final boolean state){
        if (state){
            view.setBackground(ContextCompat.getDrawable(this,R.drawable.right_style));

        }else{
            view.setBackground(ContextCompat.getDrawable(this,R.drawable.wrong_style));
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.answer_style));
                if (!state){
                    wrongWords.offer(addWrongProblem());
//                    WrWord word = new WrWord();
//                    String[] words = wrongWords.poll().split(",");
//                    word.setWword(words[0]);
//                    word.setInterpretation(words[1]);
//                    word.save();//保存数据到数据库中
//                    Log.d(TAG,words.toString());
                }else {
                    passedNum++;
                    passed.setText("已通过: " + (passedNum));
                }
                String test = notesBox.get(num[answer]).getName();
                previous.setText(notesBox.get(num[answer]).getName());
                finishTest(view);
            }
        }, 1000);
    }

    //    单词测试完后触发
    private void finishTest(View view){
        if (passedNum == maxNum){
            AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
            dialog.setTitle("测试完啦");
            dialog.setTitle("恭喜完成今日目标，是否加量学习");
            dialog.setPositiveButton("加量学习", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    maxNum += 20;
                    maxTestNum.setText("总词数: "+ maxNum);
                    getOptions();
                }
            });
            dialog.setNegativeButton("返回主界面", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.show();
        }else {
            getOptions();
        }
    }

    //    设置点击事件的监听器
    private void setClick(){
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBackground(v,isQuest(A));
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackground(v,isQuest(B));
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackground(v,isQuest(C));
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackground(v,isQuest(D));
            }
        });
    }

    //    将错误单词记录在一个ArrayList中
    private String addWrongProblem(){
        String temp = "";
        for (int i : num){
            temp = temp + i +",";
        }
        temp = temp + answer;

        return temp;
    }

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