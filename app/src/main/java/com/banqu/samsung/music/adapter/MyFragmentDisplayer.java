package com.banqu.samsung.music.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.banqu.samsung.music.R;
import com.banqu.samsung.music.carlifeapplauncher.adapter.NightMode;

public class MyFragmentDisplayer extends AppCompatActivity {

    public String className = "";
    public FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().add(this);
        NightMode.setCustomNightModeSetting(getWindow(),this);
        setContentView(R.layout.activity_fragment_displayer);

        init();//初始化
        getValue();//获取类名
        if(className!=null&&!className.equals("")){
            //由于广泛用了Fragment，因此，这里要用这个方法调用
            FragmentManager fm =  getSupportFragmentManager();
            // 开启Fragment事务
            FragmentTransaction transaction = fm.beginTransaction();
            try{
                Class<?> fragClass = Class.forName(className);//反射动态获取类
                Object obj = fragClass.newInstance();
                Fragment fragment = (Fragment)obj;//类型转换为Fragment
                //跳转
                transaction.replace(R.id.fragmentContainerView2, fragment);
                transaction.commit();
            }catch(Exception e){
//                Logger.error("##############", e, "class error");
            }
        }

        Toolbar tb = findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void init(){
//        初始化
        frameLayout = (FrameLayout)findViewById(R.id.fragmentContainerView2);
    }

    //获取传入值
    public void getValue(){
        Intent intent =getIntent();
        className =  intent.getExtras().getString("className");
    }

    @Override
    protected void onDestroy()
    {
        ActivityManager.getInstance().remove(this);
        super.onDestroy();
    }

}