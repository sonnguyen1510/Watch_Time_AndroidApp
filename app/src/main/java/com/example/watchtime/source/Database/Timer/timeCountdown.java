package com.example.watchtime.source.Database.Timer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class timeCountdown implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int ID;
    private int timeLeft;
    private int totalTime;

    public timeCountdown(int ID, int timeLeft, int totalTime) {
        this.ID = ID;
        this.timeLeft = timeLeft;
        this.totalTime = totalTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(@NonNull int ID) {
        this.ID = ID;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }


}
