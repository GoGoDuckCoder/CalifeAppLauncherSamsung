package com.banqu.samsung.music.carlifeapplauncher.music;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

interface MusicUIInterface{
    public void onAppNameChanged(String appName);

    public void onPackageNameChanged(String pkgName);

    public void onMetadataChanged(MediaMetadataCompat metadata);

    public void onPlaybackStateChanged(PlaybackStateCompat state);

//    public void recerive_MediaItems(List<MediaBrowserCompat.MediaItem> items, boolean isRoot);

}