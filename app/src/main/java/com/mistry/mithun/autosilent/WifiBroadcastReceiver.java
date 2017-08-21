package com.mistry.mithun.autosilent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mithun on 7/30/2017.
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {

    SharedPreferences sharedPref;

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();

//        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//        if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION .equals(action)) {
//            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
//            if (SupplicantState.isValidState(state)
//                    && state == SupplicantState.COMPLETED) {
                Log.d("wifi", "comes here");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                sharedPref = context.getSharedPreferences("wifiSilent", Context.MODE_PRIVATE);
                Boolean activated = sharedPref.getBoolean("wifi_activated", false);
                if(activated){
                    String home_network = sharedPref.getString("home_wifi", "none");
                    String work_network = sharedPref.getString("work_wifi", "none");
                    String connected = checkConnectedToDesiredWifi(context, home_network, work_network);
                    if(connected != null){
                        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                        if(connected.equals(home_network)){
                            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                        else{
                            String mode = sharedPref.getString("wifimode", "Vibrate");
                            if(mode.equalsIgnoreCase("vibrate")){
                                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            }
                            else{
                                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            }
                        }
                    }
                }
            }
        }, 3700);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        sharedPref = context.getSharedPreferences("wifiSilent", Context.MODE_PRIVATE);
//                Boolean activated = sharedPref.getBoolean("wifi_activated", false);
//                if(activated){
//                    String home_network = sharedPref.getString("home_wifi", "none");
//                    String work_network = sharedPref.getString("work_wifi", "none");
//                    String connected = checkConnectedToDesiredWifi(context, home_network, work_network);
//                    if(connected != null){
//                        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
//                        if(connected.equals(home_network)){
//                            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                        }
//                        else{
//                            String mode = sharedPref.getString("wifimode", "Vibrate");
//                            if(mode.equalsIgnoreCase("vibrate")){
//                                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                            }
//                            else{
//                                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                            }
//                        }
//                    }
//                }

//            }
//        }
    }

    /** Detect you are connected to a specific network. */
    private String checkConnectedToDesiredWifi(Context context, String home, String work) {
        String connected = null;

        WifiManager wifiManager =
                (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifi = wifiManager.getConnectionInfo();

        if (wifi != null) {

            String ssid = wifi.getSSID();
            Log.d("wififunction", ssid);
            if(ssid.substring(1,ssid.length()-1).equals(home)){
                connected = home;
            }
            if(ssid.substring(1,ssid.length()-1).equals(work)){
                connected = work;
            }
        }

        return connected;
    }
}
