package com.example.watchtime.source.ui.Time;

import android.app.IntentService;
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
    private CountDownTimer onTick;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //
        data = (Timer_data) intent.getSerializableExtra("Timer_data");
        Log.e("In handle intent",data.getData().getTimeLeft()+"");
        mediaPlayer = new MediaPlayer();

        startTime();

        Log.e("In service",data.getData().getTotalTime()+"");

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

        Log.e("In service","OnCreate");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        onTick.cancel();
        Log.e("In service","OnDestroy");
        if(mediaPlayer.isPlaying()){
            mediaPlayer.release();
        }
        //DataStore.getInstance(TimeCountdown.this).timeCountdownQuery().deleteTimeLeft(data.getData().getID());
    }


    //

    public void startTime(){
        onTick = new CountDownTimer(data.getData().getTimeLeft(), 1000) {
            public void onTick(long millisUntilFinished) {
                //Update notification
                String time = function.Timer.FormatTime(data);

                //Log.e("In service",""+data.second);
                Intent intent1 = new Intent();
                intent1.setAction("com.example.watchtime.source.ui.Time");
                intent1.putExtra(global_variable.BroadcaseIntentName,time );
                sendBroadcast(intent1);
                Log.e("In handle intent",time);
                updateNotification(time);
                //Update database
                //DataStore.getInstance(TimeCountdown.this).timeCountdownQuery().updateTimeLeft((int)millisUntilFinished,data.getData().getID());
                //Log.e("InDB",""+DataStore.getInstance(TimeCountdown.this).timeCountdownQuery().getTimeLeft(global_variable.TimerID).getTimeLeft()+"");
                data.CountDown();

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
    //-------------------------------NOTIFICATION--------------------------------------
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

    private void updateNotification(String text) {
        Notification notification = getMyActivityNotification(text);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID, notification);
    }





}
