package com.example.watchtime.source.Database.Timer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TimeCountdownQuery {
    @Insert
    void storeTimeLeft(TimeCountdown timeCountdown);

    @Query("Select * from TimeCountdown where ID = :ID")
    TimeCountdown getTimeLeft(int ID);

    @Query("Update TimeCountdown set timeLeft = :Timeleft where ID = :ID")
    void updateTimeLeft(int Timeleft , int ID);

    @Query("Delete from TimeCountdown where ID = :ID")
    void deleteTimeLeft(int ID );
}
