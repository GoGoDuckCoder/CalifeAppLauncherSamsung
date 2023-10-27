package com.baidu.BaiduMap.music.carlifeapplauncher.music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.baidu.BaiduMap.music.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.MediaBrowserServiceCompat;

public class MediaPlaybackService extends MediaBrowserServiceCompat implements MediaPlaybackServiceInterface {
    //    private static final String MY_MEDIA_ROOT_ID = "media_root_id";
//    private static final String MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id";
    private static final String TAG = "MediaPlayBackService";

    private MediaSessionCompat mediaSession;
    private MusicService musicService;

    private NotificationManager notificationManager;
    private static String id = "music_mirror";
    private static String name = "music_mirror_service";
    private MusicServiceShutdownReceiver musicServiceShutdownReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create a MediaSessionCompat
        mediaSession = new MediaSessionCompat(getBaseContext(), TAG);
//        mediaSession = new MediaSessionCompat(getBaseContext(), TAG);
        musicService = MusicService.getInstance(getApplicationContext());
        musicService.setPlaybackServiceInterface(this);
        // Enable callbacks from MediaButtons and TransportControls
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        // MySessionCallback() has methods that handle callbacks from a media controller
        mediaSession.setCallback(new MySessionCallback());
        // Set the session's token so that client activities can communicate with it.
        setSessionToken(mediaSession.getSessionToken());
        mediaSession.setActive(true);

//      mediaSession


//        boolean adapt = false;
////        int status = 0;
//        ContentResolver cr = getContentResolver();
//        try {
//            Bundle bd = cr.call("com.samsung.android.carlink.carlife.openprovider", "get_connection_status", null, null);
//            int status = bd.getInt("connection_status");
//            adapt = true;
//        } catch (Exception e) {
//            Log.i("TTTT", "adapt: " + e);
//        }


        //Notifications
        notificationManager = getSystemService(NotificationManager.class);
        Notification mirror_service = createNotification();

        if (notificationManager != null && mirror_service != null) {
            mirror_service.flags |= Notification.FLAG_ONGOING_EVENT;
            notificationManager.notify(31415926, mirror_service);
            startForeground(31415926, mirror_service);
        }



//        if (adapt) {
//            Log.i("TTTT", "adapt: ");
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    Bundle bd = cr.call("com.samsung.android.carlink.carlife.openprovider", "get_connection_status", null, null);
//                    int status = bd.getInt("connection_status");
//                    if (status == 0 && MainActivityFinal.mainActivityFinal == null) {
//                        MediaSessionConnectionOperator.getInstance(getApplicationContext()).disconnect();
//                        timer.cancel();
//                    }
//                }
//            },  10*1000, 3*1000);
//        } else {
//            Log.i("TTTT", "Not adapt: ");
//
//        }
        musicServiceShutdownReceiver = new MusicServiceShutdownReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(musicServiceShutdownReceiver, intentFilter);


        if (MediaSessionConnectionOperator.getInstance(null) != null) {
            MediaSessionConnectionOperator.getInstance(null).set_Connecting(false);
            MediaSessionConnectionOperator.getInstance(null).set_serviceReady(true);
        }

    }

    @Override
    public void onDestroy() {
        if (musicServiceShutdownReceiver != null) {
            this.unregisterReceiver(musicServiceShutdownReceiver);
            musicServiceShutdownReceiver = null;
        }

        if (musicService!=null) {
            musicService.pause();
        }



        mediaSession.setActive(false);
        mediaSession.release();
        notificationManager.cancel(31415926);


        if (MediaSessionConnectionOperator.getInstance(null) != null) {
            MediaSessionConnectionOperator.getInstance(null).set_Connecting(false);
            MediaSessionConnectionOperator.getInstance(null).set_serviceReady(false);
        }

        super.onDestroy();
    }

    private Notification createNotification() {
        if (notificationManager == null) {
            return null;
        }
        Intent clickIntent = new Intent(getApplicationContext(), MusicServiceNotificationReceiver.class); //点击通知之后要发送的广播
        clickIntent.setAction("kill_MusicService");
//        int id1 = (int) (System.currentTimeMillis() / 1000);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),
                0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channel);
        // you can't change any channel property after channel is registered
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getBaseContext(), id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("音乐镜像服务")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentText("服务运行中:点击可停止");
//        if (!adapt) {
//        }else
//        {
//            notificationBuilder.setContentText("服务运行中");
//        }

        return notificationBuilder.build();
//            notificationManager.notify(1,notificationBuilder.build());
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
//        if (clientPackageName.equals(getPackageName())) {
//            return new BrowserRoot(MY_MEDIA_ROOT_ID, null);
//        } else {
//            return new BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null);
//        }
        return null;
//        Log.i("MEDIA_BROWSER_SERVICE_COMPAT", "onGetRoot() called ID:" + MY_MEDIA_ROOT_ID);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        result.sendResult(mediaItems);
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            Log.i(TAG, "receiver action from carlife: on");
            musicService.play();
        }

        @Override
        public void onStop() {

            Log.i(TAG, "receiver action from carlife: onPlay");
            musicService.stop();
        }

        @Override
        public void onPause() {

            Log.i(TAG, "receiver action from carlife: onStop");
            musicService.pause();
        }

        @Override
        public void onSkipToNext() {

            Log.i(TAG, "receiver action from carlife: onNext");
            musicService.next();
        }

        @Override
        public void onSkipToPrevious() {

            Log.i(TAG, "receiver action from carlife: onPrevious");
            musicService.previous();
        }
    }

    //Interface Callbacks
    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        mediaSession.setMetadata(metadata);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        mediaSession.setPlaybackState(state);
    }

//    private Timer killer;

    @Override
    public void kill_session() {
//        stopSelf();
        //mediaSession.setActive(false);
    }

    @Override
    public void active_session() {
        //  mediaSession.setActive(true);
//        if(killer!=null)
//        {
//            killer.cancel();
//            killer = null;
//        }
//
//        Log.i("MusicService", "active_session: "+mediaSession.isActive());
//        if(mediaSession!=null&&!mediaSession.isActive())
//        {
//            mediaSession.setActive(true);
//        }
    }
}
