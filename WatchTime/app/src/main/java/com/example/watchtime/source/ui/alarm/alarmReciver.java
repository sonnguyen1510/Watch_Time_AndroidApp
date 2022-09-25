package com.example.watchtime.source.ui.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.watchtime.resouce.global_variable;

public class alarmReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
                String datatime = intent.getStringExtra(global_variable.AlarmData);
        }
}
