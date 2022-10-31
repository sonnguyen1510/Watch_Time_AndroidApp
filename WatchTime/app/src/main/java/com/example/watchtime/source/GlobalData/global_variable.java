package com.example.watchtime.source.GlobalData;

import com.example.watchtime.Test_class;
import com.example.watchtime.source.UIFunction.Timer.TimerProcess;
import com.example.watchtime.source.ui.Main;
import com.example.watchtime.source.UIFunction.world_clock.world_clock_process;

public class global_variable {
    /**
     ----------------------ACTIVITIES--------------------------
     *
     *
     *
     */
    public static final Class Timer_activity = Main.class;
    public static final Class Alarm_activity = Main.class;
    //public static final Class World_Clock_activity = world_clock.class;
    public static final Class Test_class = Test_class.class;
    /**
     *
     -----------------------VARIABLE----------------------------
     */

    public static final String APIkey = "77cef6addede40418128873416cfee07";
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

    /**
     *
     --------------------------WORLD CLOCK----------------------------
     */
    public static final String WorldClockUpdate = "DATATIME";
    public static final Class worldClockService = world_clock_process.class;


    public static String Request = "RequestToBroadcast";
}
