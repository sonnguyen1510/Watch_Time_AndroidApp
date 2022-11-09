package com.example.watchtime.source.GlobalData;

import static com.example.watchtime.source.GlobalData.function.Timer.FormatTime;

import android.app.ActivityManager;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.example.watchtime.source.Object.Time;

public class function {
    public static class Timer {
        public static String getStringTime(int milliseconds){
            int totalTime = milliseconds;
            int seconds = (int) (milliseconds / 1000) % 60 ;
            int minutes = (int) ((milliseconds / (1000*60)) % 60);
            int hours   = (int) ((milliseconds / (1000*60*60)) % 24);;


            String stringTime ="";
            //set hour
            if(hours/10 ==0){
                stringTime += "0"+hours;
            }
            else
                stringTime += hours;

            //set minute
            if(minutes/10 == 0 ){
                stringTime +=":0"+minutes;
            }
            else
                stringTime +=":"+minutes;

            //set second
            if(seconds/10==0){
                stringTime +=":0"+seconds;
            }else
                stringTime +=":"+seconds;
            return stringTime;
        }

        public static String FormatTime(int hour,int minutes,int second){


            String stringTime ="";
            //set hour
            if(hour/10 ==0){
                stringTime += "0"+hour;
            }
            else
                stringTime += hour;

            //set minute
            if(minutes/10 == 0 ){
                stringTime +=":0"+minutes;
            }
            else
                stringTime +=":"+minutes;

            //set second
            if(second/10==0){
                stringTime +=":0"+second;
            }else
                stringTime +=":"+second;
            return stringTime;
        }

        public static String FormatTime(int hour , int minutes){
            String stringTime ="";
            //set hour
            if(hour/10 ==0){
                stringTime += "0"+hour;
            }
            else
                stringTime += hour;

            //set minute
            if(minutes/10 == 0 ){
                stringTime +=":0"+minutes;
            }
            else
                stringTime +=":"+minutes;

            return stringTime;
        }



        public static int[] getTime(int milliseconds){
            int[] time = new int[3];

            int totalTime = milliseconds;
            int seconds = (int) (milliseconds / 1000) % 60 ;
            int minutes = (int) ((milliseconds / (1000*60)) % 60);
            int hours   = (int) (milliseconds / (1000*60*60)) ;

            time[0] = hours;
            time[1] = minutes;
            time[2] = seconds;
            return time ;
        }

        public static int toMilisecond(int hours, int minutes, int second) {
            return (hours*60*60 + minutes*60 +second)*1000;
        }
    }


    public static class World_clock{
        public static String DayCheck(Time time , int day_offset){
            if(time.getHour() + (int)day_offset >23)
                return "Tomorrow";
            else if(time.getHour() + (int)day_offset <0)
                return "Yesterday";
            else
                return "Today";
        }


        public static String getTime(){
            return null;
        }

        public static String getTimebyOffset(Time UTC,float offset) {
            int differTime = UTC.getHour() + (int)offset;

            if(differTime <0){
                return FormatTime(23+differTime,UTC.getMinute());
            }
            else if(differTime > 23){
                return FormatTime(differTime-23,UTC.getMinute());
            }
            else
                return FormatTime(differTime,UTC.getMinute());
        }
    }

    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
