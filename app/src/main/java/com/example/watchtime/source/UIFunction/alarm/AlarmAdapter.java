package com.example.watchtime.source.UIFunction.alarm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;
import com.example.watchtime.source.Database.Alarm.Alarm;
import com.example.watchtime.source.Database.DataStore;
import com.example.watchtime.source.GlobalData.function;
import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.Object.AlarmList;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.alarm> {
    private List<Alarm> data  ;
    private Context context;
    private BottomSheetDialog EditAlarm;
    public boolean EditedMode = false;
    private List<Boolean> deleteList = new ArrayList<>();

    public AlarmAdapter(List<Alarm> data , Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public alarm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.alarm_item,parent,false);

        return new alarm(itemView);
    }
    public List<Alarm> DeleteItemSelected(){
        List<Alarm> alarmRemove = new ArrayList<>();
        for (int i = 0 ; i<deleteList.size() ; i++){
            if(deleteList.get(i) == true){
                Alarm DeleteAlarm = data.get(i);
                data.remove(i);
                deleteList.remove(i);
                alarmRemove.add(DeleteAlarm);
                DataStore.getInstance(context).alarmListQuery().deleteAlarmByID(DeleteAlarm.getID());
                i--;
            }
        }

        Intent SendUpdateAlarmData = new Intent();
        SendUpdateAlarmData.setAction("com.example.watchtime.source.ui.Alarm");
        SendUpdateAlarmData.putExtra(global_variable.AlarmProcessRequest,"Delete");
        SendUpdateAlarmData.putExtra("DeleteData",(Serializable) alarmRemove);
        context.sendBroadcast(SendUpdateAlarmData);

        notifyDataSetChanged();
        return data;
    }

    @Override
    public void onBindViewHolder(@NonNull alarm holder, @SuppressLint("RecyclerView") int position) {
        Alarm alarmData = data.get(position);
        deleteList.add(false);

        if(alarmData.getHours() >= 10 && alarmData.getMinutes() >=10){
            holder.time.setText(alarmData.getHours() + ":"+alarmData.getMinutes());
        }else{
            if(alarmData.getHours() <10 && alarmData.getMinutes() <10){
                holder.time.setText("0"+alarmData.getHours() + ":0"+alarmData.getMinutes());
            }
            else if (alarmData.getHours() >=10 && alarmData.getMinutes() <10){
                holder.time.setText(alarmData.getHours() + ":0"+alarmData.getMinutes());
            }
            else {
                holder.time.setText("0"+alarmData.getHours() + ":"+alarmData.getMinutes());
            }
        }

        holder.tittle.setText(alarmData.getTittle() +"");

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

        Alarm newAlarm = new Alarm(alarmData);
        holder.Edit_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * SHOW POPUP WINDOWN
                 * */
                EditAlarm = new BottomSheetDialog(v.getContext(), R.style.BottomSheetStyle);
                BottomSheetBehavior<View> bottomSheetBehavior;
                View sheetview = LayoutInflater.from(v.getContext()).inflate(R.layout.add_alarm, (LinearLayout) v.findViewById(R.id.add_Alarm_layout));


                //Set up view

                sheetview.findViewById(R.id.add_Alarm_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditAlarm.dismiss();
                    }
                });

                TextView title = sheetview.findViewById(R.id.alarm_function_title);
                title.setText("");
                //-----SETUP TIME-----------------
                TextView showTime =sheetview.findViewById(R.id.ShowAlarmTime);
                showTime.setText(function.Timer.FormatTime(alarmData.getHours(),alarmData.getMinutes()));
                sheetview.findViewById(R.id.alarm_timeSetting).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                newAlarm.setHours(hourOfDay);
                                newAlarm.setMinutes(minute);
                                showTime.setText(function.Timer.FormatTime(alarmData.getHours(),alarmData.getMinutes()));

                            }
                        };
                        TimePickerDialog alarmTime = new TimePickerDialog(v.getContext(),onTimeSetListener,newAlarm.getHours(),newAlarm.getMinutes(),true);
                        alarmTime.setTitle("Set alarm time");
                        alarmTime.show();
                    }
                });


                //-----SETUP REPEATE DAY-----------------
                String Alarm_repeatDay = alarmData.getDayactive();
                List<String> listDay = new ArrayList<>();
                listDay.add("Monday");
                listDay.add("Tuesday");
                listDay.add("Wednesday");
                listDay.add("Thursday");
                listDay.add("Friday");
                listDay.add("Saturday");
                listDay.add("Sunday");
                EditText repeatdaychoosed = sheetview.findViewById(R.id.Repeat_time_choosed);
                repeatdaychoosed.setText(Alarm_repeatDay);
                //RecyclerView.Adapter<> RepeatDay = new
                repeatdaychoosed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog chooseRepeatDay = new Dialog(view.getContext());
                        chooseRepeatDay.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        chooseRepeatDay.setContentView(R.layout.alarm_repearday);
                        Window RepeatDaywindow = chooseRepeatDay.getWindow();
                        if(RepeatDaywindow == null){
                            Toast.makeText(view.getContext(),"We have some error , please reset the app and try again !",Toast.LENGTH_LONG);
                        }
                        RepeatDaywindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                        RepeatDaywindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        WindowManager.LayoutParams windownAttribute = RepeatDaywindow.getAttributes();
                        windownAttribute.gravity = Gravity.CENTER;
                        RepeatDaywindow.setAttributes(windownAttribute);
                        //custom dialog view
                        Repeat_day_adapter repeat_day_adapter = new Repeat_day_adapter(view.getContext(),listDay);
                        RecyclerView showRepeatList = chooseRepeatDay.findViewById(R.id.Show_repeatDay);
                        showRepeatList.setAdapter(repeat_day_adapter);
                        showRepeatList.setLayoutManager(new LinearLayoutManager(sheetview.getContext()));

                        chooseRepeatDay.findViewById(R.id.select_Alarm_repeatday).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newAlarm.setDayactive("");
                                String dayRepeat = "";
                                for(int position = 0 ; position < listDay.size();position++){
                                    View RepeatDayCheck = showRepeatList.getLayoutManager().findViewByPosition(position);
                                    TextView repeat_day = RepeatDayCheck.findViewById(R.id.repeat_day);
                                    ImageView repeat_day_icon = RepeatDayCheck.findViewById(R.id.repeat_day_isCheckedicon);
                                    if(repeat_day_icon.getVisibility() == View.VISIBLE){
                                        dayRepeat+=repeat_day.getText()+",";
                                    }
                                }
                                //View icon = viewItem.findViewById(R.id.view);
                                //EditText showRepeatDay = sheetview.findViewById(R.id.Repeat_time_choosed);
                                //Remove "," in last string
                                dayRepeat = function.removeLastChar(dayRepeat);
                                repeatdaychoosed.setText(dayRepeat);
                                chooseRepeatDay.dismiss();
                            }
                        });

                        chooseRepeatDay.show();
                    }
                });

                //-----SETUP DROPDOWN ALERT SONG MENU--------------
                AutoCompleteTextView alertsongChoosed = sheetview.findViewById(R.id.alarm_sound_choosed);
                alertsongChoosed.setText(context.getResources().getResourceEntryName(alarmData.getAlertsong()));
                int Alarm_Sound;

                List<String> ListSong = DataStore.getInstance(context).alertSongQuery().getAllAlertsong();
                ArrayAdapter<String> alertsongAdapter = new ArrayAdapter<>(v.getContext(), R.layout.alertsong_dropdownitem, ListSong);
                alertsongChoosed.setAdapter(alertsongAdapter);
                alertsongChoosed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
                        try {
                            newAlarm.setAlertsong(DataStore.getInstance(view.getContext()).alertSongQuery().getAlertSong(item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                //-----------------SETUP TITTLE-------------
                EditText Almtitle = sheetview.findViewById(R.id.alarm_tittle_choosed);
                Almtitle.setText(alarmData.getTittle());
                Almtitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(v.getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.settitle_dialog);

                        Window window = dialog.getWindow();

                        if(window!= null){
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            WindowManager.LayoutParams windowAttribute  = window.getAttributes();
                            windowAttribute.gravity = Gravity.CENTER;
                            window.setAttributes(windowAttribute);

                            //Save title
                            EditText inputTittle = dialog.findViewById(R.id.alarm_settittle_title);
                            inputTittle.setText(alarmData.getTittle());

                            dialog.findViewById(R.id.alarm_settittle_savetitle).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    newAlarm.setTittle( inputTittle.getText().toString());
                                    dialog.dismiss();
                                }
                            });
                        }
                        dialog.show();
                    }
                });
                //----------------------------------SAVE-------------------------------------------

                sheetview.findViewById(R.id.add_Alarm_Save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("Save","Updated alarm");
                        //Delete old alarm
                        DataStore.getInstance(sheetview.getContext()).alarmListQuery().deleteAlarmByID(alarmData.getID());
                        DataStore.getInstance(sheetview.getContext()).alarmListQuery().createNewAlarm(newAlarm);
                        data.set(position,newAlarm);
                        notifyDataSetChanged();
                        EditAlarm.dismiss();

                        //Update
                        Intent SendTimeData = new Intent();
                        SendTimeData.setAction("com.example.watchtime.source.ui.Time");
                        SendTimeData.putExtra(global_variable.Request,"UpdateAlarm");
                        SendTimeData.putExtra("Position",position);
                        SendTimeData.putExtra("Update",(Serializable) data.get(position));
                    }
                });
                //----------------------------------DELETE-----------------------------------
                sheetview.findViewById(R.id.DeleteAlarm).setVisibility(View.VISIBLE);
                sheetview.findViewById(R.id.DeleteAlarm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Delete Alarm")
                                .setMessage("Do you want to delete this alarm ?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent SendTimeData = new Intent();
                                        SendTimeData.setAction("com.example.watchtime.source.ui.Time");
                                        SendTimeData.putExtra(global_variable.Request,"DeleteAlarm");
                                        SendTimeData.putExtra("Position",position);
                                        SendTimeData.putExtra("Delete",(Serializable) alarmData);
                                        v.getContext().sendBroadcast(SendTimeData);
                                        EditAlarm.dismiss();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(v.getResources().getDrawable(R.drawable.ic_baseline_delete_forever_24))
                                .create()
                                .show();
                    }
                });

                EditAlarm.setContentView(sheetview);
                bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                //Set min heigh
                LinearLayout layout =  sheetview.findViewById(R.id.add_Alarm_layout);
                assert layout != null;
                layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

                //Show view
                EditAlarm.show();

            }
        });
        holder.activeAlarm.setChecked(alarmData.isActive());

        holder.activeAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Alarm updateAlarm = alarmData;
                updateAlarm.setActive(isChecked);
                //Update in service
                Intent SendUpdateAlarmData = new Intent();
                SendUpdateAlarmData.setAction("com.example.watchtime.source.ui.Alarm");
                SendUpdateAlarmData.putExtra(global_variable.AlarmProcessRequest,"UpdateStatus");
                SendUpdateAlarmData.putExtra("Status",isChecked);
                SendUpdateAlarmData.putExtra("Alarm",(Serializable) alarmData);
                context.sendBroadcast(SendUpdateAlarmData);

                //Update in DB
                DataStore.getInstance(context).alarmListQuery().UpdateStatus(alarmData.getID(),isChecked);
            }
        });

        holder.AlarmCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    deleteList.set(position,true);
                }
                else {
                    deleteList.set(position,false);
                }
            }
        });


        if (EditedMode == false){
            holder.AlarmCheck.setChecked(false);
            holder.AlarmCheck.setVisibility(View.GONE);
            holder.Edit_Alarm.setVisibility(View.GONE);
            holder.activeAlarm.setVisibility(View.VISIBLE);
        }
        else {
            holder.AlarmCheck.setVisibility(View.VISIBLE);
            holder.Edit_Alarm.setVisibility(View.VISIBLE);
            holder.activeAlarm.setVisibility(View.GONE);
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

    public Alarm get(int position) {
        return data.get(position);
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
                    SendTimeData.putExtra("NumberOfChecked",getNumofCheckedAlarm());
                    v.getContext().sendBroadcast(SendTimeData);
                }
            });
        }
    }

    private int getNumofCheckedAlarm() {
        for (boolean i : deleteList){
            if(i){
                return 1;
            }
        }
        return 0;
    }
}
