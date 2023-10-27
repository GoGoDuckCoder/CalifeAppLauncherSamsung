package com.baidu.BaiduMap.music.carlifeapplauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.BaiduMap.music.R;
import com.baidu.BaiduMap.music.carlifeapplauncher.NotificationListener;
import com.baidu.BaiduMap.music.databinding.MsgboxDrawerBinding;

import java.util.ArrayList;

public class MsgBoxUI {
    private Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView msgdrawer;
    private ImageView clear;
    private MsgBoxAdaptor msgBoxAdaptor;
    //    private AppsBinding binding;
    private MsgboxDrawerBinding binding;

    public static MsgBoxUI msgBoxUI;

    public MsgBoxUI(Context context, LayoutInflater layoutInflater, ViewGroup root) {
        this.context = context;
        this.layoutInflater = layoutInflater;

        binding = MsgboxDrawerBinding.inflate(layoutInflater);
        root.addView(binding.getRoot());
//        binding.msgboxDrawer.setBackgroundColor(Color.RED);
        msgdrawer = binding.msgboxDrawer;
        clear = binding.clear;

        msgBoxAdaptor = new MsgBoxAdaptor(context);
        msgdrawer.setAdapter(msgBoxAdaptor);
        msgdrawer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        msgBoxUI = this;

        NotificationFactory.update_msgbox();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationFactory.clear();
                ArrayList<Msg> default_list =  new ArrayList<Msg>();
                Msg a = new Msg();
                a.label = "车联助手";
                a.title = "暂无信息";
                a.text = "请注意安全驾驶！";
                a.icon = context.getDrawable(R.mipmap.ic_launcher_round);
                default_list.add(a);
                msgBoxAdaptor.update(default_list);
            }
        });

    }

    public static MsgBoxUI isEnable() {
        return msgBoxUI;
    }

    public void update(ArrayList<Msg> msgs) {
        msgBoxAdaptor.update(msgs);
    }

    public void onDestroy() {
        msgBoxUI = null;
    }
}
