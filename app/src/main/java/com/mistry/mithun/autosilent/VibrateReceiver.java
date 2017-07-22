package com.mistry.mithun.autosilent;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Mithun on 7/20/2017.
 */

public class VibrateReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {

        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

        int request_code = intent.getIntExtra("request_code", 0);
        if(request_code != 0) {
            AlarmManager alarmMgr;
            PendingIntent alarmIntent;

            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent pintent = new Intent(context, VibrateReceiver.class);
            pintent.putExtra("request_code", request_code);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            int day = calendar.get(Calendar.DAY_OF_WEEK);

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

            calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK));
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, 0);

            calendar.add(Calendar.DATE, 1);

            while (calendar.get(Calendar.DAY_OF_WEEK) != day) {
                calendar.add(Calendar.DATE, 1);
            }

            alarmIntent = PendingIntent.getBroadcast(context, request_code, pintent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    }
}
