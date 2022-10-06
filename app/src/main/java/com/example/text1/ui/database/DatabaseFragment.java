package com.example.text1.ui.database;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.text1.Activity0;
import com.example.text1.BaseName;
import com.example.text1.BaseNameAdapter;
import com.example.text1.R;
import com.example.text1.databinding.FragmentDashboardBinding;
//import com.example.text1.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.List;

public class DatabaseFragment extends Fragment {

    public ListView mListView;
    View root; //存储视图

   @Nullable

    BaseNameAdapter mAdapter1;

    List<BaseName> mList;

    //new的实例时候一定要写在onCreateView里面，不然效果会不一样
    Intent mIntent;
    private DatabaseViewModel databaseViewModel;

    @Nullable
    private FragmentDashboardBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        databaseViewModel =
                new ViewModelProvider(this).get(DatabaseViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

        root = inflater.inflate(R.layout.database_layout, container, false);
//        mListView = (android.widget.ListView) database_layout.findViewById(R.id.listView1);

//        final TextView textView = binding.textDashboard;
//        databaseViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        mListView = root.findViewById(R.id.listView1);

        return root;
    }

    public void onStart() {

        super.onStart();
//        initView();
        initData();
        initAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initView() {
//        View database_layout = View.inflate(R.layout.database_layout, R.layout.database_layout, null);
//        R.layout.database_layout.

//        mListView = (android.widget.ListView) database_layout.findViewById(R.id.listView1);
//        mListView = (android.widget.ListView) root.findViewById(R.id.listView1);
    }

    //添加bean类数据临时的先添加，再连数据库
    private void initData() {
        mList = new ArrayList<>();
        String[] str = new String[]{"部队编制", "财政贸易", "称谓", "成语虚词", "党政机关", "法律", "工业建筑", "工作",
                "国防公安", "教育", "科学", "空间", "矿物", "历史", "民族", "农牧副渔",
                 "人品德才", "日用品", "生物", "时间", "食品", "事情事态", "数量词", "数目", "数学", "体育"
                , "天体气象", "卫生", "文化", "武器装备", "物理", "心理", "性质性能", "姓氏", "衣物", "哲学", "职业", "植物",
                "洲洋国家名称", "字母","其他"};
        for (String s : str) {
            BaseName message = new BaseName(s);
            mList.add(message);
            //item的点击事件，里面可以设置跳转并传值

            mListView.setOnItemClickListener((adapterView, view, i, l) -> {
//                Toast.makeText(getActivity(), "进入" + str[i] + "库", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), Activity0.class);
                intent.putExtra("category_name", str[i]);
                startActivity(intent);
                /*
                switch (i) {
                    case 0:
                        Intent intent = new Intent(getActivity(), Activity0.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), Activity1.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(), Activity2.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getActivity(), Activity3.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getActivity(), Activity4.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(getActivity(), Activity5.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(getActivity(), Activity6.class);
                        startActivity(intent6);
                        break;
                    case 7:
                        Intent intent7 = new Intent(getActivity(), Activity7.class);
                        startActivity(intent7);
                        break;
                    case 8:
                        Intent intent8 = new Intent(getActivity(), Activity8.class);
                        startActivity(intent8);
                        break;
                    case 9:
                        Intent intent9 = new Intent(getActivity(), Activity9.class);
                        startActivity(intent9);
                        break;
                    case 10:
                        Intent intent10 = new Intent(getActivity(), Activity10.class);
                        startActivity(intent10);
                        break;
                    case 11:
                        Intent intent11 = new Intent(getActivity(), Activity11.class);
                        startActivity(intent11);
                        break;
                    case 12:
                        Intent intent12 = new Intent(getActivity(), Activity12.class);
                        startActivity(intent12);
                        break;
                    case 13:
                        Intent intent13 = new Intent(getActivity(), Activity13.class);
                        startActivity(intent13);
                        break;
                    case 14:
                        Intent intent14 = new Intent(getActivity(), Activity14.class);
                        startActivity(intent14);
                        break;
                    case 15:
                        Intent intent15 = new Intent(getActivity(), Activity15.class);
                        startActivity(intent15);
                        break;
                    case 16:
                        Intent intent16 = new Intent(getActivity(), Activity16.class);
                        startActivity(intent16);
                        break;
                    case 17:
                        Intent intent17 = new Intent(getActivity(), Activity17.class);
                        startActivity(intent17);
                        break;
                    case 18:
                        Intent intent18 = new Intent(getActivity(), Activity18.class);
                        startActivity(intent18);
                        break;
                    case 19:
                        Intent intent19 = new Intent(getActivity(), Activity19.class);
                        startActivity(intent19);
                        break;
                    case 20:
                        Intent intent20 = new Intent(getActivity(), Activity20.class);
                        startActivity(intent20);
                        break;
                    case 21:
                        Intent intent21 = new Intent(getActivity(), Activity21.class);
                        startActivity(intent21);
                        break;
                    case 22:
                        Intent intent22 = new Intent(getActivity(), Activity22.class);
                        startActivity(intent22);
                        break;
                    case 23:
                        Intent intent23 = new Intent(getActivity(), Activity23.class);
                        startActivity(intent23);
                        break;
                    case 24:
                        Intent intent24 = new Intent(getActivity(), Activity24.class);
                        startActivity(intent24);
                        break;
                    case 25:
                        Intent intent25 = new Intent(getActivity(), Activity25.class);
                        startActivity(intent25);
                        break;
                    case 26:
                        Intent intent26 = new Intent(getActivity(), Activity26.class);
                        startActivity(intent26);
                        break;
                    case 27:
                        Intent intent27 = new Intent(getActivity(), Activity27.class);
                        startActivity(intent27);
                        break;
                    case 28:
                        Intent intent28 = new Intent(getActivity(), Activity28.class);
                        startActivity(intent28);
                        break;
                    case 29:
                        Intent intent29 = new Intent(getActivity(), Activity29.class);
                        startActivity(intent29);
                        break;
                    case 30:
                        Intent intent30 = new Intent(getActivity(), Activity30.class);
                        startActivity(intent30);
                        break;
                    case 31:
                        Intent intent31 = new Intent(getActivity(), Activity31.class);
                        startActivity(intent31);
                        break;
                    case 32:
                        Intent intent32 = new Intent(getActivity(), Activity32.class);
                        startActivity(intent32);
                        break;
                    case 33:
                        Intent intent33 = new Intent(getActivity(), Activity33.class);
                        startActivity(intent33);
                        break;
                    case 34:
                        Intent intent34 = new Intent(getActivity(), Activity34.class);
                        startActivity(intent34);
                        break;
                    case 35:
                        Intent intent35 = new Intent(getActivity(), Activity35.class);
                        startActivity(intent35);
                        break;
                    case 36:
                        Intent intent36 = new Intent(getActivity(), Activity36.class);
                        startActivity(intent36);
                        break;
                    case 37:
                        Intent intent37 = new Intent(getActivity(), Activity37.class);
                        startActivity(intent37);
                        break;
                    case 38:
                        Intent intent38 = new Intent(getActivity(), Activity38.class);
                        startActivity(intent38);
                        break;
                    case 39:
                        Intent intent39 = new Intent(getActivity(), Activity39.class);
                        startActivity(intent39);
                        break;
                    case 40:
                        Intent intent40 = new Intent(getActivity(), Activity40.class);

                        startActivity(intent40);
                        break;*/




            });
        }
    }

    private void initAdapter() {
        //传两个参数过去 1、上下文 2、集合
        mAdapter1 = new BaseNameAdapter(getActivity(), mList);

        mListView.setAdapter(mAdapter1);

    }
}