package com.example.text1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;
public class FruitAdapter1 extends ArrayAdapter<Fruit1> {

    private final int resourceId;

    public FruitAdapter1(Context context, int textViewResourceId,//第一个参数上下文对象，第二个参数，显示布局样式，第三个，数据来源
                        List<Fruit1> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        Fruit1 fruit = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.fruitImage = view.findViewById(R.id.fruit_image);
            viewHolder.fruitName = view.findViewById(R.id.name);
            viewHolder.explanation= view.findViewById(R.id.fruit_name);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.fruitImage.setImageBitmap(fruit.getImageId());
        viewHolder.fruitName.setText(fruit.getName());
        viewHolder.explanation.setText(fruit.getExplanation());
        return view;
    }
    class ViewHolder {
        ImageView fruitImage;
        TextView fruitName;
        TextView explanation;
    }
}