package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banqu.samsung.music.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FsFragment newInstance(String param1, String param2) {
        FsFragment fragment = new FsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            String a= Settings.Global.getString(requireContext().getContentResolver(),"policy_control");
            Settings.Global.putString(requireContext().getContentResolver(),"policy_control","immersive.full=*");
            Log.i("fstest",""+a);
        }catch (Exception e)
        {e.printStackTrace();}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fs, container, false);
    }
}