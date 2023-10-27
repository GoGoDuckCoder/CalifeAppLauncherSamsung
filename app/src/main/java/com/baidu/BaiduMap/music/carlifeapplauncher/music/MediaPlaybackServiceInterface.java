package com.baidu.BaiduMap.music.carlifeapplauncher.music;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

public interface MediaPlaybackServiceInterface {
    public void onMetadataChanged(MediaMetadataCompat metadata);

    public void onPlaybackStateChanged(PlaybackStateCompat state);

    public void kill_session();

    public void active_session();
}
