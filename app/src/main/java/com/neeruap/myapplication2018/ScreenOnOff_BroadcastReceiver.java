package com.neeruap.myapplication2018;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ScreenOnOff_BroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            Log.d(TAG, "Broadcast registered in Manifest--SCREEN OFF");

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            Log.d(TAG, "Broadcast registered in Manifest--SCREEN ON");

        }


    }

}
