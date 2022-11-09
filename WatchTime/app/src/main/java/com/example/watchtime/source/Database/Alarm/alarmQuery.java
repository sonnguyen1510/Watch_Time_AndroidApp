package com.example.watchtime.source.Database.Alarm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface alarmQuery {
    @Insert
    void createNewAlarm (Alarm alarmList);

    @Query("Select * from Alarm")
    List<Alarm> getAllAlarm();

    @Query("Select * from Alarm where ID = :ID")
    List<Alarm> getAlarmByID(int ID);

    @Query("DELETE FROM Alarm where ID = :ID")
    void deleteAlarmByID(int ID);
}
