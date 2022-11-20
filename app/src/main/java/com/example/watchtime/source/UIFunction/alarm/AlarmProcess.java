package com.example.watchtime.source.UIFunction.alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.watchtime.R;
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

            String action = intent.getStringExtra(global_variable.AlarmProcessRequest);
            if(action.equalsIgnoreCase("isUpdateAlarm")){
                Date current = Calendar.getInstance().getTime();
                data = (AlarmList) intent.getSerializableExtra("UpdateData");
                if(data.isEmpty()){
                    onDestroy();
                    Log.e("No alarm","");
                }
                else{
                    nextAlarm = data.getClosestAlarmHigh(current.getHours(),current.getMinutes());
                    Log.e("Update alarm","");
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
        if(data != null){
            Date current = Calendar.getInstance().getTime();
            data = (AlarmList) intent.getSerializableExtra("Alarm_data");
            nextAlarm = data.getClosestAlarmHigh(current.getHours(),current.getMinutes());
        }

        //Run alarm
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Date currentTime = Calendar.getInstance().getTime();
                if(!(nextAlarm == null)){
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
                        if(mediaPlayer != null){
                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                        }
                    }
                }

            }
        },0,1000);

        return super.onStartCommand(intent, flags, startId);
    }
    //-------------------------------NOTIFICATION--------------------------------------
    private Notification getMyActivityNotification(String text){
        // The PendingIntent to launch our activity if the user selects
        // this notification

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, new Intent(this, global_variable.Timer_activity), 0);

        PendingIntent stopTimer = PendingIntent.getService(this,0,new Intent(this,global_variable.TimerService),0 );

        return new Notification.Builder(this)
                .setContentTitle("Alarm")
                .setContentText(text)
                .setSmallIcon(R.drawable.img)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.ic_baseline_timer_red, "Stop",stopTimer)
                .getNotification()
                ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Alarm","Destroy");
    }
    private void alert(int alertsong) {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), alertsong);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }
}
