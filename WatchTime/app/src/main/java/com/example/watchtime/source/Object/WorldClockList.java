package com.example.watchtime.source.Object;

import com.example.watchtime.source.Database.WorldClock.worldClockList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class WorldClockList implements Serializable {
    public ArrayList<worldClockList> data = new ArrayList<>();
    public Time Current;

    public WorldClockList(ArrayList<worldClockList> data) {
        this.data = data;
    }

    public WorldClockList(Time Current) {
        this.UpdateTime(Current);
    }


    public void UpdateTime(Time current){
        this.Current = current;


    }
    public Time getTime(int position){
        worldClockList Region = data.get(position);
        return this.Current.getTimebyoffset(Region.getOffset());
    }

    public int getDiffer(int position){
        TimeZone current = TimeZone.getDefault();
        return this.data.get(position).getOffset() - current.getOffset(new Date().getTime())/1000/60/60;
    }

    public Time getCurrent() {
        return this.Current;
    }

    public worldClockList get(int position){
        return this.data.get(position);
    }

    public void remove(int position){
        this.data.remove(position);
    }

    public int size(){
        return data.size();
    }

    public void add(worldClockList newWorldClock) {
        this.data.add(newWorldClock);
    }
}
