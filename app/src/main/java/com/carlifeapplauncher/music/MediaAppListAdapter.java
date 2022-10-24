package com.carlifeapplauncher.music;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.session.MediaController;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banqu.samsung.music.R;

import java.util.List;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.PopupWindow;

import androidx.annotation.NonNull;

public class MediaAppListAdapter extends RecyclerView.Adapter<MediaAppListAdapter.ViewHolder> {
    private List<MediaController> playerlist;
    private Context c;
    private PopupWindow pw;
    //    private MainActivity ma;
    private View.OnClickListener ocl;
//    private UniveralMusicApp obj;

    public MediaAppListAdapter(Context c) {

        playerlist = MusicService.getInstance(c.getApplicationContext()).getActiveSessions();
        this.c = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.playerrow, parent, false);
        MediaAppListAdapter.ViewHolder viewHolder = new MediaAppListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        //set icon size
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        LinearLayout.LayoutParams imglp = (LinearLayout.LayoutParams) holder.img.getLayoutParams();
        imglp.height = Integer.valueOf(sharedPreferences.getString("launcher_icon_size", "200"));
        imglp.width = Integer.valueOf(sharedPreferences.getString("launcher_icon_size", "200"));
        holder.img.setLayoutParams(imglp);

        try {
            PackageManager pm = c.getPackageManager();
            ApplicationInfo temp_info = pm.getApplicationInfo(playerlist.get(i).getPackageName(), PackageManager.GET_META_DATA);
            holder.textView.setText(pm.getApplicationLabel(temp_info));
            holder.img.setImageDrawable(pm.getApplicationIcon(temp_info));
        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return playerlist.size();
    }

    public void linkpop(PopupWindow pw) {
        this.pw = pw;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            textView.setTextColor(c.getColor(R.color.text_color));
            img = (ImageView) itemView.findViewById(R.id.ta_cell_img);
            itemView.setOnClickListener(ocl);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicService.getInstance(c.getApplicationContext()).switchFromToken(MediaSessionCompat.Token.fromToken(playerlist.get(getAdapterPosition()).getSessionToken()));
                    pw.dismiss();
                }
            });

        }
    }
}
