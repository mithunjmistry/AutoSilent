package com.mistry.mithun.autosilent;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class OneTimeActivity extends AppCompatActivity {

    private EditText from, to;
    private RadioGroup radioGroup;
    private PendingIntent alarmIntent;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time);

        from = (EditText)findViewById(R.id.oneTimeFrom);
        to = (EditText)findViewById(R.id.oneTimeTo);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        intent = new Intent(getApplicationContext(), VibrateReceiver.class);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.vibrateRadioButton) {
                    intent = new Intent(getApplicationContext(), VibrateReceiver.class);
                }  else {
                    intent = new Intent(getApplicationContext(), ModeReceiver.class);
                }
            }
        });
    }

    public void timePicker(final View v){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(OneTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                switch (v.getId()) {

                    case R.id.oneTimeFrom:
                        from.setText(time);
                        break;

                    case R.id.oneTimeTo:
                        to.setText(time);
                        break;

                    default:
                        break;
                }
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void set(View view){
        Intent main = new Intent(this, MainActivity.class);
        Validation validation = new Validation(getApplicationContext());
        Boolean validation_status = validation.check_from_to(from.getText().toString(), to.getText().toString());
        if(validation_status){
            String[] from_parameters = from.getText().toString().split(":");
            String[] to_parameters = to.getText().toString().split(":");
            AlarmManager alarmMgr;
            alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            // calendar.set(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(from_parameters[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(from_parameters[1]));
            calendar.set(Calendar.SECOND, 1);
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

            Intent ring_intent = new Intent(getApplicationContext(), RingReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 11, ring_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            calendar.setTimeInMillis(System.currentTimeMillis());
            // calendar.set(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(to_parameters[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(to_parameters[1]));
            calendar.set(Calendar.SECOND, 1);
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            Toast.makeText(getApplicationContext(), "One time schedule saved and is active.", Toast.LENGTH_SHORT).show();
            startActivity(main);
        }
    }

    @Override
    public void onBackPressed(){
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
}
