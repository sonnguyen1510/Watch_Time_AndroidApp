package com.example.watchtime.source.Database.Timer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlertSongQuery {
    @Insert
    void insertAlertSong(AlertSong alertSong);

    @Query("SELECT songname from alertsong")
    List<String> getAllAlertsong();

    @Query("SELECT location from alertsong where songname =:songname")
    int getAlertSong(String songname);
}
