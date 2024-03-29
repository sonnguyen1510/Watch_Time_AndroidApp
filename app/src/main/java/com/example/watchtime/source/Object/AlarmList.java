package com.example.watchtime.source.Object;

import com.example.watchtime.source.Database.Alarm.Alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AlarmList implements Serializable {
    private TreeMap<Integer,List<Alarm>> alarmList = new TreeMap<>();

    public AlarmList(List<Alarm> alarmList) {
        for( Alarm i : alarmList){
            this.addNewAlarm(i);
        }
    }

    public TreeMap getData(){
        return alarmList;
    }

    public AlarmList(Alarm alarm){
        this.addNewAlarm(alarm);
    }


    public void addNewAlarm (Alarm alarm){
        if(alarmList.get(mergeMinuteAndHourForCheck(alarm)) == null){
            List<Alarm> newAlarm = new ArrayList<>();
            newAlarm.add(alarm);
            this.alarmList.put(mergeMinuteAndHourForCheck(alarm),newAlarm);
        }
        else{
            List<Alarm> alarmList = this.alarmList.get(mergeMinuteAndHourForCheck(alarm));
            alarmList.add(alarm);
            this.alarmList.put(mergeMinuteAndHourForCheck(alarm),alarmList);
        }

    }

    public void addNewAlarm (List<Alarm> alarmList){
        for( Alarm i : alarmList){
            this.addNewAlarm(i);
        }
    }

    public Alarm getClosestAlarm(Date date){
        int key = mergeMinuteAndHourForCheck(date);
        Map.Entry<Integer, List<Alarm>> low = alarmList.floorEntry(key);
        Map.Entry<Integer, List<Alarm>> high = alarmList.ceilingEntry(key);
        Object res = null;
        if (low != null && high != null) {
            res = Math.abs(key-low.getKey()) < Math.abs(key-high.getKey()) ? low.getValue() : high.getValue();
        } else if (low != null || high != null) {
            res = low != null ? low.getValue() : high.getValue();
        }

        return (Alarm) res;
    }

    public List<Alarm> getClosestAlarmHigh(int hour , int minute){
        int key = mergeMinuteAndHourForCheck(hour ,minute);
        Map.Entry<Integer, List<Alarm>> high = alarmList.ceilingEntry(key);
        if(high == null){
            return this.getByPosition(0);
        }else
            return high.getValue();
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

    public List<Alarm> getByPosition(int position) {
        return (List<Alarm>) alarmList.values().toArray()[position];
    }

    public int getKey(int position){
        return (int) alarmList.keySet().toArray()[position];
    }
    public int size() {
        return alarmList.size();
    }

    public boolean detete(int position){
        if(position >= this.size()){
            return false;
        }
        else{

            this.alarmList.remove(this.getKey(position));
            return true;
        }
    }

    public boolean deleteAll(){
        alarmList.clear();
        return true;
    }

    public boolean isEmpty(){
        if(alarmList.size() == 0){
            return true;
        }
        else
            return false;
    }

    public void detete(Alarm delete) {
        int key = this.mergeMinuteAndHourForCheck(delete);
        List<Alarm> value = alarmList.get(key);
        if(value != null){
            for (int position = 0 ; position < value.size() ; position++){
                if(delete.getID() == value.get(position).getID()){
                    value.remove(position);
                }
            }
            this.alarmList.put(key,value);
        }

    }

    public void update(Alarm updateAlarm) {
        int key = this.mergeMinuteAndHourForCheck(updateAlarm);
        List<Alarm> value = alarmList.get(key);
        if(value != null){
            for (int position = 0 ; position < value.size() ; position++){
                if(updateAlarm.getID() == value.get(position).getID()){
                    value.set(position,updateAlarm);
                }
            }
            this.alarmList.put(key,value);
        }
    }

    public List<Alarm> toListAlarm() {
        List<Alarm> result = new ArrayList<>();
        for ( int i = 0 ; i < alarmList.size();i++){
            List<Alarm> temp = this.getByPosition(i);
           for(Alarm j : temp){
               result.add(j);
           }
        }
        return result;
    }

}
