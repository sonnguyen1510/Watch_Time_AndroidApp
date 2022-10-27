package com.example.watchtime.source.UIFunction.world_clock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;
import com.example.watchtime.source.Object.Time;
import com.example.watchtime.source.GlobalData.function;
import com.example.watchtime.source.Database.WorldClock.worldClockList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class world_clock_adapter extends RecyclerView.Adapter<world_clock_adapter.world_clock_view> {

    public List<worldClockList> clockData;
    public Context context;

    public world_clock_adapter(List<worldClockList> clockData, Context context) {
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
        //get your local time zone
        TimeZone current = TimeZone.getDefault();
        //get time zone in different place;
        TimeZone tz = TimeZone.getTimeZone(data.getTimeZone());

        //time zone difference (Hour)
        int differ = tz.getOffset(new Date().getTime())/1000/60/60 - current.getOffset(new Date().getTime())/1000/60/60;
        //getCurrentTime
        Time currentTime = new Time(Calendar.getInstance().getTime());
        Time regionTime = currentTime.getTimebyoffset(differ);

        holder.worldTime.setText(regionTime.toStringTime());
        holder.Region.setText(data.getRegion());
        holder.TimeZone.setText(function.World_clock.DayCheck(currentTime,differ)+", "+ signCheck(differ)+differ+" Hours");

    }

    private String signCheck(int differ) {
        if(differ >= 0)
            return "+";
        else
            return "";
    }

    @Override
    public int getItemCount() {
        return clockData.size();
    }

    public class world_clock_view extends RecyclerView.ViewHolder{
        TextView worldTime;
        TextView Region;
        TextView TimeZone;
        public world_clock_view(@NonNull View itemView) {
            super(itemView);
            worldTime = itemView.findViewById(R.id.world_time);
            Region = itemView.findViewById(R.id.RegionName);
            TimeZone = itemView.findViewById(R.id.TimeZone);
        }
    }
}
