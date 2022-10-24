package com.banqu.samsung.music.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.banqu.samsung.music.HomeActivity;
import com.banqu.samsung.music.adapter.MyFragmentDisplayer;
import com.banqu.samsung.music.R;
import com.banqu.samsung.music.SettingsActivity;
import com.carlifeapplauncher.adapter.Common;
import com.carlifeapplauncher.adapter.NightMode;

import java.util.ArrayList;
import java.util.List;

public class IntroducttoryActivity extends AppCompatActivity {

    private ViewPager mViewPage;
    private List<View> viewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NightMode.setCustomNightModeSetting(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introducttory);

        initView();
        initAdapter();
        initStart();

    }

    /**
     * viewPager和4个引导
     */
    private void initView() {
        mViewPage=findViewById(R.id.introductory_viewPager);
        viewList=new ArrayList<>();
        View pageA = getView(R.layout.introductory_a);
        viewList.add(pageA);

        View pageB = getView(R.layout.introductory_b);
        viewList.add(pageB);

        View pageC = getView(R.layout.introductory_c);
        viewList.add(pageC);

        View pageE = getView(R.layout.introductory_e);
        viewList.add(pageE);

        View pageD = getView(R.layout.introductory_d);
        viewList.add(pageD);



//        viewList.add(getView(R.layout.introductory_d));


        Button  app = pageB.findViewById(R.id.setup_app);
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"请添加应用!完成后返回",Toast.LENGTH_LONG).show();
                Intent i = new Intent();
                i.setClassName(getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsAppDrawerFragment.class.getName());
                startActivity(i);
            }
        });

        Button music = pageC.findViewById(R.id.setup_music);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"请开启音乐镜像服务并打开权限！完成后返回",Toast.LENGTH_LONG).show();
                Intent i = new Intent();
                i.setClassName(getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsMusicFragment.class.getName());
                startActivity(i);
            }
        });

        Button done = pageD.findViewById(R.id.setup_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Common.isIgnoringBatteryOptimizations(getApplicationContext()))
                {
                    Common.requestIgnoreBatteryOptimizations(getApplicationContext());
                }
                else
                {
                    SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    editor.putBoolean("FirstStartFlag",false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"设置完成！请在Carlife应用管理中添加车联助手以在车机使用助手！",Toast.LENGTH_LONG).show();
                    Intent i = new Intent();
                    i.setClassName(getPackageName(), HomeActivity.class.getName());
                    startActivity(i);
                }


            }
        });
    }

    /**
     * 适配器
     */
    private void initAdapter() {
        IntroductoryAdapter adapter =new IntroductoryAdapter(viewList);
        mViewPage.setAdapter(adapter);
    }



    private View getView(int resId) {
        return LayoutInflater.from(this).inflate(resId,null);
    }

    private void initStart() {
//        mButton=viewList.get(3).findViewById(R.id.btn_open);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(IntroductoryActivity.this,MainActivity.class));
//                IntroductoryActivity.this.finish();
//            }
//        });
    }

}