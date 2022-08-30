package com.example.watchtime.source.Database.Alarm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface alarmListQuery {
    @Insert
    void createNewAlarm (alarmList alarmList);

    @Query("Select * from alarmList")
    List<alarmList> getAllAlarm();

    @Query("Select * from alarmList where ID = :ID")
    List<alarmList> getAlarmByID(int ID);
}
