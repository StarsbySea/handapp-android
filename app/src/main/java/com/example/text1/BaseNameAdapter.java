package com.example.text1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class BaseNameAdapter extends BaseAdapter {
    final List<BaseName> mList;
    final Context mContext;

    public BaseNameAdapter(Context context, List<BaseName> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    ViewHolder mHolder;

    @Nullable
    @Override
    public View getView(final int position, @Nullable View view, ViewGroup viewGroup) {
        mHolder = new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem1, null);
            mHolder.tv1 = view.findViewById(R.id.TotalName);
            mHolder.ib1 = view.findViewById(R.id.imageButton5);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        //如果没有这些内容，将会显示布局文件中的内容
        mHolder.tv1.setText(mList.get(position).getName());
        //头像的点击事件并传值

        return view;
    }

    class ViewHolder {
        TextView tv1;
        ImageView ib1;
    }
}
