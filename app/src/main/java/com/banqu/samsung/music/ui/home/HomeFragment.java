package com.banqu.samsung.music.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

//import com.banqu.samsung.databinding.FragmentHomeBinding;
import com.banqu.samsung.music.adapter.ActivityManager;
import com.banqu.samsung.music.databinding.FragmentHomeBinding;
import com.banqu.samsung.music.MainActivity;
import com.carlifeapplauncher.adapter.AppAnnouncement;
import com.carlifeapplauncher.adapter.Common;
import com.carlifeapplauncher.adapter.OpenProvider;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        if (OpenProvider.isSupported(requireContext())) {
        if (Common.isInstalled(requireContext(),"com.samsung.android.carlink")) {
            String versionName = Common.getCarlinkVersionName(getContext());
            binding.textView19.setText("车联服务：" + versionName);
            binding.userpanel.setVisibility(View.VISIBLE);
        } else {
            binding.textView19.setText("抱歉，您当前的系统环境不支持三星Carlife定制版与车联助手插件！请检查系统更新，并升级国行最新系统，外版可刷带车联服务的第三方国行移植固件。感谢您的支持");
            binding.userpanel.setVisibility(View.GONE);

        }

        binding.guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppAnnouncement.runGuide(requireContext());
            }
        });

        binding.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(requireContext(), MainActivity.class);
                startActivity(i);
            }
        });

        binding.reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().clearAll();
            }
        });
        binding.cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"请直接加群转账红包！谢谢",Toast.LENGTH_LONG).show();
            }
        });

        binding.updateView.setVisibility(View.GONE);
        AppAnnouncement.runUpdate(requireContext(), binding.updateView);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}