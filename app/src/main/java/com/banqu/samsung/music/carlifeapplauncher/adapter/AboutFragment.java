package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.banqu.samsung.music.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_about, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        Button update = v.findViewById(R.id.update_btn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://pan.baidu.com/s/1dcSRUyaK7LwVvEObSxWTPQ?pwd=8888"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

