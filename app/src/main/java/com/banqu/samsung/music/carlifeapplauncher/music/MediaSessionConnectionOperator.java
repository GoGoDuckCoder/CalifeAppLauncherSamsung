package com.banqu.samsung.music.carlifeapplauncher.music;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MediaSessionConnectionOperator {


    public static MediaSessionConnectionOperator mediaSessionConnectionOperator;
    private static String TAG = "MediaSessionConnectionOperator";


    private Context context;
    private boolean serviceReady;
    private boolean connecting;
    private Intent musicServiceIntent;
//    private Boolean connecting = false;
//    private MediaBrowserCompat mediaBrowserCompat;
//    private final MediaBrowserCompat.ConnectionCallback connectionCallbacks =
//            new MediaBrowserCompat.ConnectionCallback() {
//                @Override
//                public void onConnected() {
//                    connecting= false;
//                    Log.i(TAG, "onConnected 已连接");
//                }
//
//                @Override
//                public void onConnectionSuspended() {
//                    Log.i(TAG, "onConnectionSuspended");
//                    //mediaBrowserCompat.connect();
//                    //connection = true;
//                    // The Service has crashed. Disable transport controls until it automatically reconnects
//                }
//
//                @Override
//                public void onConnectionFailed() {
//                    connecting = false;
//                    Log.i(TAG, "onConnectionFailed");
//                    //connection = false;
//                    // The Service has refused our connection
//                }
//            };

    public MediaSessionConnectionOperator(Context context) {
        Log.i(TAG, "初始化中");
//        mediaBrowserCompat = new MediaBrowserCompat(context,
//                new ComponentName(context, MediaPlaybackService.class),
//                connectionCallbacks,
//                null);
        this.context = context;

        serviceReady = false;
        connecting = false;
        musicServiceIntent = new Intent(context, MediaPlaybackService.class);
    }

    public static MediaSessionConnectionOperator getInstance(Context context) {

        if (context!=null&&mediaSessionConnectionOperator == null) {
            //init
            Log.i(TAG, "初始化");
            mediaSessionConnectionOperator = new MediaSessionConnectionOperator(context);
        }
        return mediaSessionConnectionOperator;
    }

    public void connect() {
        if (connecting) {
            return;
        }
        if (serviceReady) {
            return;
        }
        Log.i("asdaksdjkasd","connecting:"+connecting+" ready:"+serviceReady);
//        Log.i("asdaksdjkasd",connecting+"");
        connecting = true;
        context.startForegroundService(musicServiceIntent);
    }

    public void disconnect() {
        try {
            Log.i(TAG, "disconnect: 杀进程");
            connecting =true;
            context.stopService(musicServiceIntent);
//            serviceReady = false;
            mediaSessionConnectionOperator = null;
        } catch (Exception e) {

        }
    }



    public void set_Connecting(boolean connecting)
    {
        this.connecting = connecting;
    }

    public void set_serviceReady(boolean serviceReady)
    {
        this.serviceReady = serviceReady;
    }


//    public void showNotification()
//    {
    // Get the session's metadata
//        MediaControllerCompat controller = mediaSession.getController();
//        MediaMetadataCompat mediaMetadata = controller.getMetadata();
//        MediaDescriptionCompat description = mediaMetadata.getDescription();
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
//
//        builder
//                // Add the metadata for the currently playing track
//                .setContentTitle(description.getTitle())
//                .setContentText(description.getSubtitle())
//                .setSubText(description.getDescription())
//                .setLargeIcon(description.getIconBitmap())
//
//                // Enable launching the player by clicking the notification
//                .setContentIntent(controller.getSessionActivity())
//
//                // Stop the service when the notification is swiped away
//                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context,
//                        PlaybackStateCompat.ACTION_STOP))
//
//                // Make the transport controls visible on the lockscreen
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//
//                // Add an app icon and set its accent color
//                // Be careful about the color
//                .setSmallIcon(R.drawable.notification_icon)
//                .setColor(ContextCompat.getColor(context, R.color.primaryDark))
//
//                // Add a pause button
//                .addAction(new NotificationCompat.Action(
//                        R.drawable.pause, getString(R.string.pause),
//                        MediaButtonReceiver.buildMediaButtonPendingIntent(context,
//                                PlaybackStateCompat.ACTION_PLAY_PAUSE)))
//
//                // Take advantage of MediaStyle features
//                .setStyle(new MediaStyle()
//                        .setMediaSession(mediaSession.getSessionToken())
//                        .setShowActionsInCompactView(0)
//
//                        // Add a cancel button
//                        .setShowCancelButton(true)
//                        .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context,
//                                PlaybackStateCompat.ACTION_STOP)));
//
//        // Display the notification and place the service in the foreground
//        startForeground(id, builder.build());
//    }
}
