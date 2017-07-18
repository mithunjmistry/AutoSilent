package com.mistry.mithun.autosilent;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
    Button can_del_button;

    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);

        can_del_button = (Button)findViewById(R.id.can_del_button);

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

    public void cancel_delete(View v){
        String button_text = can_del_button.getText().toString();
        if(button_text.equalsIgnoreCase("cancel")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
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

    public void save(View view){
        Boolean time_check;
        Intent intent = new Intent(this, MainActivity.class);

        if(monday.isChecked()){
            time_check = check_from_to(monday_from.getText().toString(), monday_to.getText().toString());
            if(time_check){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
        if(tuesday.isChecked()){
            time_check = check_from_to(tuesday_from.getText().toString(), tuesday_to.getText().toString());
            if(time_check){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
        if(wednesday.isChecked()){
            time_check = check_from_to(wednesday_from.getText().toString(), wednesday_to.getText().toString());
            if(time_check){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
        if(thursday.isChecked()){
            time_check = check_from_to(thursday_from.getText().toString(), thursday_to.getText().toString());
            if(time_check){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
        if(friday.isChecked()){
            time_check = check_from_to(friday_from.getText().toString(), friday_to.getText().toString());
            if(time_check){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
        if(saturday.isChecked()){
            time_check = check_from_to(saturday_from.getText().toString(), saturday_to.getText().toString());
            if(time_check){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
        if(sunday.isChecked()){
            time_check = check_from_to(sunday_from.getText().toString(), sunday_to.getText().toString());
            if(time_check){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
        if(!monday.isChecked() && !tuesday.isChecked() && !wednesday.isChecked() && !thursday.isChecked()
                && !friday.isChecked() && !saturday.isChecked() && !sunday.isChecked()){
            Toast.makeText(this, "Please select at least one day to save schedule.", Toast.LENGTH_SHORT).show();
        }
    }


}
