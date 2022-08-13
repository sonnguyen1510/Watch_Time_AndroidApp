package com.example.watchtime.source.Database;


import static com.example.watchtime.resouce.global_variable.Database_Version;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.watchtime.source.Database.Timer.TimeCountdownQuery;
import com.example.watchtime.source.ui.Time.TimeCountdown;

@Database(entities = {
        TimeCountdown.class
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

    public abstract TimeCountdownQuery timeCountdownQuery();
}
