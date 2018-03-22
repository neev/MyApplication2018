package com.neeruap.myapplication2018;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ScreenOnOff_Service extends Service {

    BroadcastReceiver mReceiver=null;
    private static final String TAG = "ScreenOnOff_Service";
    private static final int FOREGROUND_NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        // Toast.makeText(getBaseContext(), "Service on create", Toast.LENGTH_SHORT).show();

        // Register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOnOff_Receiver();
        registerReceiver(mReceiver, filter);



    }



    public ScreenOnOff_Service() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean screenOn = false;

        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOn = intent.getBooleanExtra("screen_state", false);
            goForeground();

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
     * Move service to the foreground, to avoid execution limits on background processes.
     *
     * Callers should call stopForeground(true) when background work is complete.
     */
    private void goForeground() {
        Intent notificationIntent = new Intent(this, HomeMapsActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification n = new Notification.Builder(this)
                .setContentTitle("Advertising device via Bluetooth")
                .setContentText("This device is discoverable to others nearby.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(FOREGROUND_NOTIFICATION_ID, n);
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
