package com.example.watchtime.source.ui.alarm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.watchtime.resouce.global_variable;
import com.example.watchtime.source.Database.Alarm.Alarm;

import java.util.List;

public class AlarmProcess extends Service{
    public List<Alarm> data;


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



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
