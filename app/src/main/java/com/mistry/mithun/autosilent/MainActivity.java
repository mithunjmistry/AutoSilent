package com.mistry.mithun.autosilent;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void set_schedule(View view){
        Intent intent = new Intent(this, SetScheduleActivity.class);
        intent.putExtra("previousIntent", "mainview");
        startActivity(intent);
    }

    public void view_schedule(View view){
        Intent intent = new Intent(this, ViewScheduleActivity.class);
        startActivity(intent);
    }

    public void test(View view){
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    public void one_time_activity(View view){
        Intent intent = new Intent(this, OneTimeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
