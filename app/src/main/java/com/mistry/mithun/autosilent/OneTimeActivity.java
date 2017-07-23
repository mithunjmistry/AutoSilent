package com.mistry.mithun.autosilent;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class OneTimeActivity extends AppCompatActivity {

    private EditText from, to, date;
    private RadioGroup radioGroup;
    private PendingIntent alarmIntent;
    private Intent intent;
    int year, month, day;
    private SharedPreferences sharedPref;
    Button oneTimeCancel;
    RadioButton silent, vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time);

        sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Boolean one_time_set = sharedPref.getBoolean("one_time_set", false);


        from = (EditText)findViewById(R.id.oneTimeFrom);
        to = (EditText)findViewById(R.id.oneTimeTo);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        silent = (RadioButton)findViewById(R.id.silentRadioButton2);
        vibrate = (RadioButton)findViewById(R.id.vibrateRadioButton);

        date = (EditText)findViewById(R.id.oneTimeDate);
        Calendar calendar = Calendar.getInstance();

        if(one_time_set){
            String from_preset = sharedPref.getString("from_preset", "10:20");
            String to_preset = sharedPref.getString("to_preset", "10:30");
            String date_preset = sharedPref.getString("date_preset", "04/12/2016");
            Boolean vibrate_preset = sharedPref.getBoolean("vibrate_mode", true);
            String[] date_parameters = date_preset.split("/");
            String[] from_parameters = from_preset.split(":");
            month = Integer.parseInt(date_parameters[0]) - 1;
            day = Integer.parseInt(date_parameters[1]);
            year = Integer.parseInt(date_parameters[2]);
            if((day >= calendar.get(Calendar.DAY_OF_MONTH)) && (month >= (calendar.get(Calendar.MONTH))) && (year >= calendar.get(Calendar.YEAR))){
                if((Integer.parseInt(from_parameters[0]) >= calendar.get(Calendar.HOUR_OF_DAY)) && (Integer.parseInt(from_parameters[1]) > calendar.get(Calendar.MINUTE))){
                    from.setText(from_preset);
                    to.setText(to_preset);
                    oneTimeCancel = (Button)findViewById(R.id.cancelOneTime);
                    oneTimeCancel.setVisibility(View.VISIBLE);
                    if(!vibrate_preset){
                        silent.setChecked(true);
                        vibrate.setChecked(false);
                    }
                }
                else{
                    editor.putBoolean("one_time_set", false);
                    editor.apply();
                }
            }
            else{
                editor.putBoolean("one_time_set", false);
                editor.apply();
            }
        }
        else{
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        showDate(year, month+1, day);

        intent = new Intent(getApplicationContext(), VibrateReceiver.class);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                SharedPreferences.Editor editor = sharedPref.edit();
                if(checkedId == R.id.vibrateRadioButton) {
                    intent = new Intent(getApplicationContext(), VibrateReceiver.class);
                }  else {
                    intent = new Intent(getApplicationContext(), ModeReceiver.class);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    year = arg1;
                    month = arg2;
                    day = arg3;
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        date.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
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

    public void cancelOneTime(View view){

        AlertDialog.Builder alert = new AlertDialog.Builder(
                OneTimeActivity.this);
        alert.setTitle("Cancel One Time Schedule");
        alert.setMessage("Are you sure to cancel non-recurring schedule?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AlarmManager alarmMgr;
                alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

                PendingIntent alarmIntent;
                intent = new Intent(getApplicationContext(), VibrateReceiver.class);
                Intent ring_intent = new Intent(getApplicationContext(), RingReceiver.class);

                if(sharedPref.getBoolean("vibrate_mode", true)){
                    alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    vibrate.setChecked(true);
                    silent.setChecked(false);
                }
                else{
                    intent = new Intent(getApplicationContext(), ModeReceiver.class);
                    alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    silent.setChecked(true);
                    vibrate.setChecked(false);
                }
                alarmMgr.cancel(alarmIntent);

                alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 11, ring_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmMgr.cancel(alarmIntent);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("one_time_set", false);
                editor.apply();
                oneTimeCancel.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "One time schedule cancelled successfully.", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void set(View view){
        Intent main = new Intent(this, MainActivity.class);
        Calendar calendar = Calendar.getInstance();
        if((day >= calendar.get(Calendar.DAY_OF_MONTH)) && (month >= (calendar.get(Calendar.MONTH))) && (year >= calendar.get(Calendar.YEAR))) {

        Validation validation = new Validation(getApplicationContext());
        Boolean validation_status = validation.check_from_to(from.getText().toString(), to.getText().toString());
        if(validation_status) {
            String[] from_parameters = from.getText().toString().split(":");
            String[] to_parameters = to.getText().toString().split(":");
            AlarmManager alarmMgr;
            alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
            calendar.set(year, month, day);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(to_parameters[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(to_parameters[1]));
            calendar.set(Calendar.SECOND, 1);
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            sharedPref = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("one_time_set", true);
            editor.putString("from_preset", from.getText().toString());
            editor.putString("to_preset", to.getText().toString());
            editor.putString("date_preset", date.getText().toString());
            editor.putBoolean("vibrate_mode", vibrate.isChecked());

            editor.apply();
            Toast.makeText(getApplicationContext(), "One time schedule saved and is active.", Toast.LENGTH_SHORT).show();
            startActivity(main);
        }
        }
        else{
            Toast.makeText(this, "You cannot set a schedule in past.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
}
