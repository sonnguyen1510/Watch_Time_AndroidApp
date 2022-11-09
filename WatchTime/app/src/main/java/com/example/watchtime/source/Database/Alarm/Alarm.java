package com.example.watchtime.source.Database.Alarm;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Alarm implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String tittle;
    private int Hours;
    private int Minutes;
    private int alertsong;
    private boolean isActive;
    private String Dayactive;

    public Alarm(String tittle, int Hours, int Minutes, int alertsong, boolean isActive, String Dayactive) {
        this.tittle = tittle;
        this.Hours = Hours;
        this.Minutes = Minutes;
        this.alertsong = alertsong;
        this.isActive = isActive;
        this.Dayactive = Dayactive;
    }

    public String getDayactive() {
        return Dayactive;
    }

    public void setDayactive(String Dayactive) {
        this.Dayactive = Dayactive;
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

    public void setHours(@NonNull int Hours) {
        this.Hours = Hours;
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

    public void setMinutes(@NonNull int Minutes) {
        this.Minutes = Minutes;
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

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
