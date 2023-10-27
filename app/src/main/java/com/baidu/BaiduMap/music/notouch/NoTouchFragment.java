package com.baidu.BaiduMap.music.notouch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.baidu.BaiduMap.music.R;
import com.baidu.BaiduMap.music.MyFragmentDisplayer;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FakeStart;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FavoFragment;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.OpenProvider;
import com.baidu.BaiduMap.music.carlifeapplauncher.apps.AppsUI;

public class NoTouchFragment extends Fragment {

    ViewGroup location_vg;

    public NoTouchFragment() {

    }

    public static NoTouchFragment newInstance(String param1, String param2) {
        NoTouchFragment fragment = new NoTouchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_no_touch_ui, container, false);
    }

    private void reset_app_layout() {
        if (location_vg == null) {
            location_vg = getView().findViewById(R.id.remoteholder);
        }
        location_vg.removeAllViews();
        AppsUI appsUI = new AppsUI(getContext(), getLayoutInflater(), location_vg, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        reset_app_layout();


        Button start_mirroring = (Button) getView().findViewById(R.id.btn_start_mirroring);
        start_mirroring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OpenProvider.isConnected(getContext())) {
                    //open mirror ui on car
                    FakeStart.StartUsingDeepLink(getContext(), "com.banqu.samsung.music", "mirror");
                    //open permission ui
                } else {
                    Toast.makeText(getContext(), "未链接车机", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button add_app = (Button) getView().findViewById(R.id.add_apps);
        add_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", FavoFragment.class.getName());
                startActivity(i);
            }
        });

        Switch jumpswitch = (Switch) getView().findViewById(R.id.jumpnotouch);
        boolean jumpswitch_value = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("jumpnotouch", false);
        jumpswitch.setChecked(jumpswitch_value);
        jumpswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(getContext(),"asd"+b,Toast.LENGTH_LONG).show();
                SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                ed.putBoolean("jumpnotouch",b);
                ed.apply();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        reset_app_layout();
    }
}
