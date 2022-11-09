package com.example.watchtime.source.UIFunction.alarm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;

import java.util.List;
import java.util.zip.Inflater;

public class Repeat_day_adapter extends RecyclerView.Adapter<Repeat_day_adapter.Repeatday> {
    public Context context;
    public List<String>Day_of_Week;

    public Repeat_day_adapter(Context context, List<String> day_of_Week) {
        this.context = context;
        Day_of_Week = day_of_Week;
    }

    @NonNull
    @Override
    public Repeatday onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.repeatday_item,
                parent,
                false
        );
        return new Repeatday(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Repeatday holder, int position) {
        String WeekDay = Day_of_Week.get(position);
        holder.repeat_day.setText(WeekDay);
        holder.repeat_day_icon.setImageResource(R.drawable.ic_baseline_check_24);
        holder.repeat_day_icon.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return Day_of_Week.size();
    }

    public class Repeatday extends RecyclerView.ViewHolder {
        public LinearLayout repeat_day_isChecked;
        public ImageView repeat_day_icon;
        public TextView repeat_day;
        public Repeatday(@NonNull View itemView) {
            super(itemView);
            repeat_day_isChecked = itemView.findViewById(R.id.repeat_day_isChecked);
            repeat_day = itemView.findViewById(R.id.repeat_day);
            repeat_day_icon = itemView.findViewById(R.id.repeat_day_isCheckedicon);
            repeat_day_isChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(repeat_day_icon.getVisibility() == View.GONE){
                        repeat_day_icon.setVisibility(View.VISIBLE);
                        Log.e("","repeat_day_isChecked clicked");

                    }
                    else{
                        repeat_day_icon.setVisibility(View.GONE);
                    }

                }
            });
        }
    }


}
