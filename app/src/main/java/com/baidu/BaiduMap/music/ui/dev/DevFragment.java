package com.baidu.BaiduMap.music.ui.dev;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.baidu.BaiduMap.music.databinding.FragmentDevBinding;

public class DevFragment extends Fragment {

    private FragmentDevBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentDevBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        boolean log_enable = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("log", false);
        if (log_enable) {
            binding.devDebug.setText("点击关闭Log");
        } else {
            binding.devDebug.setText("点击开启Log");
        }

        binding.devDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit();

                if (PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("log", false)) {
                    ed.putBoolean("log", false);
                    ed.apply();
                    binding.devDebug.setText("点击开启Log");
                } else {
                    ed.putBoolean("log", true);
                    ed.apply();
                    binding.devDebug.setText("点击关闭Log");
                }
            }
        });

        binding.devRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}