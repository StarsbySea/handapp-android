package com.example.text1.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.text1.NoteActivity;
import com.example.text1.R;
import com.example.text1.SearchActivity;
import com.example.text1.databinding.FragmentHomeBinding;

public class SearchFragment extends Fragment {

private SearchViewModel searchViewModel;

   @Nullable
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
        View root = inflater.inflate(R.layout.search, container, false);
/*
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton imagebutton = getActivity().findViewById(R.id.imageButton);
        imagebutton.setOnClickListener(v -> {
//            Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}