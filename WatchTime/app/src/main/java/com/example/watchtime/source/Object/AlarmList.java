package com.example.watchtime.source.Object;

import com.example.watchtime.source.Database.Alarm.Alarm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AlarmList {
    private TreeMap<Integer,Alarm> alarmList = new TreeMap<>();

    public AlarmList(List<Alarm> alarmList) {
        for( Alarm i : alarmList){
            this.alarmList.put(mergeMinuteAndHourForCheck(i),i);
        }
    }

    public AlarmList(Alarm alarm){
        this.alarmList.put(mergeMinuteAndHourForCheck(alarm),alarm);
    }

    public void addNewAlarm (Alarm alarm){
        this.alarmList.put(mergeMinuteAndHourForCheck(alarm),alarm);

    }

    public void addNewAlarm (List<Alarm> alarmList){
        for( Alarm i : alarmList){
            this.alarmList.put(mergeMinuteAndHourForCheck(i),i);
        }
    }

    public Alarm getClosestAlarm(Date date){
        int key = mergeMinuteAndHourForCheck(date);
        Map.Entry<Integer, Alarm> low = alarmList.floorEntry(key);
        Map.Entry<Integer, Alarm> high = alarmList.ceilingEntry(key);
        Object res = null;
        if (low != null && high != null) {
            res = Math.abs(key-low.getKey()) < Math.abs(key-high.getKey()) ? low.getValue() : high.getValue();
        } else if (low != null || high != null) {
            res = low != null ? low.getValue() : high.getValue();
        }

        return (Alarm) res;
    }

    public Alarm getClosestAlarmHigh(int hour , int minute){
        int key = mergeMinuteAndHourForCheck(hour ,minute);
        return alarmList.ceilingEntry(key).getValue();
    }

    private int mergeMinuteAndHourForCheck(Alarm alarm){
        String minutes = "";
        if(alarm.getMinutes() - 10 < 0) // if minutes < 10
           minutes= "0"+String.valueOf(alarm.getMinutes());
        else
            minutes= String.valueOf(alarm.getMinutes());

        return Integer.parseInt(String.valueOf(alarm.getHours())+minutes);
    }

    @SuppressWarnings("deprecation")
    private int mergeMinuteAndHourForCheck(Date date){
        String minutes = "";
        if(date.getMinutes() - 10 < 0) // if minutes < 10
            minutes= "0"+String.valueOf(date.getMinutes());
        else
            minutes= String.valueOf(date.getMinutes());

        return Integer.parseInt(String.valueOf(date.getHours())+minutes);
    }

    private int mergeMinuteAndHourForCheck(int hour, int minute){
        String minutes = "";
        if(minute - 10 < 0) // if minutes < 10
            minutes= "0"+String.valueOf(minute);
        else
            minutes= String.valueOf(minute);

        return Integer.parseInt(String.valueOf(hour)+minutes);
    }

    public Alarm get(int position) {
        Set<Map.Entry<Integer,Alarm> > entrySet = alarmList.entrySet();

        // Converting entrySet to ArrayList
        List<Map.Entry<Integer,Alarm> > entryList = new ArrayList<>(entrySet);
        return (Alarm) entryList.get(position);
    }

    public int size() {
        return alarmList.size();
    }
}
