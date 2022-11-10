package com.example.watchtime.source.UIFunction.world_clock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.watchtime.source.Object.Time;
import com.example.watchtime.source.GlobalData.global_variable;

import java.io.Serializable;
import java.util.Calendar;

public class world_clock_process extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //int UTCtime = intent.getIntExtra("worldClock_data",0);
        Log.e("In world clock Thread","");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Time runTime = new Time(Calendar.getInstance().getTime());
                    while (!isInterrupted()) {
                        Thread.sleep(1000);

                        if(runTime.IsNextMinutes()){
                            UpdateTimeAlert(runTime.getTime());
                            Log.e("In world clock Thread","Update");
                        }
                        Log.e("In world clock Thread",runTime.toStringTime());

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void UpdateTimeAlert(Time time) {
        Intent SendTimeData = new Intent();
        SendTimeData.setAction("com.example.watchtime.source.ui.Time");
        SendTimeData.putExtra(global_variable.Request,"isUpdateClock");
        SendTimeData.putExtra("Clock",(Serializable) time);
        sendBroadcast(SendTimeData);
    }



    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
