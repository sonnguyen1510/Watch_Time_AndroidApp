package com.example.watchtime.source.Database.Timer;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TimeCountdown {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private int ID;
    private int timeLeft;

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
