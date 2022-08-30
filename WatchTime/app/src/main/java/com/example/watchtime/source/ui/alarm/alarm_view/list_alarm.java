package com.example.watchtime.source.ui.alarm.alarm_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;
import com.example.watchtime.source.Database.Alarm.alarmList;

import java.util.List;

public class list_alarm extends RecyclerView.Adapter<list_alarm.alarm> {
    private List<alarmList>data ;
            private Context context;

    public list_alarm(List<alarmList> data , Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public alarm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.alarm_item,parent,true);

        return new alarm(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull alarm holder, int position) {
        alarmList alarmData = data.get(position);
        holder.time.setText(alarmData.getTime() + "");
        holder.tittle.setText(alarmData.getTittle() +"");
        holder.activeAlarm.setChecked(alarmData.isActive());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class alarm extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView tittle;
        public Switch activeAlarm;

        public alarm(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.alarm_time);
            tittle = itemView.findViewById(R.id.alarm_title);
            activeAlarm = itemView.findViewById(R.id.active_alarm);
        }
    }
}
