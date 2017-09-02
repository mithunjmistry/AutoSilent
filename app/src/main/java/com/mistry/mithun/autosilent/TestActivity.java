package com.mistry.mithun.autosilent;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

public class TestActivity extends AppCompatActivity{

    EditText hours, minutes;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);



        hours = (EditText)findViewById(R.id.hour);
        minutes = (EditText)findViewById(R.id.minute);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void mode(View v){
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ModeReceiver.class);
        switch (v.getId()) {

            case R.id.vibrate:
                Log.d("Vibrate", "vibrate");
                intent = new Intent(getApplicationContext(), VibrateReceiver.class);
                break;

            case R.id.silent:
                Log.d("silent", "silent");
                intent = new Intent(getApplicationContext(), ModeReceiver.class);
                break;

            case R.id.ring:
                Log.d("ring", "ring");
                intent = new Intent(getApplicationContext(), RingReceiver.class);
                break;

            default:
                break;
        }
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // calendar.set(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hours.getText().toString()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minutes.getText().toString()));
        calendar.set(Calendar.SECOND, 1);
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    public void cancel_mode(View v){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), VibrateReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

    }
}
