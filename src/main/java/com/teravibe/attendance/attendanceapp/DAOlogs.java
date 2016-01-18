package com.teravibe.attendance.attendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobijovz on 1/14/16.
 */
public class DAOlogs {
    private SQLiteDatabase database;
    private DBhelper dbHelper;
    private String[] allColumns = {
            Constants.COLUMN_NAME_ID,
            Constants.COLUMN_NAME_UID,
            Constants.COLUMN_NAME_DATE,
            Constants.COLUMN_NAME_TIME_IN,
            Constants.COLUMN_NAME_TIME_OUT,
            Constants.COLUMN_NAME_LONGITUDE,
            Constants.COLUMN_NAME_LATITUDE,
            Constants.COLUMN_NAME_STATUS
    };
    public DAOlogs(Context context)
    {
        dbHelper = new DBhelper(context);
    }

    public void open()
    {
        database = dbHelper.getReadableDatabase();
    }
    public void close()
    {
        dbHelper.close();
    }

    public ModelLogs createAttendanceLog(ModelLogs modelLogs){

        open();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_UID,modelLogs.getUid());
        values.put(Constants.COLUMN_NAME_DATE,modelLogs.getDate());
        values.put(Constants.COLUMN_NAME_TIME_IN,modelLogs.getTime_in());
        values.put(Constants.COLUMN_NAME_TIME_OUT,modelLogs.getTime_out());
        values.put(Constants.COLUMN_NAME_LONGITUDE,modelLogs.getLongitude());
        values.put(Constants.COLUMN_NAME_LATITUDE,modelLogs.getLatitude());
        values.put(Constants.COLUMN_NAME_STATUS,modelLogs.getStatus());

        long insertId = database.insert(Constants.TABLE_NAME, null, values);
        Cursor cursor = database.query(Constants.TABLE_NAME,allColumns,Constants.COLUMN_NAME_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        ModelLogs newLog = cursorToSettings(cursor);
        cursor.close();
        close();
        return newLog;
    }

    public List<ModelLogs> getAllLogs()
    {
        open();
        List<ModelLogs> collectionLogs = new ArrayList<>();
        Cursor cursor = database.query(Constants.TABLE_NAME, allColumns, null, null, null, null, Constants.COLUMN_NAME_DATE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            ModelLogs logs = cursorToSettings(cursor);
            collectionLogs.add(logs);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return collectionLogs;
    }

    public List<ModelLogs> checkCurrentDateLogs(String date)
    {
        open();
        List<ModelLogs> collectionLogs = new ArrayList<>();
        String where = Constants.COLUMN_NAME_DATE + "= ? and "+ Constants.COLUMN_NAME_STATUS + " IS NOT NULL";
        String[] whereArgs = {date};
        Cursor cursor = database.query(Constants.TABLE_NAME, allColumns, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            ModelLogs logs = cursorToSettings(cursor);
            collectionLogs.add(logs);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return collectionLogs;
    }

    public void updateTimeOut(String date, String timeOut){
        ContentValues values = new ContentValues();
        open();
        String where = Constants.COLUMN_NAME_DATE + "= ? and "+ Constants.COLUMN_NAME_STATUS + " = '" + Constants.STATUS.LoggedIn + "'";
        String[] whereArgs = {date};
        values.put(Constants.COLUMN_NAME_TIME_OUT, timeOut);
        values.put(Constants.COLUMN_NAME_STATUS, Constants.STATUS.LoggedOut.toString());
        database.update(Constants.TABLE_NAME, values, where, whereArgs);
        close();
    }


    private ModelLogs cursorToSettings(Cursor cursor)
    {
        ModelLogs log = new ModelLogs();
        log.setId(cursor.getLong(0));
        log.setUid(cursor.getString(1));
        log.setDate(cursor.getString(2));
        log.setTime_in(cursor.getString(3));
        log.setTime_out(cursor.getString(4));
        log.setLongitude(cursor.getString(5));
        log.setLatitude(cursor.getString(6));
        log.setStatus(cursor.getString(7));
        return log;
    }
}
