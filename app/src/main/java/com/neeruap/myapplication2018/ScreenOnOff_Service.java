package com.neeruap.myapplication2018;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ScreenOnOff_Service extends Service {

    BroadcastReceiver mReceiver=null;
    private static final String TAG = "ScreenOnOff_Service";
    private static final int FOREGROUND_NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";

    @Override
    public void onCreate() {
        super.onCreate();

        // Toast.makeText(getBaseContext(), "Service on create", Toast.LENGTH_SHORT).show();

        // Register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOnOff_Receiver();
        registerReceiver(mReceiver, filter);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

    }



    public ScreenOnOff_Service() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean screenOn = false;

        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOn = intent.getBooleanExtra("screen_state", false);

                Log.i(TAG, "Starting foreground service");


                startForeground(FOREGROUND_NOTIFICATION_ID, getNotification());

        }catch(Exception e){}

        //  Toast.makeText(getBaseContext(), "Service on start :"+screenOn,
        //Toast.LENGTH_SHORT).show();

        if (!screenOn) {

            // your code here
            // Some time required to start any service
            Toast.makeText(getBaseContext(), "Screen on, ", Toast.LENGTH_LONG).show();
            Log.i(TAG,"Screen ON");

        } else {

            // your code here
            // Some time required to stop any service to save battery consumption
            Toast.makeText(getBaseContext(), "Screen off,", Toast.LENGTH_LONG).show();

            Log.i(TAG,"Screen OFF");
        }

        return START_STICKY;
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent(this, ScreenOnOff_Service.class);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_media_play_dark, getString(R.string.option_get_place),
                        servicePendingIntent)
                .setContentText("screen state")

                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)

                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onDestroy() {

        Log.i("ScreenOnOff", "Service  destroy");
        if(mReceiver!=null)
            unregisterReceiver(mReceiver);
        stopForeground(true);
        super.onDestroy();

    }
}
