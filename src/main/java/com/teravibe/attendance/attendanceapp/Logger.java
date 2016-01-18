package com.teravibe.attendance.attendanceapp;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class Logger extends AppCompatActivity {
    TelephonyManager telephonyManager;
    DAOlogs daoLogs;
    String android_id;
    String uniqueId;
    LogAdapter logAdapter;
    ListView logsListVIew;
    Button loggedBtn;

    String[] date;
    String[] timeIn;
    String[] timeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logger);
        daoLogs = new DAOlogs(getApplicationContext());
        getApplicationContext();
        telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        uniqueId = telephonyManager.getDeviceId() + "-" + android_id;
        logsListVIew = (ListView) this.findViewById(R.id.logsContainer);
        loggedBtn = (Button) this.findViewById(R.id.loggedBtn);

        switch (Constants.STATUS.valueOf(checkStatus())) {
            case None:
                loggedBtn.setText(R.string.login_string);
                break;
            case LoggedIn:
                loggedBtn.setText(R.string.logout_string);
                break;
            case LoggedOut:
                loggedBtn.setText(R.string.login_string);
                break;
        }
        loggedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        getLogs(getApplicationContext());
    }



    private void getLogs(Context context){

        List<ModelLogs> collectionLogs =  daoLogs.getAllLogs();
        date = new String[collectionLogs.size()];
        timeIn = new String[collectionLogs.size()];
        timeOut = new String[collectionLogs.size()];

        int index = 0;
        for (ModelLogs newLogs : collectionLogs) {
            date[index] = newLogs.getDate();
            timeIn[index] = newLogs.getTime_in();
            timeOut[index] = newLogs.getTime_out();
            index++;
        }
        logAdapter = new LogAdapter(context,date, timeIn, timeOut);
        logsListVIew.setAdapter(logAdapter);
        logAdapter.notifyDataSetChanged();

    }
    private String checkStatus() {
        List<ModelLogs> specificLog = daoLogs.checkCurrentDateLogs(Constants.dateFormat.format(new Date()));
            if (specificLog.size() > 0) {
                return specificLog.get(0).getStatus();
            } else {
                return "None";
            }
    }

    private void Login(){

        switch (Constants.STATUS.valueOf(checkStatus())) {
            case None:
                ModelLogs modelLogs = new ModelLogs();
                modelLogs.setUid(uniqueId);
                modelLogs.setDate(Constants.dateFormat.format(new Date()));
                modelLogs.setTime_in(Constants.timeFormat.format(new Date()));
                modelLogs.setLongitude("");
                modelLogs.setLatitude("");
                modelLogs.setStatus(Constants.STATUS.LoggedIn.toString());
                daoLogs.createAttendanceLog(modelLogs);
                Toast.makeText(getApplicationContext(),"You are Logged In",Toast.LENGTH_LONG).show();
                getLogs(getApplicationContext());
                loggedBtn.setText(R.string.logout_string);
                break;
            case LoggedIn:
                daoLogs.updateTimeOut(Constants.dateFormat.format(new Date()), Constants.timeFormat.format(new Date()));
                Toast.makeText(getApplicationContext(),"You are Logged Out", Toast.LENGTH_LONG).show();
                getLogs(getApplicationContext());
                loggedBtn.setText(R.string.login_string);
                break;
            case LoggedOut:
                Toast.makeText(getApplicationContext(),"You're already logged in today",Toast.LENGTH_LONG).show();
                break;
        }
        //Constants.dateFormat.format(new Date());
        //Constants.timeFormat.format(new Date());
        //telephonyManager.getDeviceId();
       // Log.d("UID", android_id);
    }
}

class LogAdapter extends ArrayAdapter<String> {
    Context context;
    String date[];
    String timein[];
    String timeout[];

    ViewHolder viewHolder;
    static class ViewHolder{
        TextView dateTextView;
        TextView timeInTextView;
        TextView timeOutTextView;

    }
    public LogAdapter(Context context, String date[], String timein[], String timeout[]) {
        super(context, R.layout.log_layout, R.id.loggedItem, date);
        this.context = context;
        this.date = date;
        this.timein = timein;
        this.timeout = timeout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.log_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateText);
            viewHolder.timeInTextView = (TextView) convertView.findViewById(R.id.timeInText);
            viewHolder.timeOutTextView = (TextView) convertView.findViewById(R.id.timeOutText);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.dateTextView.setText(this.date[position]);
        viewHolder.timeInTextView.setText(this.timein[position]);
        viewHolder.timeOutTextView.setText(this.timeout[position]);


        return convertView;
    }
}

