package com.example.watchtime.source.UI;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;
import com.example.watchtime.source.Database.Alarm.Alarm;
import com.example.watchtime.source.JSON.JSONReader;
import com.example.watchtime.source.Object.AlarmList;
import com.example.watchtime.source.Object.Time;
import com.example.watchtime.source.Object.Timezone;
import com.example.watchtime.source.GlobalData.function;
import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.Database.DataStore;
import com.example.watchtime.source.Database.Timer.AlertSong;
import com.example.watchtime.source.Object.Timer_data;
import com.example.watchtime.source.Database.Timer.timeCountdown;
import com.example.watchtime.source.Database.WorldClock.worldClockList;
import com.example.watchtime.source.Object.WorldClockList;
import com.example.watchtime.source.UIFunction.alarm.AlarmAdapter;
import com.example.watchtime.source.UIFunction.alarm.AlarmProcess;
import com.example.watchtime.source.UIFunction.alarm.Repeat_day_adapter;
import com.example.watchtime.source.UIFunction.world_clock.addWorldClock_adapter;
import com.example.watchtime.source.UIFunction.world_clock.world_clock_adapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Main extends AppCompatActivity implements Serializable {
    /**
     * ****MENU*****
     */
    public LinearLayout Timer;
    public LinearLayout World_Clock;
    public LinearLayout Alarm;
    private int menuToChoose = 0;
    private ViewFlipper LayoutChange;
    private Menu optionsMenu;
    /**
     * ********************************TIMER****************************
     */
    public Button start_timer;
    public Button cancel_timer;


    public Timer_data timer_data;
    public ProgressBar PercentTimeLeft;
    public TextView ShowTimeLeft;

    //Alert song
    public TextInputLayout alertsong;
    public List<String> ListSong;
    public ArrayAdapter<String> alertsongAdapter;
    public int DefaultSong;

    //time
    public LinearLayout timeSetting;
    public NumberPicker mHourPicker;
    public NumberPicker mMinutePicker;
    public NumberPicker mSecondPicker;

    private int mCurrentHour = 0; // 0-23
    private int mCurrentMinute = 0; // 0-59
    private int mCurrentSeconds = 0; // 0-59

    public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER =
            new NumberPicker.Formatter() {

                @Override
                public String format(int value) {
                    return String.format("%02d", value);
                }
            };

    /**
     * ********************************WORLD CLOCK***************************
     */
    public world_clock_adapter world_clock_adapter;
    public WorldClockList worldClock_data;
    public RecyclerView showWorldClock;
    //Add world clock
    public BottomSheetDialog addWorldClock;
    public boolean worldclockEditmode = false;



    /**
     ********************************ALARM****************************
     */

    public AlarmList alarm_data;
    public RecyclerView showAlarm;
    public AlarmAdapter alarm_adapter;
    //AddAlarm
    public BottomSheetDialog addAlarm;
    public boolean AlarmEditmode = false;
    /**
     * CONTENT
     *
     *
     *
     *
     */
    /**
     * ********************************CONTACT TO SERVICE ****************************
     */
    //Use to get time data from service
    BroadcastReceiver TimerBroadcastReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String Request = intent.getStringExtra(global_variable.Request);
            ///--------------------------------------World Clock--------------------------
            if (Request.equalsIgnoreCase("isUpdateClock")) {
                Time updateTime = (Time) intent.getSerializableExtra("Clock");
                UpdateClock(updateTime);
                Log.e("Main","worldClock Update");
            }
            else if (Request.equalsIgnoreCase("isAddClock")) {
                Timezone.zones region = (Timezone.zones) intent.getSerializableExtra("Region");
                if (DataStore.getInstance(Main.this).worldClockListQuery().getWorldClockByRegion(region.name) == null) {

                    TimeZone tz = TimeZone.getTimeZone(region.value);
                    worldClockList newWorldClock = new worldClockList(region.value,tz.getOffset(new Date().getTime())/1000/60/60,region.name);

                    worldClock_data.add(newWorldClock);
                    DataStore.getInstance(Main.this).worldClockListQuery().InsertWorldClock(newWorldClock);
                    world_clock_adapter.notifyDataSetChanged();

                    if(addWorldClock.isShowing()){
                        addWorldClock.dismiss();
                    }


                } else {
                    Toast.makeText(Main.this, "This world clock has been added !", Toast.LENGTH_LONG);
                }
            }
            else if(Request.equalsIgnoreCase("UpdateTimer")){
                String datatime = intent.getStringExtra(global_variable.TimeData);
                startTimerLayoutChange();
                ShowTimeLeft.setText(datatime);
            }
            else if (Request.equalsIgnoreCase("updateWorldClockMenu")){
                MenuItem delete = optionsMenu.findItem(R.id.DeleteSecletedWorldClock);
                MenuItem edit = optionsMenu.findItem(R.id.world_clock_edit);
                int numberOfCheckedWC = intent.getIntExtra("NumberOfCheckedWC",0);


                if(numberOfCheckedWC  > 0){
                    delete.setVisible(true);
                    edit.setVisible(false);
                }
                else{
                    delete.setVisible(false);
                    edit.setVisible(true);
                }
            }
            ///----------------------------------ALARM-------------------------------------
            else if(Request.equalsIgnoreCase("RepeatDay")){

            }
            else if(Request.equalsIgnoreCase("UpdateAlarm")){
                Alarm Update = (com.example.watchtime.source.Database.Alarm.Alarm) intent.getSerializableExtra("Update");
                alarm_data.update(Update);
                alarm_adapter.notifyDataSetChanged();
                //DataStore.getInstance(Main.this).alarmListQuery().deleteAlarmByID(delete.getID());

                //Update data in Alarm service
                Intent SendUpdateAlarmData = new Intent();
                SendUpdateAlarmData.setAction("com.example.watchtime.source.ui.Alarm");
                SendUpdateAlarmData.putExtra(global_variable.AlarmProcessRequest,"Update");
                SendUpdateAlarmData.putExtra("UpdateData",(Serializable)Update);
                sendBroadcast(SendUpdateAlarmData);
            }
            else if (Request.equalsIgnoreCase("updateAlarmMenu")){
                MenuItem delete = optionsMenu.findItem(R.id.DeleteSecletedAlarm);
                MenuItem edit = optionsMenu.findItem(R.id.alarm_edit_menu);
                int numberOfCheckedAlarm = intent.getIntExtra("NumberOfChecked",0);


                if(numberOfCheckedAlarm > 0){
                    delete.setVisible(true);
                    edit.setVisible(false);
                }
                else{
                    delete.setVisible(false);
                    edit.setVisible(true);
                }
            }
            else if (Request.equalsIgnoreCase("DeleteAlarm")){
                Alarm delete = (com.example.watchtime.source.Database.Alarm.Alarm) intent.getSerializableExtra("Delete");
                alarm_data.detete(delete);
                alarm_adapter.notifyDataSetChanged();
                DataStore.getInstance(Main.this).alarmListQuery().deleteAlarmByID(delete.getID());

                //Update data in Alarm service
                Intent SendUpdateAlarmData = new Intent();
                List<Alarm> deleteAlarms = new ArrayList<>();
                deleteAlarms.add(delete);

                SendUpdateAlarmData.setAction("com.example.watchtime.source.ui.Alarm");
                SendUpdateAlarmData.putExtra(global_variable.AlarmProcessRequest,"Delete");
                SendUpdateAlarmData.putExtra("DeleteData",(Serializable)deleteAlarms );
                sendBroadcast(SendUpdateAlarmData);
            }

        }
    };


    /**
     * ---------------------------------------------ACTIVITY--------------------------------------
     */
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter TimerintentFilter = new IntentFilter();
        TimerintentFilter.addAction("com.example.watchtime.source.ui.Time");
        registerReceiver(TimerBroadcastReciver, TimerintentFilter);



        ClearOptionChoice();


        //Check timer is active or not
        if (DataStore.getInstance(this).timeCountdownQuery().getTimeLeft(global_variable.TimerID) != null) {
            startTimerLayoutChange();
        }

        //Start layout is world clock
        ImageView WorldClockImage = findViewById(R.id.world_clock_image);
        WorldClockImage.setImageResource(R.drawable.ic_baseline_world_clock_red);
        WorldClockMenu();
        //startActivity(new Intent(v.getContext(),World_Clock_activity));
        LayoutChange.setDisplayedChild(LayoutChange.indexOfChild(findViewById(R.id.world_clock)));


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E8E6E6")));
        setContentView(R.layout.main);

        /**
         * RUN APP FIRST TIME
         */
        //------------------------------ALERT--------------------------
        //-----FIRST USING APP ------------
        if (DataStore.getInstance(this).alertSongQuery().getAllAlertsong().isEmpty()) {
            List<AlertSong> CreateFristListSong = new ArrayList<>();
            CreateFristListSong.add(new AlertSong("  Radar", R.raw.radar));
            CreateFristListSong.add(new AlertSong("  Apex", R.raw.apex));

            for (AlertSong data : CreateFristListSong) {
                DataStore.getInstance(this).alertSongQuery().insertAlertSong(data);
            }
        }
        //-----SET DEFAULT ------------
        DefaultSong = R.raw.radar;



        /**
         * --------------------------------MENU----------------------------
         */
        LayoutChange=  findViewById(R.id.UIFunction);
        Timer = findViewById(R.id.Timer_button);
        World_Clock = findViewById(R.id.world_clock_button);
        Alarm = findViewById(R.id.alarm_button);

        //Timer activity setup

        Timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                ClearOptionChoice();
                TimerMenu();
                ImageView TimerImage = findViewById(R.id.Timer_image);
                TimerImage.setImageResource(R.drawable.ic_baseline_timer_red);
                LayoutChange.setDisplayedChild(LayoutChange.indexOfChild(findViewById(R.id.Timer)));
            }
        });

        //world clock activity setup
        World_Clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearOptionChoice();
                WorldClockMenu();
                ImageView WorldClockImage = findViewById(R.id.world_clock_image);
                WorldClockImage.setImageResource(R.drawable.ic_baseline_world_clock_red);
                //startActivity(new Intent(v.getContext(),World_Clock_activity));
                LayoutChange.setDisplayedChild(LayoutChange.indexOfChild(findViewById(R.id.world_clock)));
            }
        });

        //Alarm activity setup
        Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearOptionChoice();
                AlarmMenu();
                ImageView AlarmImage = findViewById(R.id.alarm_image);
                AlarmImage.setImageResource(R.drawable.ic_baseline_alarm_red);
                //startActivity(new Intent(v.getContext(),World_Clock_activity));
                LayoutChange.setDisplayedChild(LayoutChange.indexOfChild(findViewById(R.id.alarm)));
            }
        });

        /**
         *-------------------------------TIMER-------------------------------------------
         */
        //-------------------------------SETUP--------------------------
        ShowTimeLeft = findViewById(R.id.ShowTimeLeft);
        timeSetting = findViewById(R.id.timeSetting);
        start_timer = findViewById(R.id.start_timer);
        cancel_timer = findViewById(R.id.Cancel_timer);
        ShowTimeLeft = findViewById(R.id.ShowTimeLeft);
        PercentTimeLeft = findViewById(R.id.PercentageOfTimeLeft);

        //------------------------------TIME PICKER--------------------

        mHourPicker = findViewById(R.id.hour);
        mMinutePicker = findViewById(R.id.minute);
        mSecondPicker = findViewById(R.id.seconds);

        setCurrentHour(0);
        setCurrentMinute(0);
        setCurrentSecond(0);

        // hour
        mHourPicker = (NumberPicker) findViewById(R.id.hour);
        mHourPicker.setMinValue(0);
        mHourPicker.setMaxValue(24);
        mHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCurrentHour = newVal;
            }
        });

        // digits of minute
        mMinutePicker = (NumberPicker) findViewById(R.id.minute);
        mMinutePicker.setMinValue(0);
        mMinutePicker.setMaxValue(59);
        mMinutePicker.setFormatter(TWO_DIGIT_FORMATTER);
        mMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                mCurrentMinute = newVal;
            }
        });

        // digits of seconds
        mSecondPicker = (NumberPicker) findViewById(R.id.seconds);
        mSecondPicker.setMinValue(0);
        mSecondPicker.setMaxValue(59);
        mSecondPicker.setFormatter(TWO_DIGIT_FORMATTER);
        mSecondPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCurrentSeconds = newVal;
            }
        });


        //-----SETUP DROPDOWN ALERT SONG MENU--------------
        alertsong = findViewById(R.id.choose_alert_song);
        AutoCompleteTextView alertsongChoosed = findViewById(R.id.alertsong_choosed);
        ListSong = DataStore.getInstance(this).alertSongQuery().getAllAlertsong();

        alertsongAdapter = new ArrayAdapter<>(this, R.layout.alertsong_dropdownitem, ListSong);
        alertsongChoosed.setAdapter(alertsongAdapter);
        alertsongChoosed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                try {
                    DefaultSong = DataStore.getInstance(view.getContext()).alertSongQuery().getAlertSong(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //-------------------------------BUTTON-------------------------

        start_timer = findViewById(R.id.start_timer);
        start_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change Layout
                startTimerLayoutChange();

                //String[] Time = inputTime.getText().toString().split(":");
                int hours = mCurrentHour;
                int minutes = mCurrentMinute;
                int seconds = mCurrentSeconds;

                timer_data = new Timer_data();
                timer_data.totalTime = function.Timer.toMilisecond(hours, minutes, seconds);
                timer_data.hour = mCurrentHour;
                timer_data.minute = mCurrentMinute;
                timer_data.second = mCurrentSeconds;
                //alert song
                timer_data.AlertSong = DefaultSong;

                DataStore.getInstance(Main.this).timeCountdownQuery().storeTimeLeft(new timeCountdown(global_variable.TimerID, timer_data.totalTime, timer_data.totalTime));
                startTimer();
                //onCountdown();
            }


        });


        cancel_timer = findViewById(R.id.Cancel_timer);
        cancel_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        /**
         *-------------------------------WORLD CLOCK-------------------------------------------
         */
        //------------------LOAD WORLD CLOCK LIST-------------------------------
        //Load world clock
        Date current = Calendar.getInstance().getTime();
        worldClock_data = new WorldClockList(new Time(current));

        showWorldClock = findViewById(R.id.worldClock_list);
        List<worldClockList> wc_data  = DataStore.getInstance(this).worldClockListQuery().getAllWorldClock();
        for (worldClockList i : wc_data){
            worldClock_data.add(i);
        }

        world_clock_adapter = new world_clock_adapter(worldClock_data, this);

        showWorldClock.setAdapter(world_clock_adapter);
        showWorldClock.setLayoutManager(new LinearLayoutManager(this));
        //Updating Times
        Intent startWorldClock = new Intent(this, global_variable.worldClockService);
        //Send time data
        startService(startWorldClock);

        //swipe layout : https://github.com/chthai64/SwipeRevealLayout
        worldClockList deleteWorldClock = null;
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        new AlertDialog.Builder(Main.this).setTitle("Delete world clock")
                                .setMessage("Delete world clock " + worldClock_data.get(position).getRegion())
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        world_clock_adapter.notifyItemChanged(position);
                                        dialog.cancel();
                                    }
                                })
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DataStore.getInstance(Main.this).worldClockListQuery().deleteWorldClock(worldClock_data.get(position).getID());
                                        worldClock_data.remove(position);
                                        world_clock_adapter.notifyItemChanged(position);
                                    }
                                })
                                .create().show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    int height = itemView.getBottom() - itemView.getTop();
                    int width = height / 3;
                    if (dX < 0) {
                        Paint p = new Paint();
                        p.setColor(Color.RED);
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        float margin = (dX / 5 - width) / 2;
                        RectF iconDest = new RectF((float) itemView.getRight() + margin, (float) itemView.getTop() + width, (float) itemView.getRight() + (margin + width), (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, iconDest, p);

                    }


                } else {
                    c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
                int position = holder.getAdapterPosition();
                int dragFlags = 0; // whatever your dragFlags need to be
                int swipeFlags = createSwipeFlags(position);

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            private int createSwipeFlags(int position) {
                return position == 0 ? 0 : ItemTouchHelper.LEFT;
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(showWorldClock);

        /**
         *-------------------------------ALARM-------------------------------------------
         */

            List<Alarm> alarmData = DataStore.getInstance(this).alarmListQuery().getAllAlarm();
            alarm_data = new AlarmList(alarmData);

            showAlarm = findViewById(R.id.alarm_list);
            alarm_adapter  = new AlarmAdapter(alarm_data.toListAlarm(),this);
            showAlarm.setAdapter(alarm_adapter);
            showAlarm.setLayoutManager(new LinearLayoutManager(this));
            if(!alarm_data.isEmpty()){
                Log.e("In main", alarm_data.size()+"");
                if(!function.isMyServiceRunning(AlarmProcess.class,this)){
                    StartAlarmProcess(this, alarm_data);
                }
            }

                //Toast.makeText(this,"Can't get alarm data", Toast.LENGTH_LONG);


    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(TimerBroadcastReciver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StopWorldClockService();
    }

    /**
     * -------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------
     *-------------------------------MENU FUNCTION-------------------------------------------
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_worldClockmenu:
                openAddworldClockWindow(this);
                return true;
            //Alarm-------------------------------------
            case R.id.add_Alarm:
                openAddAlarmWindow(this);
                return true;
            case R.id.alarm_edit_menu:
                EditAlarm(this);
                return true;
            case R.id.DeleteSecletedAlarm:
                DeleteAlarmSelected(this);
                return true;
            case R.id.alarm_delete_all:
                DeleteAllAlarm(this);
                return true;
            //World Clock------------------------------------
            case R.id.world_clock_delete_all:
                DeleteAllWorldClock(this);
            case R.id.world_clock_edit:
                EditWorldClock(this);
                return true;
            case R.id.DeleteSecletedWorldClock:
                DeleteWorldClockSelected(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void TimerMenu() {
        menuToChoose = R.menu.timer;
        invalidateOptionsMenu();
    }
    private void AlarmMenu() {
        menuToChoose = R.menu.alarm;
        invalidateOptionsMenu();
    }
    private void WorldClockMenu() {
        menuToChoose = R.menu.worldclock;
        invalidateOptionsMenu();
    }
    private void ClearOptionChoice() {
        ImageView TimerImage = findViewById(R.id.Timer_image);
        ImageView WorldClockImage = findViewById(R.id.world_clock_image);
        ImageView AlarmImage = findViewById(R.id.alarm_image);

        TimerImage.setImageResource(R.drawable.ic_baseline_timer);
        AlarmImage.setImageResource(R.drawable.ic_baseline_alarm);
        WorldClockImage.setImageResource(R.drawable.ic_baseline_world);

        if(AlarmEditmode == true){
            EditAlarm(this);

        }
        if(worldclockEditmode == true){
            EditWorldClock(this);
        }
    }

    private void UpdateClock(Time updateTime) {
        worldClock_data.UpdateTime(updateTime);
        world_clock_adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuToChoose, menu);
        optionsMenu = menu;
        return true;
    }




    private void WorldClockMenuReset(Menu menu){
        MenuItem delete = menu.findItem(R.id.DeleteSecletedWorldClock);
        MenuItem edit = menu.findItem(R.id.world_clock_edit);
        MenuItem deleteAll = menu.findItem(R.id.world_clock_delete_all);


        if(delete.isVisible()){
            delete.setVisible(false);
            edit.setVisible(true);
        }

        deleteAll.setVisible(false);

    }

    private void AlarmMenuReset(Menu menu){
        MenuItem delete = menu.findItem(R.id.DeleteSecletedAlarm);
        MenuItem edit = menu.findItem(R.id.alarm_edit_menu);
        MenuItem deleteAll = menu.findItem(R.id.alarm_delete_all);


        if(delete.isVisible()){
            delete.setVisible(false);
            edit.setVisible(true);
        }
        deleteAll.setVisible(false);

    }

    /**
     *-------------------------------ALARM FUNCTION-------------------------------------------
     */
    private void StartAlarmProcess(Context context, AlarmList listAlarm){
        Intent startAlarm = new Intent(this, global_variable.AlarmService);
        //Send time data
        //Log.e("",data.getData().getTotalTime()+"");
        startAlarm.putExtra("Alarm_data", (Serializable) listAlarm);
        startService(startAlarm);
    }

    private void openAddAlarmWindow(Context context) {
        /**
         * SHOW POPUP WINDOWN
         * */
        Alarm newalarm = new Alarm();


        addAlarm = new BottomSheetDialog(Main.this, R.style.BottomSheetStyle);
        BottomSheetBehavior<View> bottomSheetBehavior;
        View sheetview = LayoutInflater.from(context).inflate(R.layout.add_alarm, (LinearLayout) findViewById(R.id.add_Alarm_layout));


        //Set up view

        sheetview.findViewById(R.id.add_Alarm_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm.dismiss();
            }
        });
        //-----SETUP TIME-----------------


        sheetview.findViewById(R.id.alarm_timeSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        newalarm.setHours(hourOfDay);
                        newalarm.setMinutes( minute);
                        TextView showTime =sheetview.findViewById(R.id.ShowAlarmTime);
                        showTime.setText(function.Timer.FormatTime(newalarm.getHours(),newalarm.getMinutes()));
                    }
                };
                TimePickerDialog alarmTime = new TimePickerDialog(v.getContext(),onTimeSetListener,newalarm.getHours(),newalarm.getMinutes(),true);
                alarmTime.setTitle("Set alarm time");
                alarmTime.show();
            }
        });



        //-----SETUP REPEATE DAY-----------------
        newalarm.setDayactive( "");

        List<String> listDay = new ArrayList<>();
        listDay.add("Monday");
        listDay.add("Tuesday");
        listDay.add("Wednesday");
        listDay.add("Thursday");
        listDay.add("Friday");
        listDay.add("Saturday");
        listDay.add("Sunday");
        EditText repeatdaychoosed = sheetview.findViewById(R.id.Repeat_time_choosed);
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
                        String Alarm_repeatDay = "";
                        for(int position = 0 ; position < listDay.size();position++){
                            View RepeatDayCheck = showRepeatList.getLayoutManager().findViewByPosition(position);
                            TextView repeat_day = RepeatDayCheck.findViewById(R.id.repeat_day);
                            ImageView repeat_day_icon = RepeatDayCheck.findViewById(R.id.repeat_day_isCheckedicon);
                            if(repeat_day_icon.getVisibility() == View.VISIBLE){
                                Alarm_repeatDay+=repeat_day.getText()+", ";
                            }
                        }
                        //View icon = viewItem.findViewById(R.id.view);
                        EditText showRepeatDay = sheetview.findViewById(R.id.Repeat_time_choosed);
                        //Remove "," in last string
                        Alarm_repeatDay = function.removeLastChar(Alarm_repeatDay);
                        showRepeatDay.setText(Alarm_repeatDay);
                        newalarm.setDayactive(Alarm_repeatDay);
                        chooseRepeatDay.dismiss();
                    }
                });

                chooseRepeatDay.show();
            }
        });

        //-----SETUP DROPDOWN ALERT SONG MENU--------------
        AutoCompleteTextView alertsongChoosed = sheetview.findViewById(R.id.alarm_sound_choosed);
        ListSong = DataStore.getInstance(this).alertSongQuery().getAllAlertsong();

        alertsongAdapter = new ArrayAdapter<>(this, R.layout.alertsong_dropdownitem, ListSong);
        alertsongChoosed.setAdapter(alertsongAdapter);
        alertsongChoosed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                try {
                    newalarm.setAlertsong(DataStore.getInstance(view.getContext()).alertSongQuery().getAlertSong(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //-----------------SETUP TITTLE-------------
        EditText Almtitle = sheetview.findViewById(R.id.alarm_tittle_choosed);
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
                    inputTittle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inputTittle.setHint("");
                            inputTittle.setFocusable(true);
                        }
                    });

                    dialog.findViewById(R.id.alarm_settittle_savetitle).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newalarm.setTittle(inputTittle.getText().toString());
                            Almtitle.setText(newalarm.getTittle());
                            dialog.dismiss();
                        }
                    });
                }

                dialog.show();
            }
        });

        //-----------------------------------------------------------------------------

        sheetview.findViewById(R.id.add_Alarm_Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Save","Save alarm");

                DataStore.getInstance(sheetview.getContext()).alarmListQuery().createNewAlarm(newalarm);
                //Log.e("Time",Alarm_hour+" "+Alarm_minutes);
                alarm_data.addNewAlarm(newalarm);
                alarm_adapter.notifyDataSetChanged();
                //Update Alarm in AlarmProcess
                Intent SendUpdateAlarmData = new Intent();
                SendUpdateAlarmData.setAction("com.example.watchtime.source.ui.Alarm");
                SendUpdateAlarmData.putExtra(global_variable.AlarmProcessRequest,"isUpdateAlarm");
                SendUpdateAlarmData.putExtra("UpdateData",newalarm);
                sendBroadcast(SendUpdateAlarmData);
                addAlarm.dismiss();
            }
        });

        addAlarm.setContentView(sheetview);
        bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        //Set min heigh
        LinearLayout layout =  sheetview.findViewById(R.id.add_Alarm_layout);
        assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

        //Show view
        addAlarm.show();


    }

    private void  DeleteAlarmSelected(Context context){
        EditAlarm(context);
        alarm_adapter.DeleteItemSelected();
        AlarmMenuReset(optionsMenu);
    }

    private void DeleteAllAlarm(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Delete All Clock")
                .setMessage("Do you want to delete all ?")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarm_data.deleteAll();
                        DataStore.getInstance(context).alarmListQuery().deleteAllAlarm();
                        EditAlarm(context);
                        alarm_adapter.notifyDataSetChanged();
                        AlarmMenuReset(optionsMenu);

                        //Update in service
                        Intent SendUpdateAlarmData = new Intent();
                        SendUpdateAlarmData.setAction("com.example.watchtime.source.ui.Alarm");
                        SendUpdateAlarmData.putExtra(global_variable.AlarmProcessRequest,"DeleteAll");
                        //SendUpdateAlarmData.putExtra("UpdateData",listAlarm);
                        sendBroadcast(SendUpdateAlarmData);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dl = dialog.create();
        dl.show();


        //Button negativeButton =   dialog

    }


    private void EditAlarm(Context context) {
        MenuItem DeleteAllButton = optionsMenu.findItem(R.id.alarm_delete_all);
        MenuItem AddButton = optionsMenu.findItem(R.id.add_Alarm);
        if(DeleteAllButton.isVisible()){
            DeleteAllButton.setVisible(false);
            AddButton.setVisible(true);
            alarm_adapter.EditedMode = false;
            alarm_adapter.notifyDataSetChanged();
            AlarmEditmode = false;
        }
        else{
            DeleteAllButton.setVisible(true);
            AddButton.setVisible(false);
            alarm_adapter.EditedMode = true;
            alarm_adapter.notifyDataSetChanged();
            AlarmEditmode = true;
        }



    }

    //------------------------------------------LAYOUT CHANGE------------------------------------
    private void startTimerLayoutChange() {
        start_timer.setVisibility(View.GONE);
        alertsong.setVisibility(View.GONE);
        timeSetting.setVisibility(View.GONE);
        //OnCountDown
        cancel_timer.setVisibility(View.VISIBLE);
        PercentTimeLeft.setVisibility(View.VISIBLE);
        ShowTimeLeft.setVisibility(View.VISIBLE);
    }

    private void stopTimerLayoutChange() {
        start_timer.setVisibility(View.VISIBLE);
        alertsong.setVisibility(View.VISIBLE);
        timeSetting.setVisibility(View.VISIBLE);
        //OnCountDown
        cancel_timer.setVisibility(View.GONE);
        PercentTimeLeft.setVisibility(View.GONE);
        ShowTimeLeft.setVisibility(View.GONE);
    }

    private void ResetTime() {
        setCurrentHour(0);
        setCurrentMinute(0);
        setCurrentSecond(0);
    }


    //------------------------------------------TIME CHANGE------------------------------------
    private void setCurrentSecond(int currentSecond) {
        this.mCurrentSeconds = currentSecond;
        updateSecondsDisplay();
    }


    private void setCurrentMinute(int currentMinute) {
        this.mCurrentMinute = currentMinute;
        updateMinuteDisplay();
    }

    private void setCurrentHour(int currentHour) {
        this.mCurrentHour = currentHour;
        updateHourDisplay();
    }

    private void updateMinuteDisplay() {
        mMinutePicker.setValue(mCurrentMinute);
        //mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
    }

    /**
     * Set the state of the spinners appropriate to the current second.
     */
    private void updateSecondsDisplay() {
        mSecondPicker.setValue(mCurrentSeconds);
        //mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
    }

    /**
     * Set the state of the spinners appropriate to the current hour.
     */
    private void updateHourDisplay() {
        int currentHour = mCurrentHour;
        mHourPicker.setValue(currentHour);
    }


    //----------------------------------------SERVICE-------------------------------------
    private void stopTimer() {
        //DataStore.getInstance(this).timeCountdownQuery().deleteTimeLeft(data.getData().getID());
        stopTimerLayoutChange();
        stopService(new Intent(this, global_variable.TimerService));
        ResetTime();
    }


    private void startTimer() {
        Intent startTimer = new Intent(this, global_variable.TimerService);
        //Send time data
        //Log.e("",data.getData().getTotalTime()+"");
        startTimer.putExtra("Timer_data", (Serializable) timer_data);
        startService(startTimer);
    }

    /**---------------------------------WOLRD CLOCK FUNCTION------------------------*/
    //https://www.youtube.com/watch?v=kzq9uoTC590
    private Object getTimezoneData(int jsonfile, Context context) {
        JSONReader data = new JSONReader();
        List<Timezone> timezonesData = (List<Timezone>) data.convertJsonToTimeZoneObject(R.raw.timezones, Main.this);

        return timezonesData;
    }
    private void StartWorldClockService(){
        Intent startWC = new Intent(this, global_variable.worldClockService);
        //Send time data
        //Log.e("",data.getData().getTotalTime()+"");
        //startWC.putExtra("Current", (Serializable) Time);
        startService(startWC);
    }

    private void StopWorldClockService(){
        stopService(new Intent(this, global_variable.worldClockService));
    }



    private void EditWorldClock(Context context){
        if(world_clock_adapter != null){
            MenuItem addWorldClock = optionsMenu.findItem(R.id.add_worldClockmenu);
            MenuItem DeleteAllButton = optionsMenu.findItem(R.id.world_clock_delete_all);
            if(DeleteAllButton.isVisible()){
                DeleteAllButton.setVisible(false);
                addWorldClock.setVisible(true);
                world_clock_adapter.EditedMode = false;
                world_clock_adapter.notifyDataSetChanged();
                worldclockEditmode = false;

            }
            else{
                DeleteAllButton.setVisible(true);
                addWorldClock.setVisible(false);
                world_clock_adapter.EditedMode = true;
                world_clock_adapter.notifyDataSetChanged();
                worldclockEditmode = true;
            }
        }

    }

    private void  DeleteWorldClockSelected(Context context){
        EditWorldClock(context);
        worldClock_data.UpdateWorldClockList(world_clock_adapter.DeleteWorldClock());
        WorldClockMenuReset(optionsMenu);
    }

    private void DeleteAllWorldClock(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Delete All Clock")
                .setMessage("Do you want to delete all ?")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataStore.getInstance(context).worldClockListQuery().deleteAllWorldClock();
                        worldClock_data.removeAll();
                        EditWorldClock(context);
                        world_clock_adapter.notifyDataSetChanged();
                        WorldClockMenuReset(optionsMenu);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void openAddworldClockWindow(Context main) {
        //https://www.youtube.com/watch?v=hclp2377fDQ
        /**
         * SHOW POPUP WINDOWN
         * */
        addWorldClock = new BottomSheetDialog(Main.this, R.style.BottomSheetStyle);
        BottomSheetBehavior<View> bottomSheetBehavior;
        View sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_worldclock, (LinearLayout) findViewById(R.id.addWorldClockLayout));


        //Set up view
        sheetview.findViewById(R.id.addworldclock_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorldClock.dismiss();
            }
        });


        /**
         * SHOW LIST CITY
         */

        List<Timezone> listcity_ingroup = (List<Timezone>) getTimezoneData(R.raw.timezones, this);
        List<Timezone.zones> listcity = new ArrayList<>();

        for (Timezone i : listcity_ingroup) {
            Timezone.zones GroupZone = new Timezone.zones(i.getGroup(), null);
            listcity.add(GroupZone);
            for (Timezone.zones j : i.getZones()) {
                listcity.add(j);
            }
        }

        addWorldClock_adapter addWorldClock_adapter = new addWorldClock_adapter(listcity, this);
        RecyclerView showCityList = sheetview.findViewById(R.id.addworldclock_listcity);
        showCityList.setAdapter(addWorldClock_adapter);
        showCityList.setLayoutManager(new LinearLayoutManager(this));

        /**
         * SEARCH CITYS
         * */

        SearchView searchCity = sheetview.findViewById(R.id.addworldclock_search_city);
        searchCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String Search = newText;
                addWorldClock_adapter.getFilter().filter(newText);
                return false;
            }
        });

        addWorldClock.setContentView(sheetview);
        bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        //Set min heigh
        LinearLayout layout =  sheetview.findViewById(R.id.addWorldClockLayout);
        assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

        //Show view
        addWorldClock.show();

    }
}

