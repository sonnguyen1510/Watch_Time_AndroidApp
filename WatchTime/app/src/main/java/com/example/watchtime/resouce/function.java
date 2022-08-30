package com.example.watchtime.resouce;

import com.example.watchtime.resouce.Object.Timer_data;

public class function {
    public static class Timer {
        public static String getStringTime(int millisecond){
            int totalTime = millisecond;
            int hour = totalTime/(60*60*1000);
            int minutes = (totalTime - (hour*(60*60*1000)))/(60*1000);
            int second = (totalTime - hour*(60*60*1000) - minutes*60*1000)/1000;


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

        public static String FormatTime(Timer_data data){

            int hour = data.hour;
            int minutes = data.minute;
            int second = data.second;

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

        public static int[] getTime(int millisecond){
            int[] time = new int[3];

            int totalTime = millisecond;
            int hour = totalTime/(60*60*1000);
            int minutes = (totalTime - (hour*(60*60*1000)))/(60*1000);
            int second = (totalTime - hour*(60*60*1000) - minutes*60*1000)/1000;

            time[0] = hour;
            time[1] = minutes;
            time[2] = second;
            return time ;
        }
    }
}
