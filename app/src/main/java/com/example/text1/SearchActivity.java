package com.example.text1;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.text1.db.MyOpenHelper;
import com.example.text1.utils.ToastUtils;
import com.github.houbb.segment.bs.SegmentBs;
import com.github.houbb.segment.support.segment.mode.impl.SegmentModes;
import com.github.houbb.segment.support.segment.result.impl.SegmentResultHandlers;

import java.util.LinkedList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class SearchActivity extends Activity {
    Context context;
    Cursor cursor;
    private Box<Note> notesBox;
    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;
    private EditText mEditText;
    private ImageView mImageView;
    //    private ListView mListView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_inside);
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        initView();

        //开启数据库
        notesBox = ObjectBox.get().boxFor(Note.class);
        // query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
        notesQuery = notesBox.query().order(Note_.name).build();

    }

    private void initView() {
        mTextView = findViewById(R.id.textview);
        mEditText = findViewById(R.id.edittext);
        mImageView = findViewById(R.id.imageview);
//        mListView = findViewById(R.id.listview);


        //设置删除图片的点击事件
/*        mImageView.setOnClickListener(v -> {
            //把EditText内容设置为空
            mEditText.setText("");
            //把ListView隐藏
            mListView.setVisibility(View.GONE);
        });*/

        //EditText添加监听
        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                showListView();
                return true;
            }
            return false;
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            //文本改变的时候执行
            public void onTextChanged(@NonNull CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                mTextView.setEnabled(enable);
            }

            @Override
            //文本改变之前执行
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            //文本改变之后执行
            public void afterTextChanged(Editable s) {
            }
        });

        mTextView.setOnClickListener(v -> {
            //如果输入框内容为空，提示请输入搜索内容

            if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
//                ToastUtils.showToast(context, "请输入您要搜索的内容");
            } else {
//                ToastUtils.showToast(context, "点击了搜索");
                showListView();
                /*//判断cursor是否为空

                if (cursor != null) {
                    int columnCount = cursor.getCount();
                    if (columnCount == 0) {
                        ToastUtils.showToast(context, "对不起，没有你要搜索的内容");
                    }
                }*/
            }

        });
        ListView listView = findViewById(R.id.listViewHands);
        notesAdapter = new NotesAdapter();
        listView.setAdapter(notesAdapter);
    }

    private List<String> jieba(String text) {
        //中文分词 返回List<String>
        List<String> resultList = SegmentBs.newInstance()
                .segmentMode(SegmentModes.dict())
                .segment(text, SegmentResultHandlers.word());
        return resultList;
//        return SegmentHelper.segment(text, SegmentResultHandlers.word());
    }

    private Cursor searchWords(@NonNull List<String> words) {
        //数据库初始化

        MyOpenHelper myOpenHelper = new MyOpenHelper(getApplicationContext());
        SQLiteDatabase db = myOpenHelper.getReadableDatabase();
        String sql_word = "";
        for (String word : words) {
            if (word.trim().isEmpty() == false) {
                sql_word = sql_word + "name='" + word + "'OR ";
            }

        }
        sql_word = sql_word.substring(0, sql_word.length() - 3);
//        words.forEach(String word -> sql_word + word.toString() + ',');
        //"OR title='zaijian'"
        //创建数据库查询
        System.out.println("select * from lol13 where " + sql_word + ";");
//        Cursor test=db.rawQuery("select * from lol14",null);
//        test.moveToFirst();
//        System.out.println(test.getString(1));
        String sql_query = "select * from lol13 where " + sql_word + ";";
        cursor = db.rawQuery(sql_query, null);
        //返回SQL游标

        return cursor;
    }

    private void updateNotes(List<Note> notes) {

        notesAdapter.setNotes(notes);
    }

    private void showListView() {
        //开始搜索
//        ToastUtils.showToast(context, "开始搜索");

//        mListView.setVisibility(View.VISIBLE);
        //获得输入的内容
        String str = mEditText.getText().toString().trim();

        //对句子进行分词
        List<String> cut_text = jieba(str);
        System.out.println(cut_text.toString());
        String text = "";
        List<Note> all_result = new LinkedList<>();
        for (String word : cut_text) {
            Query<Note> query = notesBox.query(Note_.name.equal(word)).build();
//            Query<Note> query = notesBox.query().equal(Note_.name, word).build();
            List<Note> current_result = query.find();
            if (current_result.size() == 0) {

                String[] str_arr = word.split("");

                for (String ch : str_arr) {
                    System.out.print(ch);
                    query = notesBox.query(Note_.name.equal(ch)).build();
                    current_result.addAll(query.find());
                }

            }
            all_result.addAll(current_result);
        }

        updateNotes(all_result);
    }
}