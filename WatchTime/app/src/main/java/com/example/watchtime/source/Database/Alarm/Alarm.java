package com.example.watchtime.source.Database.Alarm;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Alarm implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private int ID;
    private String tittle;
    private int Hours;
    private int Minutes;
    private int alertsong;
    private boolean isActive;
    private String Dayactive;

    public Alarm(int ID, String tittle, int hours, int minutes, int alertsong, boolean isActive, String dayactive) {
        this.ID = ID;
        this.tittle = tittle;
        Hours = hours;
        Minutes = minutes;
        this.alertsong = alertsong;
        this.isActive = isActive;
        Dayactive = dayactive;
    }

    public String getDayactive() {
        return Dayactive;
    }

    public void setDayactive(String dayactive) {
        Dayactive = dayactive;
    }

    public int getAlertsong() {
        return alertsong;
    }


    public void setAlertsong(int alertsong) {
        this.alertsong = alertsong;
    }

    public int getHours() {
        return Hours;
    }

    public void setHours(@NonNull int hours) {
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

    public void setMinutes(@NonNull int minutes) {
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
