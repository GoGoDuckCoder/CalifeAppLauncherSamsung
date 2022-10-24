package com.carlifeapplauncher.music;

import android.media.browse.MediaBrowser;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import java.util.List;

interface MusicUIInterface{
    public void onAppNameChanged(String appName);

    public void onPackageNameChanged(String pkgName);

    public void onMetadataChanged(MediaMetadataCompat metadata);

    public void onPlaybackStateChanged(PlaybackStateCompat state);

//    public void recerive_MediaItems(List<MediaBrowserCompat.MediaItem> items, boolean isRoot);

}