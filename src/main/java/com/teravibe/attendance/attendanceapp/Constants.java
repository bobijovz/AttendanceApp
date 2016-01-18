package com.teravibe.attendance.attendanceapp;

import java.text.SimpleDateFormat;

/**
 * Created by bobijovz on 1/14/16.
 */
public class Constants {
    public static String TABLE_NAME = "attendance_log";
    public static String COLUMN_NAME_ID = "id";
    public static String COLUMN_NAME_UID = "uid";
    public static String COLUMN_NAME_DATE = "date";
    public static String COLUMN_NAME_TIME_IN = "time_in";
    public static String COLUMN_NAME_TIME_OUT = "time_out";
    public static String COLUMN_NAME_LONGITUDE = "longitude";
    public static String COLUMN_NAME_LATITUDE = "latitude";
    public static String COLUMN_NAME_STATUS = "status";
    public static String CREATE_LOG ="create table "
            + TABLE_NAME + " ("
            + COLUMN_NAME_ID + " integer primary key autoincrement,"
            + COLUMN_NAME_UID + " text not null,"
            + COLUMN_NAME_DATE + " text unique,"
            + COLUMN_NAME_TIME_IN + " text null,"
            + COLUMN_NAME_TIME_OUT + " text null,"
            + COLUMN_NAME_LATITUDE + " text null,"
            + COLUMN_NAME_LONGITUDE + " text null,"
            + COLUMN_NAME_STATUS +" text not null);";

    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public enum STATUS {
        None,LoggedIn,LoggedOut
    };



}
