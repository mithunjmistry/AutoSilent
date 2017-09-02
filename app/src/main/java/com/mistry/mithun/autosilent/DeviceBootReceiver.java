package com.mistry.mithun.autosilent;

/**
 * Created by Mithun on 7/22/2017.
 */

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

/**
 * @author Nilanchala
 *         <p/>
 *         Broadcast reciever, starts when the device gets starts.
 *         Start your repeating alarm here.
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    private DBHelper mydb;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */
            mydb = new DBHelper(context);
            mydb.restartDeactivation();
//            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//            ArrayList<String> alarm_codes = mydb.getAllAlarmCodes();
//
//            Intent pintent = new Intent(context, VibrateReceiver.class);
//            Intent pintent_ring = new Intent(context, RingReceiver.class);
//            pintent_ring.putExtra("request_code", Integer.parseInt(alarm_codes.get(1)));
//
//            for(String s : alarm_codes){
//                String[] codes = s.trim().split(" ");
//                for(int i = 0; i < codes.length; i+=2){
//                    int request_code_v = Integer.parseInt(codes[i]);
//                    int request_code_r = Integer.parseInt(codes[i+1]);
//
//                    pintent.putExtra("request_code", request_code_v);
//                    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, request_code_v, pintent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmIntent);
//
//                    pintent_ring.putExtra("request_code", request_code_r);
//                    alarmIntent = PendingIntent.getBroadcast(context, request_code_r, pintent_ring, PendingIntent.FLAG_UPDATE_CURRENT);
//                    alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmIntent);
//                }
//            }
        }
    }
}
