package com.example.watchtime.source.ui.Time;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.watchtime.R;
import com.example.watchtime.resouce.function;
import com.example.watchtime.resouce.global_variable;
import com.example.watchtime.source.Database.DataStore;
import com.example.watchtime.resouce.Object.Timer_data;

public class TimeCountdown extends Service {
    private static final int NOTIF_ID=1;
    private Timer_data data;
    private MediaPlayer mediaPlayer;


    @Override
    public void onCreate() {
        this.startForeground(NOTIF_ID,getMyActivityNotification(""));

        //Log.e("In service","OnCreate");
    }

    public void startTime(){
        new CountDownTimer(data.getData().getTimeLeft(), 1000) {
            public void onTick(long millisUntilFinished) {
                //Update notification

                //Log.e("In service",""+data.second);

                //Update database
                DataStore.getInstance(TimeCountdown.this).timeCountdownQuery().updateTimeLeft((int)millisUntilFinished,data.getData().getID());
                Log.e("InDB",""+DataStore.getInstance(TimeCountdown.this).timeCountdownQuery().getTimeLeft(global_variable.TimerID).getTimeLeft()+"");
                data.CountDown();
                updateNotification(function.Timer.FormatTime(data));
            }

            public void onFinish() {
                alert();
            }

        }.start();
    }

    private void alert() {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ring);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
    }

    private Notification getMyActivityNotification(String text){
        // The PendingIntent to launch our activity if the user selects
        // this notification

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, new Intent(this, global_variable.Timer_activity), 0);

        return new Notification.Builder(this)
                .setContentTitle("Time countdown")
                .setContentText(text)
                .setSmallIcon(R.drawable.img)
                .setContentIntent(contentIntent).getNotification();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.release();
        }
        DataStore.getInstance(TimeCountdown.this).timeCountdownQuery().deleteTimeLeft(data.getData().getID());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateNotification(String text) {
        Notification notification = getMyActivityNotification(text);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        data = (Timer_data) intent.getSerializableExtra("Timer_data");
        mediaPlayer = new MediaPlayer();
        startTime();
        Log.e("In service",data.getData().getTotalTime()+"");

        return super.onStartCommand(intent, flags, startId);

    }


}
