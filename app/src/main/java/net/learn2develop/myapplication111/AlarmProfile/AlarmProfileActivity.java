package net.learn2develop.myapplication111.AlarmProfile;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.learn2develop.myapplication111.AlarmHistoryActivity.AlarmHistoryActivity;
import net.learn2develop.myapplication111.DataBase.DBHelper;
import net.learn2develop.myapplication111.DataBase.GroceryAdapter;
import net.learn2develop.myapplication111.DataBase.GroceryContract;
import net.learn2develop.myapplication111.DateAndTimePickerFragment.DatePickerFragment;
import net.learn2develop.myapplication111.DateAndTimePickerFragment.TimePickerFragment;
import net.learn2develop.myapplication111.R;
import net.learn2develop.myapplication111.SmsActivity.AlarmSmsActivity;

import java.text.DateFormat;
import java.util.Calendar;



public class AlarmProfileActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.alarm_home:
                    Intent intent1 = new Intent(AlarmProfileActivity.this, AlarmProfileActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.history_alarm:
                    Intent intent2 = new Intent(AlarmProfileActivity.this, AlarmHistoryActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.sms_menu:
                    Intent intent3 = new Intent(AlarmProfileActivity.this, AlarmSmsActivity.class);
                    startActivity(intent3);

                    return true;
            }
            return false;
        }
    };



    Button dateButton, timeButton;
    PendingIntent pending_intent;
    EditText history_name;
    TextView update_text_history;
    Button alarm_history_off;
    AlarmManager alarm_manager;
    ImageButton sms_image_button;

    TextView dateTextView, timeTextView;  // date and time for db
    ImageButton button_set, button_delete;   // button fot db

    private SQLiteDatabase mDatabase;
    private GroceryAdapter mAdapter;

    final static int RQS_1 = 1;
    private static final String TAG = "Notification";
    public final static String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    //final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmprofileactivity);


        BottomNavigationView navView2 = findViewById(R.id.nav_view2);
        navView2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dateButton = findViewById(R.id.dateButton);                     // кнопка для дата диалог
        timeButton = findViewById(R.id.timeButton);            // кнопка для тайм диалог
        dateTextView = findViewById(R.id.dateTextView);                 // поле для дати
        timeTextView = findViewById(R.id.timeTextView);                 // поле для тайма

        final DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        mAdapter = new GroceryAdapter(this, getAllItems());

        history_name = findViewById(R.id.history_name);
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_history_off = findViewById(R.id.alarm_history_off);
        update_text_history = findViewById(R.id.update_text_history);
        sms_image_button = findViewById(R.id.sms_image_button);
        sms_image_button.setOnClickListener(this);

        button_delete = findViewById(R.id.button_delete);
        openHelper = new DBHelper(this);

        button_set = findViewById(R.id.button_set); // button for db


        setNameText();

        getIncomingIntent();



        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();

                Intent intent2 = new Intent(AlarmProfileActivity.this, AlarmHistoryActivity.class);
                startActivity(intent2);
            }
        });
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    db= openHelper.getWritableDatabase();
                    String profile = history_name.getText().toString();
                    deleteDate(profile);
                Toast.makeText(getApplicationContext(), "Deleted successfully ", Toast.LENGTH_LONG).show();

                Intent intent2 = new Intent(AlarmProfileActivity.this, AlarmHistoryActivity.class);
                startActivity(intent2);


                }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        alarm_history_off.setOnClickListener(new View.OnClickListener() { //создать onclick listener, чтобы отключить будильник или отменить установку будильника
            @Override
            public void onClick(View view) {
                StopAlarm();
            }

        });
    }

    private void addItem() {

        String profile = history_name.getText().toString();
        String name = timeTextView.getText().toString();
        String amount = dateTextView.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_PROFILE, profile);
        cv.put(GroceryContract.GroceryEntry.COLUMN_TIME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_DATE, amount);

        mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private void getIncomingIntent (){

        String profile = getIntent().getStringExtra("profileText");
        String name = getIntent().getStringExtra("nameText");
        String amount = getIntent().getStringExtra("countText");

        setList(profile,name,amount);
    }

    public  boolean deleteDate(String profile){
        return db.delete(GroceryContract.GroceryEntry.TABLE_NAME, GroceryContract.GroceryEntry.COLUMN_PROFILE + "=?", new String[]{profile})>0;
    }

    private void setList(String profile, String name, String amount){

        EditText history_name = findViewById(R.id.history_name);
        history_name.setText(profile);
        TextView timeTextView = findViewById(R.id.timeTextView);
        timeTextView.setText(name);
        TextView dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText(amount);

    }


    public void StopAlarm() {

        Intent my_intentNOT = new Intent(this, AlarmReceiver.class);
        alarm_manager.cancel(pending_intent); // відмінити alarm manager
        my_intentNOT.putExtra("extra", "alarm off");   // говоримо будтльнику що ми нажали "alarm_off" button
        sendBroadcast(my_intentNOT); // стосуэться виключення музики

        String stopText = "Stop Alarm";
        update_text_history.setText(stopText);  //метод що змінює текст в update_text
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        set_alarm_text(calendar);
        startAlarm(calendar);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        dateTextView.setText(currentDateString);

    }

    @Override
    public void onClick(View view) {        // переход на на страницу ActivityTwoSMSwindows
        switch (view.getId()){
            case R.id.sms_image_button:
                Intent intentSmsActivity = new Intent(this, AlarmSmsActivity.class);
                intentSmsActivity.putExtra("name", history_name.getText().toString());
                startActivity(intentSmsActivity);
                break;
            default:
                break;

        }
    }

    private void set_alarm_text(Calendar c) {
        String timeText = "";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        timeTextView.setText(timeText);

    }

    private void startAlarm(Calendar calendar) {
        Intent my_intentNOT = new Intent(this, AlarmReceiver.class);
        my_intentNOT.putExtra("extra", "alarm_on");    // виключаем музику
        pending_intent = PendingIntent.getBroadcast(AlarmProfileActivity.this, RQS_1, my_intentNOT, PendingIntent.FLAG_UPDATE_CURRENT); //создать отложенное намерение, которое задерживает намерение до указанного календарного времени
        alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);  // устанавливаем alarm manager
    }

    private void setNameText() {
        // Получаем сообщение из объекта intent
        Intent intent = getIntent();
        String nameForTwoActivity = intent.getStringExtra(EXTRA_MESSAGE);

        // Создаем текстовое поле
        history_name = findViewById(R.id.history_name);
        history_name.setTextSize(40);
        history_name.setText(nameForTwoActivity);

        // Устанавливаем текстовое поле в системе компоновки activity
        //setContentView(history_name);
    }

}