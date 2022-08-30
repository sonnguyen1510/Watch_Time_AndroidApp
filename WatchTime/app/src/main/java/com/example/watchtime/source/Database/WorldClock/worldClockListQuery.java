package com.example.watchtime.source.Database.WorldClock;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface worldClockListQuery {
    @Insert
    void InsertWorldClock(worldClockList worldClockList) ;

    @Query("Select * from worldClockList")
    List<worldClockList> getAllWorldClock();

    @Query("Select * from worldClockList where ID =:ID")
    List<worldClockList> getWorldClockByID(int ID);
}
