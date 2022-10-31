package com.example.watchtime.source.UIFunction.world_clock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;
import com.example.watchtime.source.Object.Timezone;
import com.example.watchtime.source.GlobalData.global_variable;

import java.util.ArrayList;
import java.util.List;

public class addWorldClock_adapter extends RecyclerView.Adapter<addWorldClock_adapter.addWorldClock> implements Filterable {

    public Context context;
    public List<Timezone.zones> listCity;
    public List<Timezone.zones> FiltterlistCity = new ArrayList<>();

    public addWorldClock_adapter( List<Timezone.zones> listCity,Context context) {
        this.context = context;
        this.listCity = listCity;
        this.FiltterlistCity = listCity;
    }

    @NonNull
    @Override
    public addWorldClock onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_item,parent,false);

        return new addWorldClock(view);
    }

    @Override
    public void onBindViewHolder(@NonNull addWorldClock holder, int position) {
        Timezone.zones citydata = listCity.get(position);
        if(citydata.getValue() == null){
            holder.cityname.setText(citydata.getName());
            holder.cityname.setTextSize(15);
            holder.cityname.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.cityDivider.setVisibility(View.GONE);
            holder.cityitem.setBackgroundColor(Color.parseColor("#DDDDDD"));
        }
        else{
            holder.cityitem.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.cityname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.cityname.setText(citydata.getName());
            holder.cityDivider.setVisibility(View.VISIBLE);
            holder.cityitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent SendTimeData = new Intent();
                    SendTimeData.setAction("com.example.watchtime.source.ui.Time");
                    SendTimeData.putExtra(global_variable.Request,"isAddClock");
                    SendTimeData.putExtra("Region",citydata);
                    v.getContext().sendBroadcast(SendTimeData);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listCity.size();
    }

    //https://www.youtube.com/watch?v=MWlxFccYit8
    @Override
    public Filter getFilter() {
        Filter findSpecificCity = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.values = FiltterlistCity;
                    filterResults.count = FiltterlistCity.size();
                }
                else {
                    String search = constraint.toString().toLowerCase();
                    List<Timezone.zones> city = new ArrayList<>();
                    for (Timezone.zones zones : FiltterlistCity){
                        if(zones.getName().toLowerCase().contains(search)||zones.getValue() == null){
                            city.add(zones);
                        }
                    }
                    filterResults.values = city;
                    filterResults.count = city.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listCity = (List<Timezone.zones>) results.values;
                notifyDataSetChanged();
            }
        };
        return findSpecificCity;
    }

    public class addWorldClock extends RecyclerView.ViewHolder {
        public TextView cityname;
        public LinearLayout cityitem;
        public View cityDivider;
        public addWorldClock(@NonNull View itemView) {
            super(itemView);
            cityname = itemView.findViewById(R.id.cityname);
            cityitem = itemView.findViewById(R.id.cityitem);
            cityDivider = itemView.findViewById(R.id.cityDivider);
        }
    }
}
