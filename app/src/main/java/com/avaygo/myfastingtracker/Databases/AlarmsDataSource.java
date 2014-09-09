package com.avaygo.myfastingtracker.Databases;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.avaygo.myfastingtracker.Adapters.cReminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ash on 18/08/2014.
 *
 * Class modifies data in the Alarms database and queries the database to return the results to the UI
 *
 */
public class AlarmsDataSource {

    private SQLiteDatabase database;
    private ReminderAlarmsHelper dbHelper;

    //Constructor
    public AlarmsDataSource(Context context){
        dbHelper = new ReminderAlarmsHelper(context);
    }

    //Opens a writable database
    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    /**
    Initialises the alarm list for first time usage.
        @param  day is the day of the alarm
        @param  start is the time the  alarm is set for
        @param duration is how long between the start and the end
        @param  end is the time the fast is due to end at
    */
    public void initialiseAlarms(String day, Calendar start, int duration, Calendar end){

        ContentValues values = new ContentValues();
        values.put(ReminderAlarmsHelper.COLUMN_DAY, day);
        values.put(ReminderAlarmsHelper.COLUMN_START, start.getTimeInMillis());
        values.put(ReminderAlarmsHelper.COLUMN_END, end.getTimeInMillis());
        values.put(ReminderAlarmsHelper.COLUMN_LENGTH, duration);
        values.put(ReminderAlarmsHelper.COLUMN_ENABLED, 0);

        database.insert(ReminderAlarmsHelper.TABLE_NAME, null, values);
    }

    //CRUD Operations:
    public cReminder getAlarm(int id){

        return null;
    }

    /**
     * Returns all reminders from the database in an array list.
     *
     * Takes no params.
     *
     * @return a list of <cReminder> objects containing the alarm details
     */
    public List <cReminder> getAllAlarms(){

        List <cReminder> reminderList = new ArrayList<cReminder>();
        String selectQuery = "SELECT * FROM " + ReminderAlarmsHelper.TABLE_NAME;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                cReminder reminder = new cReminder();
                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();

                reminder.set_id(cursor.getInt(0));
                reminder.setDayName(cursor.getString(1));

                calendar1.setTimeInMillis(cursor.getLong(2));
                reminder.setStartTime(calendar1);

                calendar2.setTimeInMillis(cursor.getLong(3));
                reminder.setEndTime(calendar2);

                reminder.setFastLength(cursor.getInt(4));
                reminder.setEnabled(cursor.getInt(5));

                reminderList.add(reminder);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return reminderList;
    }

    /**
     *  Gets the number of alarms that have been set.
     *
     * @return an integer that holds how many alarms have been set
     */
    public int getAlarmsCount(){

        int count;

        String countQuery = "SELECT  * FROM " + ReminderAlarmsHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    /**
     * Updates the alarm from the provided ID.
     *
     * @param _id is the id of the alarm to be updated
     * @param startTime is the new start time for the alarm
     * @param duration is the new duration for the alarm
     * @param endTime is the new end time for the alarm
     * @return
     */
    public int updateAlarm(long _id, Calendar startTime, int duration, Calendar endTime){

        //Content values puts the new data in the given columns
        ContentValues values = new ContentValues();
        values.put(ReminderAlarmsHelper.COLUMN_START, startTime.getTimeInMillis());
        values.put(ReminderAlarmsHelper.COLUMN_END, endTime.getTimeInMillis());
        values.put(ReminderAlarmsHelper.COLUMN_LENGTH, duration);

        //Updates the database with the new values
         return database.update(ReminderAlarmsHelper.TABLE_NAME, values,
                 ReminderAlarmsHelper.COLUMN_ID + " =" + _id, null);
    }

    public int getIsEnabled (int _id){

        int isEnabled = 0;

        String selectQuery = "SELECT "+ ReminderAlarmsHelper.COLUMN_ENABLED +" FROM "
                + ReminderAlarmsHelper.TABLE_NAME + " WHERE " + ReminderAlarmsHelper.COLUMN_ID + "=" + _id  ;

        Cursor cursor = database.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {

          isEnabled =  cursor.getInt(0);

        }

        cursor.close();

        return  isEnabled;
    }

    public int setIsEnabled (int _id, boolean isEnabled){

        int enabled = (isEnabled) ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(ReminderAlarmsHelper.COLUMN_ENABLED, enabled);

        return database.update(ReminderAlarmsHelper.TABLE_NAME, values,
                ReminderAlarmsHelper.COLUMN_ID + " =" + _id, null);
    }

}
