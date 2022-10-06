package com.example.text1.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.text1.AI;
import com.example.text1.About_UsActivity;
import com.example.text1.ItemView;
import com.example.text1.Login;
import com.example.text1.R;
import com.example.text1.Resetpwd;
import com.example.text1.databinding.FragmentNotificationsBinding;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SettingsFragment extends Fragment {


    private SettingsViewModel settingsViewModel;
    @Nullable
    private FragmentNotificationsBinding binding;
    private ImageView mHBack;
    private ImageView mHHead;
    private ImageView mUserLine;
    private TextView mUserName;
    private TextView mUserVal;
    private ItemView mNickName;
    private ItemView mContact;
    private ItemView mSignName;
    private ItemView mPass;
    private ItemView mPhone;
    private ItemView mVersions;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
        View root = inflater.inflate(R.layout.settings, container, false);

//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        initView(root);
        setData();

        return root;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        ImageButton button = (ImageButton) getActivity().findViewById(R.id.button2);
//        @Override
//        public void onClick (View v){
//            Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getActivity(), PhotoActivity.class);
//
//            startActivity(intent);
//        }
//    }


    public SettingsFragment() {
        // Required empty public constructor
    }

    private void setData() {

        //设置背景磨砂效果
        Glide.with(this).load(R.mipmap.logo)
                .apply(new RequestOptions().transform(new BlurTransformation(80, 10), new CenterCrop()))
                .into(mHBack);
        //设置圆形图像
        Glide.with(this).load(R.mipmap.logo)
                .apply(RequestOptions.circleCropTransform())
                .into(mHHead);

        //设置用户名整个item的点击事件
        mNickName.setItemClickListener(text -> {

            Intent intent1 = new Intent(getActivity(), Login.class);

            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.setClass(getActivity(),Login.class);
            startActivity(intent1);

        });
        mContact.setItemClickListener(text -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + "19859477544");
            intent.setData(data);
            startActivity(intent);

/*            File file= new File("")
            AI ai= new AI();*/
//            System.out.println("OK");
//            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        });
/*        mSignName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });*/
        mPass.setItemClickListener(text -> {

            Intent intent2 = new Intent(getActivity(), Resetpwd.class);
            startActivity(intent2);
        });
        mPhone.setItemClickListener(text -> {

            Intent intent3 = new Intent(getActivity(), About_UsActivity.class);

            startActivity(intent3);
        });
        mVersions.setItemClickListener(text -> Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show());
        //修改用户名item的左侧图标
      /* mNickName.setLeftIcon(R.drawable.ic_phone);
        //
        mNickName.setLeftTitle("修改后的用户名");
        mNickName.setRightDesc("名字修改");
        mNickName.setShowRightArrow(false);
        mNickName.setShowBottomLine(false);

        //设置用户名整个item的点击事件
        mNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 --注释掉检查 START (2021/9/13 0:42):
                Toast.makeText(getActivity(), "我是onclick事件显示的", Toast.LENGTH_SHORT).show();
            }
        });
*/

    }

    private void initView(View root) {
        //顶部头像控件
        mHBack = root.findViewById(R.id.h_back);
        mHHead = root.findViewById(R.id.h_head);
//        mUserLine = root.findViewById(R.id.user_line);
//        mUserName = root.findViewById(R.id.user_name);
//        mUserVal = root.findViewById(R.id.user_val);
        //下面item控件
        mNickName = root.findViewById(R.id.nickName);
        mContact = root.findViewById(R.id.contact);
//        mSignName = root.findViewById(R.id.signName);
        mPass = root.findViewById(R.id.pass);
        mPhone = root.findViewById(R.id.phone);
        mVersions = root.findViewById(R.id.about);
    }
//    @Override
//    public void onResume() {
//        int id=MainActivity.getIntent().getIntExtra("id", 0);
//        System.out.println(id);
//        if(id==2){
//            mViewPager.setCurrentItem(2);  //view2是viewPager中的第二个view，因此设置setCurrentItem（1）。
//        }
//        super.onResume();
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}