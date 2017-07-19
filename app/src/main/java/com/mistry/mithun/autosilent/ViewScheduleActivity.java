package com.mistry.mithun.autosilent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewScheduleActivity extends AppCompatActivity {

    ListView schedule_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        DBHelper mydb;
        mydb = new DBHelper(this);

        schedule_listview = (ListView)findViewById(R.id.schedule_listview);
        ArrayList array_list = mydb.getAllSchedules();
        if(array_list.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    ViewScheduleActivity.this);
            alert.setTitle("No Schedules");
            alert.setMessage("Do you want to make a new schedule?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), SetScheduleActivity.class);
                    intent.putExtra("previousIntent", "mainview");
                    startActivity(intent);
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });

            alert.show();
            return;
        }
        final ArrayList<Integer> all_id = mydb.getAllId();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        TextView textView = new TextView(this);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setText("My schedules");

        schedule_listview.addHeaderView(textView);

        schedule_listview.setAdapter(arrayAdapter);
        schedule_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                int id_selected = all_id.get(arg2-1);
                Intent intent = new Intent(getApplicationContext(), SetScheduleActivity.class);
                intent.putExtra("previousIntent", "listview");
                intent.putExtra("identification", id_selected);
                startActivity(intent);
            }
        });
    }
}
