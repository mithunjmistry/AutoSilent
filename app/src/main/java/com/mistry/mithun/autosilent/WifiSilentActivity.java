package com.mistry.mithun.autosilent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WifiSilentActivity extends AppCompatActivity {

    Spinner homeNetwork, workNetwork, wifimode;
    Button activation_button;
    SharedPreferences sharedPref;
    String work_wifi = null, home_wifi = null;
    Integer home_network_selected = null, work_network_selected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_silent);

        homeNetwork = (Spinner)findViewById(R.id.homeNetwork);
        workNetwork = (Spinner)findViewById(R.id.workNetwork);
        wifimode = (Spinner)findViewById(R.id.modewifi);
        activation_button = (Button)findViewById(R.id.activation_button_wifi);
        activation_button.setTextColor(Color.GREEN);

        WifiManager mainWifiObj;
        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        List<WifiConfiguration> stored_wifi = mainWifiObj.getConfiguredNetworks();
        List<String>  stored_wifi_ssid = new ArrayList<>();

        sharedPref = this.getSharedPreferences("wifiSilent", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Boolean activated = sharedPref.getBoolean("wifi_activated", false);
        if(activated){
            home_wifi = sharedPref.getString("home_wifi", "none");
            work_wifi = sharedPref.getString("work_wifi", "none");
            if(!stored_wifi.isEmpty()){
                for(int i = 0; i < stored_wifi.size(); i++){
                    String ssid = stored_wifi.get(i).SSID;
                    stored_wifi_ssid.add(ssid.substring(1, ssid.length()-1));
                    if(ssid.substring(1, ssid.length()-1).equalsIgnoreCase(home_wifi)){
                        home_network_selected = i;
                    }
                    if(ssid.substring(1, ssid.length()-1).equalsIgnoreCase(work_wifi)){
                        work_network_selected = i;
                    }
                }
            }
        }
        else{
            activation_button.setTextColor(Color.RED);
            activation_button.setText("Deactivated");
            if(!stored_wifi.isEmpty()){
                for(int i = 0; i < stored_wifi.size(); i++){
                    String ssid = stored_wifi.get(i).SSID;
                    stored_wifi_ssid.add(ssid.substring(1, ssid.length()-1));
                }
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stored_wifi_ssid);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeNetwork.setAdapter(dataAdapter);
        workNetwork.setAdapter(dataAdapter);
        if(work_network_selected != null && home_network_selected != null){
            homeNetwork.setSelection(home_network_selected);
            workNetwork.setSelection(work_network_selected);
        }
        else{
            editor.putBoolean("wifi_activated", false);
            editor.apply();
            activation_button.setTextColor(Color.RED);
            activation_button.setText("Deactivated");
        }

    }

    public void activationwifi(View v){
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

    public void savewifi(View view){
        String home_net = homeNetwork.getSelectedItem().toString();
        String work_net = workNetwork.getSelectedItem().toString();
        String mode = wifimode.getSelectedItem().toString();
        if(home_net.equals(work_net)){
            Toast.makeText(this, "Home and Work WiFi cannot be same.", Toast.LENGTH_SHORT).show();
            return;
        }
        sharedPref = this.getSharedPreferences("wifiSilent", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("wifi_activated", activation_button.getText().toString().equalsIgnoreCase("activated"));
        editor.putString("home_wifi", home_net);
        editor.putString("work_wifi", work_net);
        editor.putString("wifimode", mode);
        editor.apply();
        Toast.makeText(this, "Saved successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancelwifi(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
