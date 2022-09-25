package com.example.watchtime.resouce;

import com.example.watchtime.Test_class;
import com.example.watchtime.source.ui.Time.TimerProcess;
import com.example.watchtime.source.ui.Time.Timer;
import com.example.watchtime.source.ui.world_clock.world_clock;

public class global_variable {
    /**
     ----------------------ACTIVITIES--------------------------
     *
     *
     *
     */
    public static final Class Timer_activity = Timer.class;
    public static final Class Alarm_activity = Timer.class;
    public static final Class World_Clock_activity = world_clock.class;
    public static final Class Test_class = Test_class.class;
    /**
     *
     -----------------------VARIABLE----------------------------
     */


    /**
     *
     ------------------------DATABASE----------------------------
     */
    //Database version:
    public static final int Database_Version = 2;

    /**
     *
     --------------------------TIMER----------------------------
     */
    public static final int TimerID = 123456;
    public static final String TimeData = "DATATIME";

    public static final Class TimerService = TimerProcess.class;
    /**
     * @param TimerID timer using 1 ID only to control all of them
     */
    /**
     ---------------------------ALARM---------------------------
     */
    public static final String AlarmData = "DATAALARM";

}
