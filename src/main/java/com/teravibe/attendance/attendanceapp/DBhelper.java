package com.teravibe.attendance.attendanceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bobijovz on 1/14/16.
 */
public class DBhelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "attendance.db";
    public static final int DATABASE_VERSION = 1;

    //constructor
    public DBhelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_LOG);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);
    }
}

