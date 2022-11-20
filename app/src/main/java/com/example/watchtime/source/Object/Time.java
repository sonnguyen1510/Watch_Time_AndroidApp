package com.example.watchtime.source.Object;

import com.example.watchtime.source.GlobalData.function;

import java.io.Serializable;
import java.util.Date;

public class Time implements Serializable {
    public int hour ;
    public int minute;
    public int second;

    public Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }


    public Time (Date currentDay){
        this.hour = currentDay.getHours();
        this.minute = currentDay.getMinutes();
        this.second = currentDay.getSeconds();
    }

    public Time getTimebyoffset(int offset){
        int Hour = 0;
        int Minute;
        int Secound;

        if(this.hour + offset >24 ){
            Hour = this.hour + offset - 24;
        }
        else if(this.hour + offset < 0){
            Hour = this.hour + offset + 24;
        }
        else
            Hour = this.hour + offset;

        return new Time(Hour,this.minute,this.second);
    }

    public boolean IsNextMinutes(){
        if(this.second == 59){
            if(this.minute== 59){
                if(this.hour == 23) {
                    this.hour = 0;
                    this.minute = 0;
                    this.second = 0;
                }
                else{
                    this.minute = 0;
                    this.second = 0;
                    this.hour ++;
                }
            }
            else{
                this.minute++;
                this.second = 0;
            }
            return true;
        }
        else
            this.second ++;
            return false;
    }

    public String toStringTime(){
        return function.Timer.FormatTime(this.hour,this.minute);
    }

    public Time getTime() {
        return new Time(this.hour,this.minute,this.second);
    }
}