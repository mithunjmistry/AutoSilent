package com.mistry.mithun.autosilent;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    Integer id = 0;
    Boolean mainview = true;

    private DBHelper mydb;

    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);

        Intent intent = getIntent();
        String last_intent = intent.getStringExtra("previousIntent");

        schedule_name_edittext = (EditText)findViewById(R.id.schedule_name);
        mydb = new DBHelper(this);

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
            }

            if(monday_preset != null && !monday_preset.isEmpty()){
                String[] timings = monday_preset.split(" ");
                monday.setChecked(true);
                monday_from.setText(timings[0]);
                monday_to.setText(timings[1]);
            }

            if(tuesday_preset != null && !tuesday_preset.isEmpty()){
                String[] timings = monday_preset.split(" ");
                tuesday.setChecked(true);
                tuesday_from.setText(timings[0]);
                tuesday_to.setText(timings[1]);
            }

            if(wednesday_preset != null && !wednesday_preset.isEmpty()){
                String[] timings = monday_preset.split(" ");
                wednesday.setChecked(true);
                wednesday_from.setText(timings[0]);
                wednesday_to.setText(timings[1]);
            }

            if(thursday_preset != null && !thursday_preset.isEmpty()){
                String[] timings = monday_preset.split(" ");
                thursday.setChecked(true);
                thursday_from.setText(timings[0]);
                thursday_to.setText(timings[1]);
            }

            if(friday_preset != null && !friday_preset.isEmpty()){
                String[] timings = monday_preset.split(" ");
                friday.setChecked(true);
                friday_from.setText(timings[0]);
                friday_to.setText(timings[1]);
            }

            if(saturday_preset != null && !saturday_preset.isEmpty()){
                String[] timings = monday_preset.split(" ");
                saturday.setChecked(true);
                saturday_from.setText(timings[0]);
                saturday_to.setText(timings[1]);
            }

            if(sunday_preset != null && !sunday_preset.isEmpty()){
                String[] timings = monday_preset.split(" ");
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

                String time = selectedHour + ":" + selectedMinute;
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

    public boolean check_from_to(String from, String to){
        String pattern = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        if((from != null && !from.isEmpty()) && (to != null && !to.isEmpty())){
            Pattern time = Pattern.compile(pattern);
            Matcher matcher_from = time.matcher(from);
            Matcher matcher_to = time.matcher(to);
            // check through regex whether we are getting correct inputs
            if(matcher_from.find() && matcher_to.find()){
                DateFormat sdf = new SimpleDateFormat("hh:mm");
                try {
                    Date time_from = sdf.parse(from);
                    Date time_to = sdf.parse(to);
                    if(time_from.before(time_to)){
                        return true;
                    }
                    else{
                        Toast.makeText(this, "From time cannot be greater than To time.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "Please select proper time", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(this, "From or To time cannot be null for selected day.", Toast.LENGTH_SHORT).show();
        return false;
    }

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

            if (monday.isChecked()) {
                time_check = check_from_to(monday_from.getText().toString(), monday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                monday_db = monday_from.getText().toString().trim() + " " + monday_to.getText().toString().trim();
            }
            if (tuesday.isChecked()) {
                time_check = check_from_to(tuesday_from.getText().toString(), tuesday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                tuesday_db = tuesday_from.getText().toString().trim() + " " + tuesday_to.getText().toString().trim();
            }
            if (wednesday.isChecked()) {
                time_check = check_from_to(wednesday_from.getText().toString(), wednesday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                wednesday_db = wednesday_from.getText().toString().trim() + " " + wednesday_to.getText().toString().trim();
            }
            if (thursday.isChecked()) {
                time_check = check_from_to(thursday_from.getText().toString(), thursday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                thursday_db = thursday_from.getText().toString().trim() + " " + thursday_to.getText().toString().trim();
            }
            if (friday.isChecked()) {
                time_check = check_from_to(friday_from.getText().toString(), friday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                friday_db = friday_from.getText().toString().trim() + " " + friday_to.getText().toString().trim();
            }
            if (saturday.isChecked()) {
                time_check = check_from_to(saturday_from.getText().toString(), saturday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                saturday_db = saturday_from.getText().toString().trim() + " " + saturday_to.getText().toString().trim();
            }
            if (sunday.isChecked()) {
                time_check = check_from_to(sunday_from.getText().toString(), sunday_to.getText().toString());
                if (!time_check) {
                    return;
                }
                sunday_db = sunday_from.getText().toString().trim() + " " + sunday_to.getText().toString().trim();
            }

            // All checks passed, insert the data
            if(mainview) {
                mydb.insertSchedule(schedule_name, monday_db, tuesday_db, wednesday_db, thursday_db, friday_db, saturday_db, sunday_db, active);
                Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else {
                mydb.updateSchedule(id, schedule_name, monday_db, tuesday_db, wednesday_db, thursday_db, friday_db, saturday_db, sunday_db, active);
                Toast.makeText(this, "Updated.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ViewScheduleActivity.class);
                startActivity(intent);
            }

        } else {
            Toast.makeText(this, "Please name your schedule.", Toast.LENGTH_SHORT).show();
        }
    }


}
