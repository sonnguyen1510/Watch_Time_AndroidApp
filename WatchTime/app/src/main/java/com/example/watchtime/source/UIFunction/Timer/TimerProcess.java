package com.example.watchtime.source.UIFunction.Timer;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import android.os.CountDownTimer;

import android.os.IBinder;


import com.example.watchtime.R;
import com.example.watchtime.source.GlobalData.function;
import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.Object.Timer_data;
import com.example.watchtime.source.Database.DataStore;

public class TimerProcess extends Service {
    private static final int NOTIF_ID=1;
    private Timer_data data;
    private MediaPlayer mediaPlayer;
    private CountDownTimer onTick;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //
        data = (Timer_data) intent.getSerializableExtra("Timer_data");
        //Log.e("In handle intent",data.getData().getTimeLeft()+"");
        mediaPlayer = new MediaPlayer();
        //stopSelf();
        startTime();

        //Log.e("In service",data.getData().getTotalTime()+"");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public void onCreate() {
        this.startForeground(NOTIF_ID,getMyActivityNotification(""));
        //super.onCreate();

        //Log.e("In service","OnCreate");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        onTick.cancel();
        //Log.e("In service","OnDestroy");
        if(mediaPlayer.isPlaying()){
            mediaPlayer.release();
        }
        DataStore.getInstance(TimerProcess.this).timeCountdownQuery().deleteTimeLeft(global_variable.TimerID);
    }


    //

    public void startTime(){
        onTick = new CountDownTimer(data.totalTime, 1000) {
            public void onTick(long millisUntilFinished) {
                data.CountDown();
                String time = function.Timer.FormatTime(data.hour,data.minute,data.second);
                UpdateTimetoUI(time);
                updateNotification(time);
                //Log.e("In handle intent",time);



            }

            public void onFinish() {
                data.CountDown();
                String time = function.Timer.FormatTime(data.hour,data.minute,data.second);
                UpdateTimetoUI(time);
                updateNotification(time);
                alert();
            }

        }.start();
    }

    public void UpdateTimetoUI(String time){
        Intent SendTimeData = new Intent();
        SendTimeData.setAction("com.example.watchtime.source.ui.Time");
        SendTimeData.putExtra(global_variable.TimeData,time );
        sendBroadcast(SendTimeData);
    }

    private void alert() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), data.AlertSong);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }
    //-------------------------------NOTIFICATION--------------------------------------
    private Notification getMyActivityNotification(String text){
        // The PendingIntent to launch our activity if the user selects
        // this notification

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, new Intent(this, global_variable.Timer_activity), 0);

        PendingIntent stopTimer = PendingIntent.getService(this,0,new Intent(this,global_variable.TimerService),0 );

        return new Notification.Builder(this)
                .setContentTitle("Time countdown")
                .setContentText(text)
                .setSmallIcon(R.drawable.img)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.ic_baseline_timer_red, "Stop",stopTimer)
                .getNotification()
                ;
    }

    private void updateNotification(String text) {
        Notification notification = getMyActivityNotification(text);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID, notification);
    }





}
