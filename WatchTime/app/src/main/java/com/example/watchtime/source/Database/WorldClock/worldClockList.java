package com.example.watchtime.source.Database.WorldClock;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class worldClockList {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String timeZone;
    private String region;



    public worldClockList(String timeZone, String region) {
        this.timeZone = timeZone;
        this.region = region;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;;
    }




}
