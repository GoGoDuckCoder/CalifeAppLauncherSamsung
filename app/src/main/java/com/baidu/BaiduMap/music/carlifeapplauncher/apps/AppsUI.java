package com.baidu.BaiduMap.music.carlifeapplauncher.apps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.BaiduMap.music.databinding.AppsBinding;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FullAppListAdapter;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FullAppListAdapterMode;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AppsUI {

    private Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView favodrawer;
    private AppsBinding binding;

    public AppsUI(Context context, LayoutInflater layoutInflater, ViewGroup root, boolean deeplink) {
        this.context = context;
        this.layoutInflater = layoutInflater;

        binding = AppsBinding.inflate(layoutInflater);
        root.addView(binding.getRoot());

        favodrawer = binding.appdrawer;

//        FullAppListAdapter  favadapter = new FullAppListAdapter(context, FullAppListAdapterMode.mode_favorite,1);
//            favodrawer.setAdapter(favadapter);
//            favodrawer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        FullAppListAdapter favadapter;
        if (deeplink) {
            favadapter = new FullAppListAdapter(context, FullAppListAdapterMode.mode_deeplink, 0);
        } else {
            favadapter = new FullAppListAdapter(context, FullAppListAdapterMode.mode_favorite, 0);
        }

        favodrawer.setAdapter(favadapter);
//                    int col = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("launcher_col", "5"))
        int col = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("launcher_col", "3"));
        favodrawer.setLayoutManager(new GridLayoutManager(context, col, RecyclerView.VERTICAL, false));

        if(favadapter.getItemCount()==0)
        {
            binding.appdrawer.setVisibility(View.GONE);
            binding.textView13.setVisibility(View.VISIBLE);
        }else
        {
            binding.appdrawer.setVisibility(View.VISIBLE);
            binding.textView13.setVisibility(View.GONE);
        }
    }

}
