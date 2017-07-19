package com.mistry.mithun.autosilent;

/**
 * Created by Mithun on 7/18/2017.
 */

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final String CONTACTS_TABLE_NAME = "schedules";
    private static final String CONTACTS_COLUMN_NAME = "schedule_name";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table schedules " +
                        "(id integer primary key, schedule_name text not null,monday text,tuesday text, wednesday text,thursday text,friday text,saturday text,sunday text, active boolean not null)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS schedules");
        onCreate(db);
    }

    public boolean insertSchedule (String schedule_name, String monday, String tuesday, String wednesday,String thursday, String friday, String saturday, String sunday, Boolean active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("schedule_name", schedule_name);
        contentValues.put("monday", monday);
        contentValues.put("tuesday", tuesday);
        contentValues.put("wednesday", wednesday);
        contentValues.put("thursday", thursday);
        contentValues.put("friday", friday);
        contentValues.put("saturday", saturday);
        contentValues.put("sunday", sunday);
        contentValues.put("active", active);
        db.insert("schedules", null, contentValues);
        return true;
    }

    public Cursor getIndividualData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from schedules where id="+id+"", null );
        res.close();
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean scheduleNameChecker(String schedule_name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from schedules where schedule_name = ?", new String[]{schedule_name}, null );
        return (res.getCount() == 0);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateSchedule (Integer id, String schedule_name, String monday, String tuesday, String wednesday,String thursday, String friday, String saturday, String sunday, Boolean active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("schedule_name", schedule_name);
        contentValues.put("monday", monday);
        contentValues.put("tuesday", tuesday);
        contentValues.put("wednesday", wednesday);
        contentValues.put("thursday", thursday);
        contentValues.put("friday", friday);
        contentValues.put("saturday", saturday);
        contentValues.put("sunday", sunday);
        contentValues.put("active", active);
        db.update("schedules", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteSchedule (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("schedules",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllSchedules() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from schedules", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}
