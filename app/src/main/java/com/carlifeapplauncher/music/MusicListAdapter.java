package com.carlifeapplauncher.music;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.banqu.samsung.music.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    private static List<MediaBrowserCompat.MediaItem> items;
    private Context context;
    private MusicService musicService;

    public MusicListAdapter(Context context, MusicService musicService) {
        this.context = context;
        this.musicService = musicService;
    }

    public void setItems(List<MediaBrowserCompat.MediaItem> new_items) {
        if (items == null) {
            items = new ArrayList<>();
            items.addAll(new_items);

        } else {
            items.clear();
            items.addAll(new_items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.music_list_item, parent, false);
        ViewHolder viewHolder = new MusicListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.ViewHolder holder, int position) {

        MediaBrowserCompat.MediaItem item = items.get(position);
        if (item.getDescription().getIconUri() != null && item.getDescription().getIconUri().getScheme() != null) {
            Log.i("MusicService", "Uri: " + item.getDescription().getIconUri());

            Uri uri = items.get(position).getDescription().getIconUri();

            Log.i("MusicService", "scheme: " + uri.getScheme());
            switch (uri.getScheme()) {
                case "content":
                    holder.item_icon.setImageURI(items.get(position).getDescription().getIconUri());
                    break;
                case "https":
                case "http":
                    Glide.with(context).load(uri.toString()).into(holder.item_icon);
                    break;
            }

        } else if (items.get(position).getDescription().getIconBitmap() != null) {
            holder.item_icon.setImageBitmap(items.get(position).getDescription().getIconBitmap());
        } else {
            ///////////////////////////////////?????????????????????
            holder.item_icon.setVisibility(View.GONE);
        }
        holder.item_title.setText(items.get(position).getDescription().getTitle().toString());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView item_icon;
        public TextView item_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_icon = itemView.findViewById(R.id.list_item_icon);
            item_title = itemView.findViewById(R.id.list_item_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            MediaBrowserCompat.MediaItem item = items.get(getAdapterPosition());
            Log.i("MusicService", "isBrowsable: "+item.isBrowsable());
            Log.i("MusicService", "isPlayable: "+item.isPlayable());
//            if(item.isBrowsable())
//            {
//                musicService.request_MenuItems(items.get(getAdapterPosition()).getDescription().getMediaId());
//
//            }
//            else if(item.isPlayable())
//            {
//                musicService.play_from_media_id(item);
//            }

        }
    }
}
