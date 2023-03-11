package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.banqu.samsung.music.MainActivity;
import com.banqu.samsung.music.databinding.AppsBinding;
import com.banqu.samsung.music.databinding.MsgboxDrawerBinding;

import java.util.ArrayList;

public class MsgBoxUI {
    private Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView msgdrawer;
    private MsgBoxAdaptor msgBoxAdaptor;
    //    private AppsBinding binding;
    private MsgboxDrawerBinding binding;

    public  static  MsgBoxUI msgBoxUI;
    public MsgBoxUI(Context context, LayoutInflater layoutInflater, ViewGroup root) {
        this.context = context;
        this.layoutInflater = layoutInflater;

        binding = MsgboxDrawerBinding.inflate(layoutInflater);
        root.addView(binding.getRoot());
//        binding.msgboxDrawer.setBackgroundColor(Color.RED);
        msgdrawer = binding.msgboxDrawer;

        msgBoxAdaptor = new MsgBoxAdaptor(context);
        msgdrawer.setAdapter(msgBoxAdaptor);
        msgdrawer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        msgBoxUI = this;
    }
    public static MsgBoxUI isEnable()
    {
        return msgBoxUI;
    }

    public void update(ArrayList<Msg> msgs)
    {
        msgBoxAdaptor.update(msgs);
    }

    public void onDestroy()
    {
        msgBoxUI=null;
    }
}
