package com.mistry.mithun.autosilent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SetScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    EditText monday_from, monday_to;
    EditText tuesday_from, tuesday_to;
    EditText wednesday_from, wednesday_to;
    EditText thursday_from, thursday_to;
    EditText friday_from, friday_to;
    EditText saturday_from, saturday_to;
    EditText sunday_from, sunday_to;
    Button activation_button;
    EditText schedule_name_edittext;
    Integer id = 1;
    Boolean mainview = true;
    String pre_existing_alarm_codes = "";
    Boolean previous_active_status = true;

    private DBHelper mydb;
    private AlarmManager alarmMgr;

    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);

        Intent intent = getIntent();
        String last_intent = intent.getStringExtra("previousIntent");

        schedule_name_edittext = (EditText)findViewById(R.id.schedule_name);
        mydb = new DBHelper(this);
        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        activation_button = (Button)findViewById(R.id.activation_button);
        activation_button.setTextColor(Color.GREEN);

        monday = (CheckBox)findViewById(R.id.monday);
        tuesday = (CheckBox)findViewById(R.id.tuesday);
        wednesday = (CheckBox)findViewById(R.id.wednesday);
        thursday = (CheckBox)findViewById(R.id.thursday);
        friday = (CheckBox)findViewById(R.id.friday);
        saturday = (CheckBox)findViewById(R.id.saturday);
        sunday = (CheckBox)findViewById(R.id.sunday);

        monday_from = (EditText) findViewById(R.id.monday_from);
        monday_from.setOnClickListener(this);
        monday_to = (EditText) findViewById(R.id.monday_to);
        monday_to.setOnClickListener(this);

        tuesday_from = (EditText) findViewById(R.id.tuesday_from);
        tuesday_from.setOnClickListener(this);
        tuesday_to = (EditText) findViewById(R.id.tuesday_to);
        tuesday_to.setOnClickListener(this);

        wednesday_from = (EditText) findViewById(R.id.wednesday_from);
        wednesday_from.setOnClickListener(this);
        wednesday_to = (EditText) findViewById(R.id.wednesday_to);
        wednesday_to.setOnClickListener(this);

        thursday_from = (EditText) findViewById(R.id.thursday_from);
        thursday_from.setOnClickListener(this);
        thursday_to = (EditText) findViewById(R.id.thursday_to);
        thursday_to.setOnClickListener(this);

        friday_from = (EditText) findViewById(R.id.friday_from);
        friday_from.setOnClickListener(this);
        friday_to = (EditText) findViewById(R.id.friday_to);
        friday_to.setOnClickListener(this);

        saturday_from = (EditText) findViewById(R.id.saturday_from);
        saturday_from.setOnClickListener(this);
        saturday_to = (EditText) findViewById(R.id.saturday_to);
        saturday_to.setOnClickListener(this);

        sunday_from = (EditText) findViewById(R.id.sunday_from);
        sunday_from.setOnClickListener(this);
        sunday_to = (EditText) findViewById(R.id.sunday_to);
        sunday_to.setOnClickListener(this);

        if(last_intent.equalsIgnoreCase("listview")){
            mainview = false;
            id = intent.getIntExtra("identification", 1);
            schedule_name_edittext.setFocusable(false);
            Cursor cursor = mydb.getIndividualData(id);
            cursor.moveToFirst();

            String schedule_name_preset = cursor.getString(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_NAME));
            String monday_preset = cursor.getString(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_MONDAY));
            String tuesday_preset = cursor.getString(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_TUESDAY));
            String wednesday_preset = cursor.getString(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_WEDNESDAY));
            String thursday_preset = cursor.getString(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_THURSDAY));
            String friday_preset = cursor.getString(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_FRIDAY));
            String saturday_preset = cursor.getString(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_SATURDAY));
            String sunday_preset = cursor.getString(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_SUNDAY));
            pre_existing_alarm_codes = cursor.getString(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_ALARMCODES));
            int active_preset = cursor.getInt(cursor.getColumnIndex(DBHelper.SCHEDULES_COLUMN_ACTIVE));

            if (!cursor.isClosed())  {
                cursor.close();
            }

            schedule_name_edittext.setText(schedule_name_preset);
            schedule_name_edittext.setEnabled(false);
            ImageButton delete_button = (ImageButton)findViewById(R.id.deleteButton);
            delete_button.setVisibility(View.VISIBLE);

            if(active_preset == 0){
                activation_button.setText("Deactivated");
                activation_button.setTextColor(Color.RED);
                previous_active_status = false;
            }

            if(monday_preset != null && !monday_preset.isEmpty()){
                String[] timings = monday_preset.split(" ");
                monday.setChecked(true);
                monday_from.setText(timings[0]);
                monday_to.setText(timings[1]);
            }

            if(tuesday_preset != null && !tuesday_preset.isEmpty()){
                String[] timings = tuesday_preset.split(" ");
                tuesday.setChecked(true);
                tuesday_from.setText(timings[0]);
                tuesday_to.setText(timings[1]);
            }

            if(wednesday_preset != null && !wednesday_preset.isEmpty()){
                String[] timings = wednesday_preset.split(" ");
                wednesday.setChecked(true);
                wednesday_from.setText(timings[0]);
                wednesday_to.setText(timings[1]);
            }

            if(thursday_preset != null && !thursday_preset.isEmpty()){
                String[] timings = thursday_preset.split(" ");
                thursday.setChecked(true);
                thursday_from.setText(timings[0]);
                thursday_to.setText(timings[1]);
            }

            if(friday_preset != null && !friday_preset.isEmpty()){
                String[] timings = friday_preset.split(" ");
                friday.setChecked(true);
                friday_from.setText(timings[0]);
                friday_to.setText(timings[1]);
            }

            if(saturday_preset != null && !saturday_preset.isEmpty()){
                String[] timings = saturday_preset.split(" ");
                saturday.setChecked(true);
                saturday_from.setText(timings[0]);
                saturday_to.setText(timings[1]);
            }

            if(sunday_preset != null && !sunday_preset.isEmpty()){
                String[] timings = sunday_preset.split(" ");
                sunday.setChecked(true);
                sunday_from.setText(timings[0]);
                sunday_to.setText(timings[1]);
            }
        }
        else{
            schedule_name_edittext.requestFocus();
        }

    }

    @Override
    public void onClick(final View v) {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(SetScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                switch (v.getId()) {

                    case R.id.monday_from:
                        monday_from.setText(time);
                        break;

                    case R.id.monday_to:
                        monday_to.setText(time);
                        break;

                    case R.id.tuesday_from:
                        tuesday_from.setText(time);
                        break;

                    case R.id.tuesday_to:
                        tuesday_to.setText(time);
                        break;

                    case R.id.wednesday_from:
                        wednesday_from.setText(time);
                        break;

                    case R.id.wednesday_to:
                        wednesday_to.setText(time);
                        break;

                    case R.id.thursday_from:
                        thursday_from.setText(time);
                        break;

                    case R.id.thursday_to:
                        thursday_to.setText(time);
                        break;

                    case R.id.friday_from:
                        friday_from.setText(time);
                        break;

                    case R.id.friday_to:
                        friday_to.setText(time);
                        break;

                    case R.id.saturday_from:
                        saturday_from.setText(time);
                        break;

                    case R.id.saturday_to:
                        saturday_to.setText(time);
                        break;

                    case R.id.sunday_from:
                        sunday_from.setText(time);
                        break;

                    case R.id.sunday_to:
                        sunday_to.setText(time);
                        break;

                    default:
                        break;
                }
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public void cancel(View v){
        Intent intent = new Intent(this, MainActivity.class);
        if(!mainview){
            intent = new Intent(this, ViewScheduleActivity.class);
        }
        startActivity(intent);
    }

    public void delete(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(
                SetScheduleActivity.this);
        alert.setTitle("Confirm Delete");
        alert.setMessage("Are you sure to delete schedule?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mydb.deleteSchedule(id);
                Intent intent = new Intent(getApplicationContext(), ViewScheduleActivity.class);
                startActivity(intent);
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

    public void activation(View v){
        String activation_button_text = activation_button.getText().toString();
        if(activation_button_text.equalsIgnoreCase("activated")){
            activation_button.setText("Deactivated");
            activation_button.setTextColor(Color.RED);
        }
        else{
            activation_button.setText("Activated");
            activation_button.setTextColor(Color.GREEN);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        if(!mainview){
            intent = new Intent(this, ViewScheduleActivity.class);
        }
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String scheduler(String[] time_to_set_from, String[] time_to_set_to, String day, int last_db_id){

        String to_return = "";
        PendingIntent alarmIntent;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Intent pintent = new Intent(getApplicationContext(), VibrateReceiver.class);
        Intent pintent_ring = new Intent(getApplicationContext(), RingReceiver.class);

        ArrayList<Integer> request_code = requestCodeMaker(last_db_id, day, getString(R.string.vibrate_mode));

        calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(day));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time_to_set_from[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time_to_set_from[1]));
        calendar.set(Calendar.SECOND, 1);

        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), request_code.get(0), pintent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(day));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time_to_set_to[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time_to_set_to[1]));
        calendar.set(Calendar.SECOND, 1);

        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), request_code.get(1), pintent_ring, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        to_return = String.valueOf(request_code.get(0)) + " " + String.valueOf(request_code.get(1)) + " ";
        return to_return;
    }

    public void schedulerDeactivate(String req_code){
        int request_code = Integer.parseInt(req_code);
        PendingIntent alarmIntent;
        Intent pintent = new Intent(getApplicationContext(), VibrateReceiver.class);
        Intent pintent_ring = new Intent(getApplicationContext(), RingReceiver.class);

        if(Character.toString(req_code.trim().charAt(2)).equals(getString(R.string.vibrate_mode))){
            Log.d("Came in if", "deactivate if");
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), request_code, pintent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.cancel(alarmIntent);
        }
        else if(Character.toString(req_code.trim().charAt(2)).equals(getString(R.string.ringer_mode))){
            Log.d("Came in else", "deactivate else");
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), request_code, pintent_ring, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.cancel(alarmIntent);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void save(View view) {
        Boolean time_check;
        Intent intent = new Intent(this, MainActivity.class);
        String schedule_name = schedule_name_edittext.getText().toString().trim();

        if (schedule_name != null && !schedule_name.isEmpty()) {

            Boolean schedule_name_ok = mydb.scheduleNameChecker(schedule_name);
            if(!schedule_name_ok && mainview){
                Toast.makeText(this, "Schedule name already exists. Choose a different name.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!monday.isChecked() && !tuesday.isChecked() && !wednesday.isChecked() && !thursday.isChecked()
                    && !friday.isChecked() && !saturday.isChecked() && !sunday.isChecked()) {
                Toast.makeText(this, "Please select at least one day to save schedule.", Toast.LENGTH_SHORT).show();
                return;
            }

            Boolean active = activation_button.getText().toString().equalsIgnoreCase("activated");

            String monday_db = null;
            String tuesday_db = null;
            String wednesday_db = null;
            String thursday_db = null;
            String friday_db = null;
            String saturday_db = null;
            String sunday_db = null;

            Validation validation = new Validation(this);

            if (monday.isChecked()) {
                time_check = validation.check_from_to(monday_from.getText().toString(), monday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                monday_db = monday_from.getText().toString().trim() + " " + monday_to.getText().toString().trim();
            }
            if (tuesday.isChecked()) {
                time_check = validation.check_from_to(tuesday_from.getText().toString(), tuesday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                tuesday_db = tuesday_from.getText().toString().trim() + " " + tuesday_to.getText().toString().trim();
            }
            if (wednesday.isChecked()) {
                time_check = validation.check_from_to(wednesday_from.getText().toString(), wednesday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                wednesday_db = wednesday_from.getText().toString().trim() + " " + wednesday_to.getText().toString().trim();
            }
            if (thursday.isChecked()) {
                time_check = validation.check_from_to(thursday_from.getText().toString(), thursday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                thursday_db = thursday_from.getText().toString().trim() + " " + thursday_to.getText().toString().trim();
            }
            if (friday.isChecked()) {
                time_check = validation.check_from_to(friday_from.getText().toString(), friday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                friday_db = friday_from.getText().toString().trim() + " " + friday_to.getText().toString().trim();
            }
            if (saturday.isChecked()) {
                time_check = validation.check_from_to(saturday_from.getText().toString(), saturday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                saturday_db = saturday_from.getText().toString().trim() + " " + saturday_to.getText().toString().trim();
            }
            if (sunday.isChecked()) {
                time_check = validation.check_from_to(sunday_from.getText().toString(), sunday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                sunday_db = sunday_from.getText().toString().trim() + " " + sunday_to.getText().toString().trim();
            }

            String alarm_codes = "";

            if(mainview || (!mainview && (active != previous_active_status) && active)){
            // All checks passed, insert the data

                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                int last_db_id = id;

                if(mainview){
                    last_db_id = sharedPref.getInt("id", 1);
                }

                if (active) {

                    if (monday_db != null && !monday_db.isEmpty()) {

                        String request_codes = scheduler(monday_from.getText().toString().split(":"), monday_to.getText().toString().split(":"), getString(R.string.monday_code), last_db_id);
                        alarm_codes += request_codes;
                    }

                    if (tuesday_db != null && !tuesday_db.isEmpty()) {

                        String request_codes = scheduler(tuesday_from.getText().toString().split(":"), tuesday_to.getText().toString().split(":"), getString(R.string.tuesday_code), last_db_id);
                        alarm_codes += request_codes;
                    }

                    if (wednesday_db != null && !wednesday_db.isEmpty()) {

                        String request_codes = scheduler(wednesday_from.getText().toString().split(":"), wednesday_to.getText().toString().split(":"), getString(R.string.wednesday_code), last_db_id);
                        alarm_codes += request_codes;
                    }

                    if (thursday_db != null && !thursday_db.isEmpty()) {

                        String request_codes = scheduler(thursday_from.getText().toString().split(":"), thursday_to.getText().toString().split(":"), getString(R.string.thursday_code), last_db_id);
                        alarm_codes += request_codes;
                    }

                    if (friday_db != null && !friday_db.isEmpty()) {

                        String request_codes = scheduler(friday_from.getText().toString().split(":"), friday_to.getText().toString().split(":"), getString(R.string.friday_code), last_db_id);
                        alarm_codes += request_codes;
                    }

                    if (saturday_db != null && !saturday_db.isEmpty()) {

                        String request_codes = scheduler(saturday_from.getText().toString().split(":"), saturday_to.getText().toString().split(":"), getString(R.string.saturday_code), last_db_id);
                        alarm_codes += request_codes;
                    }

                    if (sunday_db != null && !sunday_db.isEmpty()) {

                        String request_codes = scheduler(sunday_from.getText().toString().split(":"), sunday_to.getText().toString().split(":"), getString(R.string.sunday_code), last_db_id);
                        alarm_codes += request_codes;
                    }

                if(mainview) {

                    mydb.insertSchedule(schedule_name, monday_db, tuesday_db, wednesday_db, thursday_db, friday_db, saturday_db, sunday_db, active, alarm_codes.trim());
                    Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
                    editor.putInt("id", last_db_id + 1);
                    editor.apply();
                    startActivity(intent);
                }
                else{
                    mydb.updateSchedule(id, schedule_name, monday_db, tuesday_db, wednesday_db, thursday_db, friday_db, saturday_db, sunday_db, active, alarm_codes.trim());
                    Toast.makeText(this, "Updated.", Toast.LENGTH_SHORT).show();
                    intent = new Intent(this, ViewScheduleActivity.class);
                    startActivity(intent);
                }
                }
            } else {
                if (active != previous_active_status){
                    // status changed
                    if(!active) {
                        // deactivated now
                        String[] pre_existing_alarm_codes_array = pre_existing_alarm_codes.split(" ");
                        for (String acode_data : pre_existing_alarm_codes_array) {
                            schedulerDeactivate(acode_data);
                        }
                    }
                }
                mydb.updateSchedule(id, schedule_name, monday_db, tuesday_db, wednesday_db, thursday_db, friday_db, saturday_db, sunday_db, active, pre_existing_alarm_codes.trim());
                Toast.makeText(this, "Updated.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ViewScheduleActivity.class);
                startActivity(intent);
            }

        } else {
            Toast.makeText(this, "Please name your schedule.", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Integer> requestCodeMaker(int last_db_id, String day, String mode) {
        ArrayList<Integer> to_return = new ArrayList<>();
        int code_from = 0;
        int code_to = 0;
        code_from = Integer.parseInt(String.valueOf(last_db_id) + day + mode);
        code_to = Integer.parseInt(String.valueOf(last_db_id) + day + getString(R.string.ringer_mode));
        to_return.add(0, code_from);
        to_return.add(1, code_to);
        return to_return;
    }


}
