package com.example.text1.ui.learn;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.text1.NoteActivity;
import com.example.text1.R;
import com.example.text1.Word;
import com.example.text1.WordTest;
import com.example.text1.databinding.FragmentNotificationsBinding;
import com.example.text1.rg.CameraParam;
import com.example.text1.rg.Tools;
import com.permissionx.guolindev.PermissionX;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LearnFragment extends Fragment {

    private static final String TAG = null;
    private LearnViewModel learnViewModel;
    @Nullable
    private FragmentNotificationsBinding binding;
    private TextView tv_camera;
    private ImageView img_picture;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        learnViewModel =
                new ViewModelProvider(this).get(LearnViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
        View root = inflater.inflate(R.layout.training_grounds, container, false);
//        Word word = DataSupport.findFirst(Word.class);
//        if (word == null) {
//
//            Log.d(TAG, "出错了");
//        }

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton button = getActivity().findViewById(R.id.button2);
        ImageButton button1 = getActivity().findViewById(R.id.imageButton8);
        button.setOnClickListener(v -> {
//            Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
            CreateCamera();
/*            Intent intent=new Intent(getActivity(), PhotoActivity.class);
            startActivity(intent);*/
        });
        button1.setOnClickListener(v -> {
            final int maxNum = 5;

            Intent intent1 = new Intent(getActivity(), WordTest.class);
            intent1.putExtra("maxNum", maxNum);
            startActivity(intent1);
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void CreateCamera() {
        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA)
                .request((boolean allGranted, List<String> grantedList, List<String> deniedList) -> {
                    if (allGranted) {
                        CameraParam mCameraParam = new CameraParam.Builder(getContext())
                                .setActivity(getActivity())
                                .setFront(true)
                                .setShowSwitch(true)
                                .setMaskMarginTop(Tools.dp2px(getContext(), 200))
                                .setMaskRatioW(8)//下面一行和当前行表示的是裁剪框的宽高比
                                .setMaskRatioH(5)
                                .build();
                    } else {

                    }
                });

    }
}


