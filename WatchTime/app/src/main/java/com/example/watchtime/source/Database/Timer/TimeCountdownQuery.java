package com.example.watchtime.source.Database.Timer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface timeCountdownQuery {
    @Insert
    void storeTimeLeft(timeCountdown timeCountdown);

    @Query("Select * from timeCountdown where ID=:ID")
    timeCountdown getTimeLeft(int ID);

    @Query("Update timeCountdown set timeLeft = :Timeleft where ID = :ID")
    void updateTimeLeft(int Timeleft , int ID);

    @Query("Delete from timeCountdown where ID = :ID")
    void deleteTimeLeft(int ID );
}
