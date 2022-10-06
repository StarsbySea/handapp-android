package com.example.text1;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;


public class weixinFragment extends Fragment {



    public weixinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton imagebutton = getActivity().findViewById(R.id.imageButton);
        imagebutton.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
    }
}
