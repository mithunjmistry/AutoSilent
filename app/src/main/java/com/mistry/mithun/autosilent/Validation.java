package com.mistry.mithun.autosilent;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mithun on 7/21/2017.
 */

public class Validation {
    private Context context;
    public Validation(Context context){
        this.context = context;
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
                        Toast.makeText(this.context, "From time cannot be greater than To time.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this.context, "Please select proper time", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(this.context, "From or To time cannot be null for selected day.", Toast.LENGTH_SHORT).show();
        return false;
    }
}
