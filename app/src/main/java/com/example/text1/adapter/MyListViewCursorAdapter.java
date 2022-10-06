package com.example.text1.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.text1.R;


public class MyListViewCursorAdapter extends CursorAdapter{
    public MyListViewCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        //获取view
        View view = View.inflate(context, R.layout.item_listview2, null);
        //寻找控件
        viewHolder.iv= view.findViewById(R.id.image);
        viewHolder.tv1= view.findViewById(R.id.explain);
        viewHolder.tv= view.findViewById(R.id.name);

        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(@NonNull View view, Context context, @NonNull Cursor cursor) {

        ViewHolder viewHolder= (ViewHolder) view.getTag();
        //从cursor中获取值
        String explain = cursor.getString(cursor.getColumnIndex("explanation"));
        System.out.println(explain);
        String name = cursor.getString(cursor.getColumnIndex("name"));
        //把数据设置到控件上面
        System.out.println(name);
        String image = cursor.getString(cursor.getColumnIndex("image"));
        System.out.println(image);
        byte[] decodedString= Base64.decode(image,Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
        viewHolder.iv.setImageBitmap(decodeByte);
        viewHolder.tv1.setText(explain);
        viewHolder.tv.setText(name);


    }

    class ViewHolder{
        TextView tv;
        TextView tv1;
        ImageView iv;
    }
}
