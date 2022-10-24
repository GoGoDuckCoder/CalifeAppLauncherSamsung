package com.carlifeapplauncher.music;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaMetadata;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.banqu.samsung.music.R;
import com.banqu.samsung.music.databinding.MusicplayerBinding;
import com.carlifeapplauncher.NotificationListener;
import com.carlifeapplauncher.adapter.Common;
import com.carlifeapplauncher.adapter.FakeStart;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MusicUI implements MusicUIInterface {

    private MusicService musicService;
    private static String TAG = "MusicUI";
    private String pkgName = null;
    private boolean show_album;
    private Context context;

    private boolean adaptive;

    private MusicplayerBinding binding;

    public MusicUI(Context context, LayoutInflater layoutInflater, ViewGroup root) {

        boolean haspermission = NotificationListener.isEnabled(context);

        this.adaptive = false;
        this.context = context;
        this.show_album = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("showalbum", true);
        Log.i(TAG, "MusicUI 实例化");

        binding = MusicplayerBinding.inflate(layoutInflater);
        root.addView(binding.getRoot());

        if (haspermission) {
            binding.musk.setVisibility(View.GONE);
            musicService = MusicService.getInstance(context.getApplicationContext());
            musicService.setMusicUIInterface(this);


            binding.LastMusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicService.previous();
                }
            });
            binding.LastMusic.setFocusable(false);

            binding.NextMusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicService.next();
                }
            });
            binding.NextMusic.setFocusable(false);

            binding.Play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicService.play();
                }
            });
            binding.Play.setFocusable(false);

            binding.Pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicService.pause();
                }
            });
            binding.Pause.setFocusable(false);
//        binding.Pause.setVisibility(View.GONE);


            binding.SwitchSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //view
                    View pop_playerlist_view = View.inflate(context, R.layout.playerpopwindows, null);
                    Common.setBgRadius(pop_playerlist_view, 50);


                    pop_playerlist_view.setBackgroundColor(context.getColor(R.color.normal_panel));
                    RecyclerView plyerlist_rec = pop_playerlist_view.findViewById(R.id.playerlist_rec);
                    ((TextView) pop_playerlist_view.findViewById(R.id.textView2)).setTextColor(context.getColor(R.color.text_color));

                    //LayoutManager
                    LinearLayoutManager lm = new LinearLayoutManager(context);
                    lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    plyerlist_rec.setLayoutManager(lm);

                    //Set Adapter
                    MediaAppListAdapter maa = new MediaAppListAdapter(context);
                    plyerlist_rec.setAdapter(maa);

                    //Set PopWindows
                    PopupWindow playerlist = new PopupWindow(pop_playerlist_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    playerlist.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    playerlist.setHeight(playerlist.getContentView().getMeasuredHeight());
                    playerlist.setOutsideTouchable(true);
//                playerlist.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0, 0, 0)));

                    playerlist.showAsDropDown(view);
                    maa.linkpop(playerlist);
                }
            });
            binding.SwitchSource.setFocusable(false);

            binding.labels.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pkgName != null) {
                        try {
                            FakeStart.Start(context, pkgName);
                        } catch (Exception e) {

                        }
                    }
                }
            });
            binding.labels.setFocusable(false);


        } else {
            binding.musk.setVisibility(View.VISIBLE);
            binding.Pause.setVisibility(View.GONE);
        }


        int musk = context.getResources().getColor(R.color.Album_musk, context.getTheme());
        int red = (musk & 0xff0000) >> 16;
        int green = (musk & 0x00ff00) >> 8;
        int blue = (musk & 0x0000ff);
        int alpha = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("album_musk_alpha", "80"));
        binding.musicmusk.setBackgroundColor(Color.argb(alpha, red, green, blue));
//Color.argb()

//        binding.musicplayerwindows.setVisibility(View.GONE);
        Log.i(TAG, "MusicUI onCreate 完成");

    }

    public void onDestroy() {

//        if (musicService != null) {
//            Log.i(TAG, "MusicService try_disconnect_MediaBrowser 完成");
//            musicService.try_disconnect_MediaBrowser();
//        }

        if (musicService != null) {
            musicService.pause();
            musicService.removeMusicUIInterface();
        }

        Log.i(TAG, "MusicUI onDestroy 完成");
    }


    @Override
    public void onAppNameChanged(String appName) {
        Log.i(TAG, "onAppNameChanged: " + appName);
//        Log.i(TAG, "pkg:"+ pkgName);

        binding.MusicSessionName.setText(appName);
//        binding.appIconMain.set
    }

    @Override
    public void onPackageNameChanged(String pkgName) {
        Log.i(TAG, "onPackageNameChanged : " + pkgName);
        this.pkgName = pkgName;

//
//        AppInfo info = Common.getInstance(context).loadAppInfo(pkgName);
//        if (info.icon != null) {
//            binding.appIconMain.setImageDrawable(info.icon);
//            MainActivityFinal.setBgRadiusOval(binding.appIconMain);
//            binding.appIconMainFrame.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    if (is_Browser_Init) {
////                        binding.adaptivewindows.setVisibility(View.VISIBLE);
////                        return;
////                    }
////                    if (musicService != null) {
////                        if (musicService.try_connect_MediaBrowser()) {
////                            Toast.makeText(context, "连接中", Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(context, "不支持该播放器适配服务", Toast.LENGTH_SHORT).show();
////                        }
////                    }
//                }
//            });
//        }

    }


    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        Log.i(TAG, "onMetadataChanged");

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.MusicName.setText(metadata.getString(MediaMetadata.METADATA_KEY_TITLE));
                binding.MusicArtist.setText(metadata.getString(MediaMetadata.METADATA_KEY_ARTIST));
//                binding.lyrics.setText(metadata.getString("lyrics"));

                if (show_album && metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART) != null) {
                    binding.musicmusk.setVisibility(View.VISIBLE);
                    binding.AlbumImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    binding.AlbumImage.setImageBitmap(metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART));
                } else {
                    binding.AlbumImage.setImageBitmap(null);
                    binding.musicmusk.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        Log.i(TAG, "onPlaybackStateChanged");
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
            case PlaybackStateCompat.STATE_BUFFERING:
                binding.Pause.setVisibility(View.VISIBLE);
                binding.Play.setVisibility(View.GONE);
                break;
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_STOPPED:
                binding.Pause.setVisibility(View.GONE);
                binding.Play.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }


//    private boolean is_Browser_Init = false;
//    private MusicListAdapter adaper;
//
//    @Override
//    public void recerive_MediaItems(List<MediaBrowserCompat.MediaItem> items, boolean isRoot) {
//
//        if (isRoot) {
//            for (MediaBrowserCompat.MediaItem item : items) {
//                LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.music_root_item, null);
//                frame.setVerticalGravity(Gravity.CENTER);
//                TextView i = frame.findViewById(R.id.item_title);
//                i.setText(item.getDescription().getTitle().toString());
//                ImageView img = frame.findViewById(R.id.item_icon);
//                img.setImageBitmap(item.getDescription().getIconBitmap());
//                frame.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        musicService.request_MenuItems(item.getMediaId());
//                    }
//                });
//
//                frame.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewGroup.LayoutParams.MATCH_PARENT, 1));
//                binding.rootmenuframe.addView(frame);
//            }
//
//            AppInfo info = Common.getInstance(context).loadAppInfo(pkgName);
//            if (info.icon != null) {
//                binding.appIcon.setImageDrawable(info.icon);
//                MainActivityFinal.setBgRadiusOval(binding.appIcon);
//            }
////            binding.appIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            binding.appIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    binding.adaptivewindows.setVisibility(View.GONE);
//                }
//            });
//
//            adaper = new MusicListAdapter(context,musicService);
//            binding.itemListview.setAdapter(adaper);
//            binding.itemListview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//            binding.adaptivewindows.setVisibility(View.VISIBLE);
//            is_Browser_Init = true;
//        } else {
//            if (adaper != null) {
//                adaper.setItems(items);
//            }
//        }
//    }

}
