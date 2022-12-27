package com.example.watchtime.source.UIFunction.alarm;

import android.app.Notification;
import android.app.NotificationManager;
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
import com.example.watchtime.source.GlobalData.function;
import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.Object.AlarmList;
import com.example.watchtime.source.UI.Main;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmProcess extends Service{
    private static final int NOTIF_ID=3;
    public AlarmList data;
    private MediaPlayer mediaPlayer;
    private List<Alarm> nextAlarm;

    BroadcastReceiver getNewAlarm = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getStringExtra(global_variable.AlarmProcessRequest);
            if(action.equalsIgnoreCase("isUpdateAlarm")){
                Date current = Calendar.getInstance().getTime();
                Alarm Alarmdata = (Alarm) intent.getSerializableExtra("UpdateData");
                data.addNewAlarm(Alarmdata);
                if(data.isEmpty()){
                    onDestroy();
                    Log.e("No alarm","");
                }
                else{
                    nextAlarm = data.getClosestAlarmHigh(current.getHours(),current.getMinutes());
                    //Log.e("Update alarm",);
                }


            }else if(action.equalsIgnoreCase("UpdateStatus")){
                //int ID = (Alarm) intent.getSerializableExtra(global_variable.AlarmData);
                boolean Status = intent.getBooleanExtra("Status",true);
                Alarm alarm = (Alarm) intent.getSerializableExtra("Alarm");


            }else if(action.equalsIgnoreCase("Update")){
                Alarm updateData = (Alarm) intent.getSerializableExtra("UpdateData");
                Date current = Calendar.getInstance().getTime();

                if(updateData!= null){
                    data.update(updateData);
                    nextAlarm = data.getClosestAlarmHigh(current.getHours(),current.getMinutes());
                }

            }
            else if(action.equalsIgnoreCase("Delete")){
                List<Alarm> delete = (List<Alarm>) intent.getSerializableExtra("DeleteData");
                for(Alarm i : delete){
                    data.detete(i);
                }
            }
            else if (action.equalsIgnoreCase("DeleteAll")){
                data.deleteAll();
                nextAlarm = null;
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
        this.startForeground(NOTIF_ID,getMyActivityNotification("",""));
        IntentFilter TimerintentFilter = new IntentFilter();
        TimerintentFilter.addAction("com.example.watchtime.source.ui.Alarm");
        registerReceiver(getNewAlarm, TimerintentFilter);
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Date current = Calendar.getInstance().getTime();
        data = (AlarmList) intent.getSerializableExtra("Alarm_data");
        nextAlarm = data.getClosestAlarmHigh(current.getHours(),current.getMinutes());

        Log.e("In Alarm Thread","");
        //Log.e("Next alarm",nextAlarm.getHours()+":"+nextAlarm.getMinutes());
        //Run alarm
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar current = Calendar.getInstance();
                Date currentTime = current.getTime();
                Log.e("In Alarm Thread",currentTime.getMinutes()+"");
                if(!(nextAlarm == null)){
                    for(Alarm i : nextAlarm){
                        if(i.getDayactive().toLowerCase().contains(DayOfWeek().toLowerCase())||i.getDayactive()==""){
                            if(currentTime.getHours() == i.getHours() && currentTime.getMinutes() == i.getMinutes()){
                                if(i.isActive()) {
                                    //play alert song
                                    if(mediaPlayer!= null){
                                        if(!mediaPlayer.isPlaying()){
                                            alert(i.getAlertsong());
                                        }
                                    }
                                    else {
                                        alert(i.getAlertsong());
                                    }
                                    updateNotification(function.Timer.FormatTime(i.getHours(),i.getMinutes())+" Time up!",i.getTittle());
                                    Log.e("Time up","");
                                    //if
                                    nextAlarm = data.getClosestAlarmHigh(currentTime.getHours(),currentTime.getMinutes());
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
                }

            }
        },0,1000);

        return super.onStartCommand(intent, flags, startId);
    }
    //-------------------------------NOTIFICATION--------------------------------------
    private Notification getMyActivityNotification(String text,String title){
        // The PendingIntent to launch our activity if the user selects
        // this notification

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, new Intent(this, Main.class), 0);

        //PendingIntent stopAlarm = PendingIntent.getService(this,0,new Intent(this,global_variable.TimerService),0 );
        Intent intent = new Intent();
        intent.setAction("com.example.watchtime.source.ui.Alarm");
        intent.putExtra(global_variable.AlarmProcessRequest,"Update");
        PendingIntent stopAlarm = PendingIntent.getBroadcast(this, 0, intent,0);


        return new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.img)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.ic_baseline_timer_red, "Stop",stopAlarm)
                .getNotification()
                ;
    }

    public String DayOfWeek(){
        Calendar current = Calendar.getInstance();
        int day = current.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                return "Sunday";
            case Calendar.MONDAY:
                // Current day is Monday
                return "Monday";
            case Calendar.TUESDAY:
                // etc.
                return "Tuesday";
            case Calendar.WEDNESDAY:
                // etc.
                return "Wednesday";
            case Calendar.THURSDAY:
                // etc.
                return "Thursday";
            case Calendar.FRIDAY:
                // etc.
                return "Friday";
            case Calendar.SATURDAY:
                // etc.
                return "Saturday";
            default:
                return "Null";
        }
    }

    private void updateNotification(String text,String tittle) {
        Notification notification = getMyActivityNotification(text,tittle);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID, notification);
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
