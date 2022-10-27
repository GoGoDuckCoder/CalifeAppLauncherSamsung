package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.banqu.samsung.music.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class FavoFragment extends Fragment {
    private RecyclerView fullapplistdrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_full_app_list, container, false);



        FullAppListAdapter adapter = new FullAppListAdapter(getContext(), FullAppListAdapterMode.mode_edit, 0);

        fullapplistdrawer = view.findViewById(R.id.fullapplist);
        ((SimpleItemAnimator) fullapplistdrawer.getItemAnimator()).setSupportsChangeAnimations(false);
        //adapter.setHasStableIds(true);
        fullapplistdrawer.setLayoutManager(new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL, false));
        fullapplistdrawer.setAdapter(adapter);

        EditText serachbox = view.findViewById(R.id.searchbox);
//        serachbox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (serachbox.getText().equals("搜索")) {
//                    serachbox.setText("");
//                }
//            }
//        });
        serachbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.i("SEARCH", s.toString());
//                Log.i("SEARCH", "update adapter");
                adapter.search(s.toString());
                fullapplistdrawer.setAdapter(adapter);
//                fullapplistdrawer.setAdapter(adapter);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle b) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (fullapplistdrawer != null) {
//            fullapplistdrawer.setAdapter(new FullAppListAdapter(getContext(), FullAppListAdapterMode.mode_edit, 0));
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//            int col = Integer.valueOf(sharedPreferences.getString("launcher_col", "5"));
//            fullapplistdrawer.setLayoutManager(new GridLayoutManager(getContext(), col, RecyclerView.VERTICAL, false));
//        }
    }
}
