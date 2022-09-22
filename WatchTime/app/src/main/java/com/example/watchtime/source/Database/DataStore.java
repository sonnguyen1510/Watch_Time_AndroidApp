package com.example.watchtime.source.Database;


import static com.example.watchtime.resouce.global_variable.Database_Version;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.watchtime.source.Database.Alarm.alarmList;
import com.example.watchtime.source.Database.Alarm.alarmListQuery;
import com.example.watchtime.source.Database.Timer.AlertSong;
import com.example.watchtime.source.Database.Timer.AlertSongQuery;
import com.example.watchtime.source.Database.Timer.timeCountdown;
import com.example.watchtime.source.Database.Timer.timeCountdownQuery;
import com.example.watchtime.source.Database.WorldClock.worldClockList;
import com.example.watchtime.source.Database.WorldClock.worldClockListQuery;
import com.example.watchtime.source.ui.Time.TimeCountdown;

@Database(entities = {
        timeCountdown.class,
        alarmList.class,
        worldClockList.class,
        AlertSong.class
},version = Database_Version)
public abstract class DataStore extends RoomDatabase {
    private static final String DATABASE_NAME = "ClockAPP";
    private static DataStore instance;

    public static synchronized DataStore getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),DataStore.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

    public abstract timeCountdownQuery timeCountdownQuery();
    public abstract alarmListQuery alarmListQuery();
    public abstract worldClockListQuery worldClockListQuery();
    public abstract AlertSongQuery alertSongQuery();
}
