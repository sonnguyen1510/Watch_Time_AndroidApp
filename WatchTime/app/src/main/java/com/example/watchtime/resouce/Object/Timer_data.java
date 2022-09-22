package com.example.watchtime.resouce.Object;

import com.example.watchtime.source.Database.Timer.timeCountdown;

import java.io.Serializable;

public class Timer_data implements Serializable {
    //private timeCountdown data;
    public int totalTime;
    public int hour;
    public int minute;
    public int second;
    public int AlertSong;



    public Timer_data(){

    }

    public boolean CountDown() {
        if (second == 0) {
            if (minute == 0) {
                if (hour == 0) {
                    return false;
                } else {
                    second = 59;
                    minute = 59;
                    hour--;
                }
            } else {
                second = 59;
                minute--;
            }
        } else
            second--;

        return true;
    }


    /*
    public timeCountdown getData() {
        return data;
    }

    public void setData(timeCountdown data) {
        this.data = data;
    }
    * */
}
