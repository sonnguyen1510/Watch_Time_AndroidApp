package com.example.watchtime.source.UIFunction.alarm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.watchtime.source.Database.Alarm.Alarm;
import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.Object.AlarmList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmProcess extends Service{
    public AlarmList data;
    private MediaPlayer mediaPlayer;
    private Alarm nextAlarm;

    BroadcastReceiver getNewAlarm = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Alarm newAlarmdata = (Alarm) intent.getSerializableExtra(global_variable.AlarmData);
            String action = intent.getStringExtra("Action");
            if(action.equalsIgnoreCase("AddAlarm")){
                if(newAlarmdata != null){
                    data.addNewAlarm(newAlarmdata);
                }
            }else if(action.equalsIgnoreCase("UpdateState")){
                //int ID = (Alarm) intent.getSerializableExtra(global_variable.AlarmData);
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter TimerintentFilter = new IntentFilter();
        TimerintentFilter.addAction("com.example.watchtime.source.ui.Alarm");
        registerReceiver(getNewAlarm, TimerintentFilter);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Run alarm
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Date currentTime = Calendar.getInstance().getTime();
                if(currentTime.getHours() == nextAlarm.getHours() && currentTime.getMinutes() == nextAlarm.getMinutes()){
                    if(nextAlarm.isActive()) {
                        //play alert song
                        if (!mediaPlayer.isPlaying()) {
                            alert(nextAlarm.getAlertsong());
                        }
                        //if
                    }
                    else {
                        nextAlarm = data.getClosestAlarmHigh(currentTime.getHours(),currentTime.getMinutes());
                    }
                }else{
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                }

            }
        },0,1000);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void alert(int alertsong) {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), alertsong);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }
}
