package com.baidu.BaiduMap.music.carlifeapplauncher.adapter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.baidu.BaiduMap.music.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class FakeStartFragment extends PreferenceFragmentCompat {
    private RecyclerView fullapplistdrawer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_full_app_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        EditText search = v.findViewById(R.id.searchbox);
        search.setVisibility(View.GONE);
        fullapplistdrawer = v.findViewById(R.id.fullapplist);



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( getContext());
        int col = Integer.valueOf(sharedPreferences.getString("launcher_col", "5"));
        FullAppListAdapter adapter;

        fullapplistdrawer = v.findViewById(R.id.fullapplist);
        adapter = new FullAppListAdapter(getContext(), FullAppListAdapterMode.mode_fakestart,0);
        //fullapplistdrawer.setHasFixedSize(true);
        ( (SimpleItemAnimator)fullapplistdrawer.getItemAnimator()).setSupportsChangeAnimations(false);
        //adapter.setHasStableIds(true);
        fullapplistdrawer.setAdapter(adapter);
        fullapplistdrawer.setLayoutManager(new GridLayoutManager(getContext(), col, RecyclerView.VERTICAL, false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fullapplistdrawer != null) {
            fullapplistdrawer.setAdapter(new FullAppListAdapter(getContext(), FullAppListAdapterMode.mode_fakestart,0));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( getContext());
            int col = Integer.valueOf(sharedPreferences.getString("launcher_col", "5"));
            fullapplistdrawer.setLayoutManager(new GridLayoutManager(getContext(), col, RecyclerView.VERTICAL, false));

        }
    }
}
