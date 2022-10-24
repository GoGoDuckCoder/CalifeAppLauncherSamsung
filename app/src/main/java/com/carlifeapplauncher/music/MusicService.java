package com.carlifeapplauncher.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.carlifeapplauncher.NotificationListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

public class MusicService {


    private static String TAG = "MusicService";

    public static MusicService musicServiceConnect;
    private MediaSessionManager mediaSessionManager;
    private Context context;
    private ComponentName notificationListenerComponentName;
    private MediaSessionCompat.Token focus;
    private MediaControllerCompat mediaControllerCompat;
//    private boolean has_pure_album;
//    private MediaBrowserCompat mediaBrowserCompat;
//    private final MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {
//        @Override
//        public void onConnected() {
//            Log.e(TAG, "onConnected------");
//            //必须在确保连接成功的前提下执行订阅的操作
//            if (mediaBrowserCompat.isConnected()) {
//                String mediaId = mediaBrowserCompat.getRoot();
////                        mediaBrowserCompat.
//                mediaBrowserCompat.unsubscribe(mediaId);
//                //get roots
////                last_id = mediaId;
//                last_subscribe = mediaId;
//                mediaBrowserCompat.subscribe(mediaId, browserSubscriptionCallback);
//            }
////                    if (mBrowser.isConnected()) {
////                        //mediaId即为MediaBrowserService.onGetRoot的返回值
////                        //若Service允许客户端连接，则返回结果不为null，其值为数据内容层次结构的根ID
////                        //若拒绝连接，则返回null
////                        String mediaId = mBrowser.getRoot();
////
////                        //Browser通过订阅的方式向Service请求数据，发起订阅请求需要两个参数，其一为mediaId
////                        //而如果该mediaId已经被其他Browser实例订阅，则需要在订阅之前取消mediaId的订阅者
////                        //虽然订阅一个 已被订阅的mediaId 时会取代原Browser的订阅回调，但却无法触发onChildrenLoaded回调
////
////                        //ps：虽然基本的概念是这样的，但是Google在官方demo中有这么一段注释...
////                        // This is temporary: A bug is being fixed that will make subscribe
////                        // consistently call onChildrenLoaded initially, no matter if it is replacing an existing
////                        // subscriber or not. Currently this only happens if the mediaID has no previous
////                        // subscriber or if the media content changes on the service side, so we need to
////                        // unsubscribe first.
////                        //大概的意思就是现在这里还有BUG，即只要发送订阅请求就会触发onChildrenLoaded回调
////                        //所以无论怎样我们发起订阅请求之前都需要先取消订阅
////                        //之前说到订阅的方法还需要一个参数，即设置订阅回调SubscriptionCallback
////                        //当Service获取数据后会将数据发送回来，此时会触发SubscriptionCallback.onChildrenLoaded回调
////                        mBrowser.subscribe(mediaId, BrowserSubscriptionCallback);
////                    }
//        }
//
//        @Override
//        public void onConnectionFailed() {
//            Toast.makeText(context, "连接服务失败", Toast.LENGTH_SHORT).show();
//        }
//    };
//    ;
//    private final MediaBrowserCompat.SubscriptionCallback browserSubscriptionCallback =
//            new MediaBrowserCompat.SubscriptionCallback() {
//                @Override
//                public void onChildrenLoaded(@NonNull String parentId,
//                                             @NonNull List<MediaBrowserCompat.MediaItem> children) {
//                    Log.e(TAG, "onChildrenLoaded------");
//                    Log.e(TAG, "parentId: " + parentId);
//                    Log.e(TAG, "size: " + children.size());
//                    //children 即为Service发送回来的媒体数据集合
//                    for (MediaBrowserCompat.MediaItem item : children) {
//                        Log.i(TAG, "Item: " + item.getDescription().getTitle().toString());
////                                list.add(item);
//                    }
//
////                    if (musicUIInterface != null) {
////                        if (mediaBrowserCompat.getRoot().equals(parentId)) {
////                            musicUIInterface.recerive_MediaItems(children, true);
////                        } else {
////                            musicUIInterface.recerive_MediaItems(children, false);
////                        }
////                    }
//
//
//                    //在onChildrenLoaded可以执行刷新列表UI的操作
////                            demoAdapter.notifyDataSetChanged();
//                }
//            };
//    ;


    private final MediaControllerCompat.Callback mediaControllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            service_onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            service_onMetadataChanged(metadata);
        }
    };
    private AudioManager audioManager;


    @NonNull
    public static MusicService getInstance(Context context) {
        if (musicServiceConnect == null) {
            Log.i(TAG, "getInstance 新建");
            musicServiceConnect = new MusicService(context);
            musicServiceConnect.getFocus();
        }
        Log.i(TAG, "getInstance 服务中");
        return musicServiceConnect;
    }

    private boolean focus_dead = false;
//    private boolean aquire_album_from_notification;
    private Boolean lock_music_player;


    public MusicService(Context context) {
        this.context = context;
//        has_pure_album = false;
        lock_music_player = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("lock_music_player", false);
//        aquire_album_from_notification = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("album_catch", false);
        notificationListenerComponentName = new ComponentName(context, NotificationListener.class);
        mediaSessionManager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
        mediaSessionManager.addOnActiveSessionsChangedListener(new MediaSessionManager.OnActiveSessionsChangedListener() {
            @Override
            public void onActiveSessionsChanged(@Nullable List<MediaController> list) {
                if (focus == null) {
                    getFocus();
                } else {
                    boolean closed = true;
                    for (MediaController tempmc : list) {
                        if (MediaSessionCompat.Token.fromToken(tempmc.getSessionToken()).equals(focus)) {
                            closed = false;
                        }
                    }
                    if (closed) {
                        focus_dead = true;
                        focus = null;
                        getFocus();
                    }
                }
            }

        }, notificationListenerComponentName);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        Log.i(TAG, "初始化完成");


    }

    public void getFocus() {
        String pkg = load_lock_music_player(context);
        Log.i(TAG, "lock_music_player: " + lock_music_player + " " + pkg);
        if (lock_music_player && !pkg.equals("false")) {
            List<MediaController> list = mediaSessionManager.getActiveSessions(notificationListenerComponentName);
            for (MediaController temp : list) {
                if (temp.getPackageName().equals(pkg)) {
                    focus = MediaSessionCompat.Token.fromToken(temp.getSessionToken());
                    break;
                }
            }
        } else {
            List<MediaController> list = mediaSessionManager.getActiveSessions(notificationListenerComponentName);
            for (MediaController temp : list) {
                if (!temp.getPackageName().equals(context.getPackageName())) {
                    focus = MediaSessionCompat.Token.fromToken(temp.getSessionToken());
                    break;
                }
            }
        }
        switchFocus();
    }


    private void switchFocus() {
        if (focus == null) {
            switchNullFocus();
            return;
        }
        //active session
//        if (mediaPlaybackServiceInterface != null) {
//            mediaPlaybackServiceInterface.active_session();
//        }
        //unload
        if (focus_dead) {
            mediaControllerCompat = null;
        } else {
            if (mediaControllerCompat != null) {
                mediaControllerCompat.unregisterCallback(mediaControllerCallback);
            }
        }
        focus_dead = false;

        //new controller and callback
        mediaControllerCompat = new MediaControllerCompat(context, focus);
        mediaControllerCompat.registerCallback(mediaControllerCallback);
        save_lock_music_player(context, mediaControllerCompat.getPackageName());
        //Get App Name
        service_onAppNameChanged(getAppName(mediaControllerCompat.getPackageName()));
        service_onPackageNameChanged(mediaControllerCompat.getPackageName());
//        service_onPackageNameChanged(mediaControllerCompat.getPackageName());
        //If has active Meta

        service_onMetadataChanged(mediaControllerCompat.getMetadata());
        service_onPlaybackStateChanged(mediaControllerCompat.getPlaybackState());

        //mediaControllerCallback.onMetadataChanged(mediaControllerCompat.getMetadata());
        //mediaControllerCallback.onPlaybackStateChanged(mediaControllerCompat.getPlaybackState());


        Log.i(TAG, "switchFocus Done");
    }

//    public void request_MenuItems(String parent_id) {
//        if (mediaBrowserCompat != null && mediaBrowserCompat.isConnected()) {
//            if (!last_subscribe.equals("")) {
//                mediaBrowserCompat.unsubscribe(last_subscribe);
//            }
//            last_subscribe = parent_id;
//            mediaBrowserCompat.subscribe(parent_id, browserSubscriptionCallback);
//        }
//    }


    private String getAppName(String packageName) {
        String name = "车联助手";
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo temp_info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            name = pm.getApplicationLabel(temp_info).toString();
        } catch (Exception e) {

        }
        return name;
    }

    private void switchNullFocus() {
        if (focus_dead) {
            mediaControllerCompat = null;
        } else {
            if (mediaControllerCompat != null) {
                mediaControllerCompat.unregisterCallback(mediaControllerCallback);
            }
        }
        focus_dead = false;

        service_onAppNameChanged("车联助手");
        service_onPackageNameChanged(null);
        service_onMetadataChanged(defaultMetadata());
        service_onPlaybackStateChanged(defaultPlaybackState());

//        if (mediaPlaybackServiceInterface != null) {
//            mediaPlaybackServiceInterface.kill_session();
//        }
//        mediaSession.setMetadata(metaBuilder.build());
    }

    public MediaMetadataCompat defaultMetadata() {
        MediaMetadataCompat.Builder metaBuilder = new MediaMetadataCompat.Builder().putString(MediaMetadataCompat.METADATA_KEY_TITLE, "车联助手")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "等待播放器中").putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, null);
        return metaBuilder.build();
    }

    private PlaybackStateCompat defaultPlaybackState() {
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(PlaybackStateCompat.STATE_PLAYING, 31415, 1);

        return stateBuilder.build();
    }


    private void service_onAppNameChanged(String name) {
        Log.i("MusicUI", "service_onPackageNameChanged: " + name);
        if (musicUIInterface != null) {
            musicUIInterface.onAppNameChanged(name);

        }
    }

    private void service_onPackageNameChanged(String pkgname) {
        Log.i("MusicUI", "service_onPackageNameChanged: " + pkgname);
        if (musicUIInterface != null) {
            musicUIInterface.onPackageNameChanged(pkgname);
        }
    }


    private void service_onPlaybackStateChanged(PlaybackStateCompat state) {
        if (musicUIInterface != null) {
            musicUIInterface.onPlaybackStateChanged(playbackStateFactory(state));
        }
        if (mediaPlaybackServiceInterface != null) {
            mediaPlaybackServiceInterface.onPlaybackStateChanged(playbackStateFactory(state));
        }
    }

    private PlaybackStateCompat playbackStateFactory(PlaybackStateCompat state) {

        if (state == null) {
            return defaultPlaybackState();
        }
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(state.getActions())
                .setState(state.getState(), state.getPosition(), state.getPlaybackSpeed());
        return stateBuilder.build();
    }


    private void service_onMetadataChanged(MediaMetadataCompat metadata) {
        metadata = metadataFactory(metadata);
        if (mediaPlaybackServiceInterface != null) {
            mediaPlaybackServiceInterface.onMetadataChanged(metadata);
        }
        if (musicUIInterface != null) {
            musicUIInterface.onMetadataChanged(metadata);
        }

//        Log.i(TAG, "service_onMetadataChanged: " + has_pure_album);
//        Bitmap album = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART);
//        if (album == null && !has_pure_album) {
//            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("album_catch", false)) {
//                Log.i(TAG, "service_onMetadataChanged: 去找专辑");
//                get_Album_From_Notification();
//            }
//        }

    }


    //0 system
    //1 notification
    private MediaMetadataCompat metadataFactory(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return defaultMetadata();
        }
        MediaMetadataCompat.Builder metaBuilder = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
        Bitmap album_art = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART);
        Bitmap art = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ART);
        Bitmap cover = null;
        if (album_art != null) {
            cover = album_art;
        } else {
            if (art != null) {
                cover = art;
            }
        }
//        if (cover == null) {
//            has_pure_album = false;
//        } else {
//            has_pure_album = true;
//        }
        metaBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, cover);


//        String lyrics =metadata.getString("ucar.media.metadata.LYRICS_LINE");
//        if(lyrics!=null) {
//            metaBuilder.putString("lyrics", metadata.getString("ucar.media.metadata.LYRICS_LINE"));
//        }
//        else
//        {
//            metaBuilder.putString("lyrics", "");
//        }

        return metaBuilder.build();
    }


    //MusicUIInterface
    private MusicUIInterface musicUIInterface;

    public void setMusicUIInterface(MusicUI musicUI) {
        this.musicUIInterface = musicUI;
        if (focus != null) {
            service_onAppNameChanged(getAppName(mediaControllerCompat.getPackageName()));
            service_onPackageNameChanged(mediaControllerCompat.getPackageName());
            service_onMetadataChanged(mediaControllerCompat.getMetadata());
            service_onPlaybackStateChanged(mediaControllerCompat.getPlaybackState());

        } else {
            service_onAppNameChanged("车联助手");
            service_onPackageNameChanged(null);
            service_onMetadataChanged(null);
            service_onPlaybackStateChanged(null);
        }
    }

    public void removeMusicUIInterface() {
        this.musicUIInterface = null;
    }


    //MediaPlaybackServiceInterface
    private MediaPlaybackServiceInterface mediaPlaybackServiceInterface;

    public void setPlaybackServiceInterface(MediaPlaybackServiceInterface mediaPlaybackServiceInterface) {
        this.mediaPlaybackServiceInterface = mediaPlaybackServiceInterface;
        if (focus != null) {
            service_onMetadataChanged(mediaControllerCompat.getMetadata());
            service_onPlaybackStateChanged(mediaControllerCompat.getPlaybackState());
        } else {
            service_onAppNameChanged("车联助手");
            service_onPackageNameChanged(null);
            service_onMetadataChanged(null);
            service_onPlaybackStateChanged(null);
        }

    }


    public void play() {
        if (focus == null) {
            return;
        }
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("switch_preference_1", false)) {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY);
            audioManager.dispatchMediaKeyEvent(event);
        } else {
            mediaControllerCompat.getTransportControls().play();
        }
    }

    public void pause() {
        if (focus == null) {
            return;
        }
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("switch_preference_1", false)) {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE);
            audioManager.dispatchMediaKeyEvent(event);
        } else {
            mediaControllerCompat.getTransportControls().pause();
//            mediaControllerCompat.getTransportControls().
        }
    }

    public void stop() {
        if (focus == null) {
            return;
        }
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("switch_preference_1", false)) {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_STOP);
            audioManager.dispatchMediaKeyEvent(event);
        } else {
            mediaControllerCompat.getTransportControls().stop();
        }
    }

    public void next() {
        if (focus == null) {
            return;
        }
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("switch_preference_1", false)) {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
            audioManager.dispatchMediaKeyEvent(event);
        } else {
            mediaControllerCompat.getTransportControls().skipToNext();
        }
    }

    public void previous() {
        if (focus == null) {
            return;
        }
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("switch_preference_1", false)) {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
            audioManager.dispatchMediaKeyEvent(event);
        } else {
            mediaControllerCompat.getTransportControls().skipToPrevious();
        }
    }

    public List<MediaController> getActiveSessions() {
        List<MediaController> t = mediaSessionManager.getActiveSessions(notificationListenerComponentName);
        List<MediaController> r = new ArrayList<MediaController>();
        for (MediaController x : t) {
            if (!x.getPackageName().equals(context.getPackageName())) {
                r.add(x);
            }
        }
        return r;
    }

    public void switchFromToken(MediaSessionCompat.Token token) {
        focus = token;
        switchFocus();
    }


//    private void get_Album_From_Notification() {
//        if (!aquire_album_from_notification) {
//            return;
//        }
//        Log.i(TAG, "service_onMetadataChanged: 找专辑");
//        if (NotificationListener.isReady()) {
//            Log.i(TAG, "service_onMetadataChanged: 找专辑 不ready");
//            if (mediaControllerCompat != null && mediaControllerCompat.getPackageName() != null
//                    && mediaControllerCompat.getMetadata() != null
//                    && mediaControllerCompat.getMetadata().getString(MediaMetadata.METADATA_KEY_TITLE) != null) {
//                Log.i(TAG, "get_Album_From_Notification");
//                NotificationListener.getInstance().task_music_start(mediaControllerCompat.getPackageName(), mediaControllerCompat.getMetadata().getString(MediaMetadata.METADATA_KEY_TITLE));
//            }
//        }
//    }

//    public void setup_Album_From_Notification(String focus, String title, Bitmap album) {
//        if (has_pure_album) {
//            return;
//        }
//        if (mediaControllerCompat != null && mediaControllerCompat.getPackageName() != null
//                && mediaControllerCompat.getMetadata() != null
//                && mediaControllerCompat.getMetadata().getString(MediaMetadata.METADATA_KEY_TITLE) != null) {
//            if (mediaControllerCompat.getPackageName().equals(focus) && mediaControllerCompat.getMetadata().getString(MediaMetadata.METADATA_KEY_TITLE).equals(title)) {
//                Log.i(TAG, "setup_Album_From_Notification");
//                MediaMetadataCompat.Builder metaBuilder = new MediaMetadataCompat.Builder()
//                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mediaControllerCompat.getMetadata().getString(MediaMetadataCompat.METADATA_KEY_TITLE))
//                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mediaControllerCompat.getMetadata().getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
//                        .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, album);
//                service_onMetadataChanged(metaBuilder.build());
//            }
//        }
//    }


    public static String load_lock_music_player(Context context) {
        String pkg = PreferenceManager.getDefaultSharedPreferences(context).getString("lock_music_player_pkg", "false");
        return pkg;
    }

    public static void save_lock_music_player(Context context, String pkgname) {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        ed.putString("lock_music_player_pkg", pkgname);
        ed.apply();
    }


//    public void destory()
//    {
//        if(musicUIInterface!=null&&mediaPlaybackServiceInterface==null)
//        {
//            musicServiceConnect = null;
//        }
//    }


    //music ui

//    public boolean try_connect_MediaBrowser() {
//        if (mediaControllerCompat == null || mediaControllerCompat.getPackageName() == null) {
//            return false;
//        }
////        if (!mediaControllerCompat.getPackageName().equals("com.netease.cloudmusic")) {
////            return false;
////        }
//        if (!mediaControllerCompat.getPackageName().equals("cmccwm.mobilemusic")) {
//            return false;
//        }
//
//        mediaBrowserCompat = new MediaBrowserCompat(context,
//                new ComponentName("cmccwm.mobilemusic", "com.migu.music.car.CarMusicService"),
//                connectionCallback,
//                null);
//        mediaBrowserCompat.connect();
//        return true;
//    }

//    private String last_subscribe = "";

//    public void try_disconnect_MediaBrowser() {
//        if (mediaBrowserCompat != null && mediaBrowserCompat.isConnected()) {
//            Log.i(TAG, "try_disconnect_MediaBrowser: disconnect");
//            if (!last_subscribe.equals("")) {
//                mediaBrowserCompat.unsubscribe(last_subscribe);
//            }
//            mediaBrowserCompat.disconnect();
//        }
//    }
//    public void play_from_media_id(MediaBrowserCompat.MediaItem item)
//    {
//        if (mediaBrowserCompat != null && mediaBrowserCompat.isConnected()&&mediaControllerCompat!=null) {
//            Log.i(TAG, "play_from_media_id: "+item.getDescription().getTitle()+" "+item.getMediaId()+ " "+item.getDescription().getMediaId());
////            mediaControllerCompat.getTransportControls().playFromMediaId(item.getMediaId(),item.getDescription().getExtras());
////            mediaBrowserCompat.
//            Log.i(TAG, "token: "+focus);
//            Log.i(TAG, "token: "+mediaBrowserCompat.getSessionToken());
//            mediaControllerCompat.getTransportControls().pause();
//            Bundle bd = new Bundle();;
//            bd.putString("ucar.media.bundle.MEDIA_ID",item.getMediaId());
//            bd.putString("ucar.media.bundle.MEDIA_ID",item.getMediaId());
//            mediaControllerCompat.getTransportControls().playFromMediaId(item.getMediaId(),bd);
//        }
//    }

}
