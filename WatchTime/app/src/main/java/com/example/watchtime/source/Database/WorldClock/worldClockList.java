package com.example.watchtime.source.Database.WorldClock;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.watchtime.source.Object.Time;

@Entity
public class worldClockList {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String timeZone;
    private int offset;
    private String region;


    public worldClockList(String timeZone, int offset, String region) {
        this.timeZone = timeZone;
        this.offset = offset;
        this.region = region;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
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
