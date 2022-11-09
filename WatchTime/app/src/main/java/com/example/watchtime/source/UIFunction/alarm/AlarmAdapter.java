package com.example.watchtime.source.UIFunction.alarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;
import com.example.watchtime.source.Database.Alarm.Alarm;
import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.Object.AlarmList;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.alarm> {
    private AlarmList data ;
    private Context context;

    public AlarmAdapter(AlarmList data , Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public alarm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.alarm_item,parent,false);

        return new alarm(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull alarm holder, int position) {
        Alarm alarmData = data.get(position);
        if(alarmData.getHours() > 10 && alarmData.getMinutes() >10){
            holder.time.setText(alarmData.getHours() + ":"+alarmData.getMinutes());
        }else{
            if(alarmData.getHours() <10 && alarmData.getMinutes() >10){
                holder.time.setText("0"+alarmData.getHours() + ":"+alarmData.getMinutes());
            }
            else if (alarmData.getHours() >10 && alarmData.getMinutes() <10){
                holder.time.setText(alarmData.getHours() + ":0"+alarmData.getMinutes());
            }
            else {
                holder.time.setText("0"+alarmData.getHours() + ":0"+alarmData.getMinutes());
            }
        }

        holder.tittle.setText(alarmData.getTittle() +"");
        holder.activeAlarm.setChecked(alarmData.isActive());
        if(holder.activeAlarm.isChecked()){

        }
        String DayRepeat = alarmData.getDayactive();
        //Day repeat
        if(DayRepeat!=null){
            if(DayRepeat.toUpperCase().contains("Monday".toUpperCase())||DayRepeat.isEmpty()){
                ChangeRepeat_dayActive(holder.repeatMonday);
            }
            else {
                ChangeRepeat_dayInactive(holder.repeatMonday);
            }

            if(DayRepeat.toUpperCase().contains("Tuesday".toUpperCase())){
                ChangeRepeat_dayActive(holder.repeatTuesday);
            }
            else{
                ChangeRepeat_dayInactive(holder.repeatTuesday);
            }

            if(DayRepeat.toUpperCase().contains("Wednesday".toUpperCase())){
                ChangeRepeat_dayActive(holder.repeatTuesday);
            }
            else{
                ChangeRepeat_dayInactive(holder.repeatWednesday);
            }

            if(DayRepeat.toUpperCase().contains("Thursday".toUpperCase())){
                ChangeRepeat_dayActive(holder.repeatThursday);
            }
            else{
                ChangeRepeat_dayInactive(holder.repeatThursday);
            }

            if(DayRepeat.toUpperCase().contains("Friday".toUpperCase())){
                ChangeRepeat_dayActive(holder.repeatFriday);
            }
            else{
                ChangeRepeat_dayInactive(holder.repeatFriday);
            }

            if(DayRepeat.toUpperCase().contains("Saturday".toUpperCase())){
                ChangeRepeat_dayActive(holder.repeatSaturday);
            }
            else{
                ChangeRepeat_dayInactive(holder.repeatSaturday);
            }

            if(DayRepeat.toUpperCase().contains("Sunday".toUpperCase())){
                ChangeRepeat_dayActive(holder.repeatSunday);
            }
            else {
                ChangeRepeat_dayInactive(holder.repeatSunday);
            }
        }
    }
    private void ChangeRepeat_dayActive(TextView textView){
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setBackground(AppCompatResources.getDrawable(context,R.drawable.repeatday_alarmclock_active));
    }

    private void ChangeRepeat_dayInactive(TextView textView){
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setBackground(AppCompatResources.getDrawable(context,R.drawable.repeatday_alarmclcok));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void SetRepeat(TextView textView){

    }

    public class alarm extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView tittle;
        public Switch activeAlarm;
        public TextView repeatMonday;
        public TextView repeatTuesday;
        public TextView repeatWednesday;
        public TextView repeatThursday;
        public TextView repeatFriday;
        public TextView repeatSaturday;
        public TextView repeatSunday;
        public CheckBox AlarmCheck;
        public ImageView Edit_Alarm;


        public alarm(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.alarm_time);
            tittle = itemView.findViewById(R.id.alarm_title);
            activeAlarm = itemView.findViewById(R.id.active_alarm);
            repeatMonday = itemView.findViewById(R.id.alarm_repeatMonday);
            repeatTuesday = itemView.findViewById(R.id.alarm_repeatTuesday);
            repeatWednesday = itemView.findViewById(R.id.alarm_repeatWednesday);
            repeatThursday = itemView.findViewById(R.id.alarm_repeatThursday);
            repeatFriday = itemView.findViewById(R.id.alarm_repeatFriday);
            repeatSaturday = itemView.findViewById(R.id.alarm_repeatSaturday);
            repeatSunday = itemView.findViewById(R.id.alarm_repeatSunday);
            AlarmCheck = itemView.findViewById(R.id.Alarm_isChecked);
            Edit_Alarm = itemView.findViewById(R.id.Edit_Alarm);

            AlarmCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent SendTimeData = new Intent();
                    SendTimeData.setAction("com.example.watchtime.source.ui.Time");
                    SendTimeData.putExtra(global_variable.Request,"updateAlarmMenu");
                    v.getContext().sendBroadcast(SendTimeData);
                }
            });


        }
    }
}
