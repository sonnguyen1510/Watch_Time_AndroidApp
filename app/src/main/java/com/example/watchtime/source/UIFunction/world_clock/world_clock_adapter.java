package com.example.watchtime.source.UIFunction.world_clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;
import com.example.watchtime.source.Database.DataStore;
import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.GlobalData.function;
import com.example.watchtime.source.Database.WorldClock.worldClockList;
import com.example.watchtime.source.Object.WorldClockList;

import java.util.ArrayList;
import java.util.List;

public class world_clock_adapter extends RecyclerView.Adapter<world_clock_adapter.world_clock_view> {

    public WorldClockList clockData;
    public Context context;
    public boolean EditedMode = false;
    private List<Boolean> deleteWC = new ArrayList<>();

    public world_clock_adapter(WorldClockList clockData, Context context) {
        this.clockData = clockData;
        this.context = context;
    }

    public WorldClockList DeleteWorldClock(){
        for (int i = 0 ; i<deleteWC.size() ; i++) {
            if (deleteWC.get(i) == true) {
                worldClockList DeleteWC = clockData.get(i);
                clockData.remove(i);
                deleteWC.remove(i);

                DataStore.getInstance(context).worldClockListQuery().deleteWorldClock(DeleteWC.getID());
            }
        }
        notifyDataSetChanged();
        return clockData;
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
    public void onBindViewHolder(@NonNull world_clock_view holder, @SuppressLint("RecyclerView") int position) {
        worldClockList data = clockData.get(position);
        deleteWC.add(false);

        int differ = clockData.getDiffer(position);
        String worldTime =  clockData.getTime(position).toStringTime();
        String Region = data.getRegion();

        holder.worldTime.setText(worldTime);
        holder.Region.setText(Region);
        holder.TimeZone.setText(function.World_clock.DayCheck(clockData.getCurrent(),differ)+", "+ signCheck(differ)+differ+" Hours");
        // Set world clock height
        holder.WorldClockItem.setMinimumWidth(getScreenWidth());

        holder.isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    deleteWC.set(position,true);
                    Log.e("Checked","Change");
                }
                else {
                    deleteWC.set(position,false);
                    Log.e("Checked","ChangeToFalse");
                }
            }
        });

        if(EditedMode == false){
            holder.isChecked.setChecked(false);
            holder.isChecked.setVisibility(View.GONE);
        }
        else {
            holder.isChecked.setVisibility(View.VISIBLE);
        }
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
                    SendTimeData.putExtra("NumberOfCheckedWC",NumberOfCheckedWC());
                    v.getContext().sendBroadcast(SendTimeData);
                }
            });
        }
    }

    private int NumberOfCheckedWC(){
        for (boolean i : deleteWC){
            if(i){
                return 1;
            }
        }
        return 0;
    }
}
