package com.example.watchtime.source.UIFunction.world_clock;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;
import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.Object.Time;
import com.example.watchtime.source.GlobalData.function;
import com.example.watchtime.source.Database.WorldClock.worldClockList;
import com.example.watchtime.source.Object.WorldClockList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class world_clock_adapter extends RecyclerView.Adapter<world_clock_adapter.world_clock_view> {

    public WorldClockList clockData;
    public Context context;

    public world_clock_adapter(WorldClockList clockData, Context context) {
        this.clockData = clockData;
        this.context = context;
    }

    @NonNull
    @Override
    public world_clock_view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewitem = LayoutInflater.from(context).inflate(
                R.layout.clock_item,
                parent,
                false
        );
        return new world_clock_view(viewitem);
    }

    @Override
    public void onBindViewHolder(@NonNull world_clock_view holder, int position) {
        worldClockList data = clockData.get(position);
        /*

       //get your local time zone
        TimeZone current = TimeZone.getDefault();
        //get time zone in different place;
        TimeZone tz = TimeZone.getTimeZone(data.getTimeZone());

        //time zone difference (Hour)
        int differ = tz.getOffset(new Date().getTime())/1000/60/60 - current.getOffset(new Date().getTime())/1000/60/60;
        //getCurrentTime
        Time currentTime = new Time(Calendar.getInstance().getTime());
        Time regionTime = currentTime.getTimebyoffset(differ);
         */
        int differ = clockData.getDiffer(position);
        String worldTime =  clockData.getTime(position).toStringTime();
        String Region = data.getRegion();

        holder.worldTime.setText(worldTime);
        holder.Region.setText(Region);
        holder.TimeZone.setText(function.World_clock.DayCheck(clockData.getCurrent(),differ)+", "+ signCheck(differ)+differ+" Hours");
        // Set world clock height
        holder.WorldClockItem.setMinimumWidth(getScreenWidth());
    }

    private String signCheck(int differ) {
        if(differ >= 0)
            return "+";
        else
            return "";
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public int getItemCount() {
        return clockData.size();
    }

    public class world_clock_view extends RecyclerView.ViewHolder{
        TextView worldTime;
        TextView Region;
        TextView TimeZone;
        LinearLayout WorldClockItem;
        CheckBox isChecked;
        public world_clock_view(@NonNull View itemView) {
            super(itemView);
            worldTime = itemView.findViewById(R.id.world_time);
            Region = itemView.findViewById(R.id.RegionName);
            TimeZone = itemView.findViewById(R.id.TimeZone);
            WorldClockItem = itemView.findViewById(R.id.world_clock_item);
            isChecked = itemView.findViewById(R.id.choosed_worldClock);
            isChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent SendTimeData = new Intent();
                    SendTimeData.setAction("com.example.watchtime.source.ui.Time");
                    SendTimeData.putExtra(global_variable.Request,"updateWorldClockMenu");
                    v.getContext().sendBroadcast(SendTimeData);
                }
            });
        }
    }
}
