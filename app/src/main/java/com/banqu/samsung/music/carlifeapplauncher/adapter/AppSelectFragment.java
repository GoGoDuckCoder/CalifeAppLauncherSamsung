package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banqu.samsung.music.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class AppSelectFragment extends Fragment {
    private RecyclerView fullapplistdrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_select, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle b) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
