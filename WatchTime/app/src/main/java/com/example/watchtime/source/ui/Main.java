package com.example.watchtime.source.ui;

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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
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
import com.example.watchtime.source.Object.Timezone;
import com.example.watchtime.source.GlobalData.function;
import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.Database.DataStore;
import com.example.watchtime.source.Database.Timer.AlertSong;
import com.example.watchtime.source.Object.Timer_data;
import com.example.watchtime.source.Database.Timer.timeCountdown;
import com.example.watchtime.source.Database.WorldClock.worldClockList;
import com.example.watchtime.source.UIFunction.alarm.AlarmAdapter;
import com.example.watchtime.source.UIFunction.world_clock.addWorldClock_adapter;
import com.example.watchtime.source.UIFunction.world_clock.world_clock_adapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity implements Serializable {
    /**
     * ****MENU*****
     */
    public LinearLayout Timer;
    public LinearLayout World_Clock;
    public LinearLayout Alarm;
    private int menuToChoose = 0;
    private ViewFlipper LayoutChange;
    /**
     * ********************************TIMER****************************
     */
    public Button start_timer;
    public Button cancel_timer;


    public Timer_data data;
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
    public List<worldClockList> worldClock_data;
    public RecyclerView showWorldClock;
    //Add world clock
    public BottomSheetDialog addWorldClock;


    /**
     ********************************ALARM****************************
     */

    public AlarmList listAlarm;
    public RecyclerView showAlarm;
    public AlarmAdapter alarm_adapter;
    //AddAlarm
    public BottomSheetDialog addAlarm;
    //DATABASE
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

            if (Request.equalsIgnoreCase("isUpdateClock")) {
                UpdateClock();
            } else if (Request.equalsIgnoreCase("isAddClock")) {
                Timezone.zones region = (Timezone.zones) intent.getSerializableExtra("Region");
                if (DataStore.getInstance(Main.this).worldClockListQuery().getWorldClockByRegion(region.name) == null) {

                    worldClockList newWorldClock = new worldClockList(region.value, region.name);

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
            else if(Request.equalsIgnoreCase("AddAlarm")){

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

                data = new Timer_data();
                data.totalTime = function.Timer.toMilisecond(hours, minutes, seconds);
                data.hour = mCurrentHour;
                data.minute = mCurrentMinute;
                data.second = mCurrentSeconds;
                //alert song
                data.AlertSong = DefaultSong;

                DataStore.getInstance(Main.this).timeCountdownQuery().storeTimeLeft(new timeCountdown(global_variable.TimerID, data.totalTime, data.totalTime));
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
        showWorldClock = findViewById(R.id.worldClock_list);
        worldClock_data = DataStore.getInstance(this).worldClockListQuery().getAllWorldClock();
        world_clock_adapter = new world_clock_adapter(worldClock_data, this);

        showWorldClock.setAdapter(world_clock_adapter);
        showWorldClock.setLayoutManager(new LinearLayoutManager(this));
        //Updating Times
        Intent startWorldClock = new Intent(this, global_variable.worldClockService);
        //Send time data
        //Log.e("",data.getData().getTotalTime()+"");
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
                                        world_clock_adapter.notifyItemRemoved(position);
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
            try {
            List<com.example.watchtime.source.Database.Alarm.Alarm> lsAlarm = DataStore.getInstance(this).alarmListQuery().getAllAlarm();
            listAlarm = new AlarmList(lsAlarm);
            alarm_adapter  = new AlarmAdapter(listAlarm,this);
            showAlarm.setAdapter(alarm_adapter);
            showAlarm.setLayoutManager(new LinearLayoutManager(this));
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this,"Can't get alarm data", Toast.LENGTH_LONG);
            }

        ItemTouchHelper.SimpleCallback simpleItemTouchCallbackForAlarmItem = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        new AlertDialog.Builder(Main.this).setTitle("Delete alarm")
                                .setMessage("Delete this alarm ?")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alarm_adapter.notifyItemChanged(position);
                                        dialog.cancel();
                                    }
                                })
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DataStore.getInstance(Main.this).worldClockListQuery().deleteWorldClock(worldClock_data.get(position).getID());
                                        alarm_adapter.notifyItemRemoved(position);
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


        ItemTouchHelper itemTouchHelperForAlarmItem = new ItemTouchHelper(simpleItemTouchCallbackForAlarmItem );
        itemTouchHelperForAlarmItem.attachToRecyclerView(showAlarm);


    }




    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(TimerBroadcastReciver);
    }
    /**
     *-------------------------------MENU FUNCTION-------------------------------------------
     */
    private void TimerMenu() {
        menuToChoose = R.menu.timer;
        invalidateOptionsMenu();
    }
    private void AlarmMenu() {
        menuToChoose = R.menu.alarm;
        invalidateOptionsMenu();
    }
    private void WorldClockMenu() {
        menuToChoose = R.menu.addworldclock;
        invalidateOptionsMenu();
    }
    private void ClearOptionChoice() {
        ImageView TimerImage = findViewById(R.id.Timer_image);
        ImageView WorldClockImage = findViewById(R.id.world_clock_image);
        ImageView AlarmImage = findViewById(R.id.alarm_image);

        TimerImage.setImageResource(R.drawable.ic_baseline_timer);
        AlarmImage.setImageResource(R.drawable.ic_baseline_alarm);
        WorldClockImage.setImageResource(R.drawable.ic_baseline_world);
    }

    private void UpdateClock() {
        world_clock_adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuToChoose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_worldClockmenu:
                openAddworldClockWindow(this);
                return true;
            case R.id.add_Alarm:
                openAddAlarmWindow(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAddAlarmWindow(Main main) {
        /**
         * SHOW POPUP WINDOWN
         * */
        BottomSheetDialog addAlarm = new BottomSheetDialog(Main.this, R.style.BottomSheetStyle);
        BottomSheetBehavior<View> bottomSheetBehavior;
        View sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_alarm, (LinearLayout) findViewById(R.id.add_Alarm_layout));


        //Set up view
        sheetview.findViewById(R.id.add_Alarm_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm.dismiss();
            }
        });

        //-----SETUP REPEATE DAY-----------------
        List<String> listDay = new ArrayList<>();
        listDay.add("Monday");
        listDay.add("Tuesday");
        listDay.add("Wednesday");
        listDay.add("Thursday");
        listDay.add("Friday");
        listDay.add("Saturday");
        listDay.add("Sunday");
        AutoCompleteTextView repeatdaychoosed = findViewById(R.id.alarm_Repeat_time);
        String choosedDay = new String();
        //RecyclerView.Adapter<> RepeatDay = new
        openChooseRepeatDay();
        //-----SETUP DROPDOWN ALERT SONG MENU--------------
        AutoCompleteTextView alertsongChoosed = findViewById(R.id.alarm_sound_choosed);
        ListSong = DataStore.getInstance(this).alertSongQuery().getAllAlertsong();
        int[] ChoosedSong = {0};

        alertsongAdapter = new ArrayAdapter<>(this, R.layout.alertsong_dropdownitem, ListSong);
        alertsongChoosed.setAdapter(alertsongAdapter);
        alertsongChoosed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                try {
                    ChoosedSong[0] = DataStore.getInstance(view.getContext()).alertSongQuery().getAlertSong(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //Set min heigh
        LinearLayout layout =  sheetview.findViewById(R.id.add_Alarm_layout);
        assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

        //Show view
        addAlarm.show();


    }

    /**
     *-------------------------------ALARM FUNCTION-------------------------------------------
     */



    private void openChooseRepeatDay() {
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
        startTimer.putExtra("Timer_data", (Serializable) data);
        startService(startTimer);
    }

    //---------------------------------WOLRD CLOCK------------------------
    //https://www.youtube.com/watch?v=kzq9uoTC590
    private Object getTimezoneData(int jsonfile, Context context) {
        JSONReader data = new JSONReader();
        List<Timezone> timezonesData = (List<Timezone>) data.convertJsonToTimeZoneObject(R.raw.timezones, Main.this);

        return timezonesData;
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

