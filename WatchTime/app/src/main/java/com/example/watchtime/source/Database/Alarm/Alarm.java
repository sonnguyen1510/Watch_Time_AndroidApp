package com.example.watchtime.source.Database.Alarm;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Alarm {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private int ID;
    private String tittle;
    private int Hours;
    private int Minutes;
    private boolean isActive;

    public int getHours() {
        return Hours;
    }

    public void setHours(int hours) {
        Hours = hours;
    }
    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public int getMinutes() {
        return Minutes;
    }

    public void setMinutes(int minutes) {
        Minutes = minutes;
    }


    public int getID() {
        return ID;
    }

    public void setID(@NonNull int ID) {
        this.ID = ID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
